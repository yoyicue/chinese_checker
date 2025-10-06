package com.yoyicue.chinesechecker.data

import android.content.Context
import android.util.Log
import com.yoyicue.chinesechecker.data.db.SaveGameDao
import com.yoyicue.chinesechecker.data.db.SaveGameEntity
import com.yoyicue.chinesechecker.game.Board
import com.yoyicue.chinesechecker.game.Hex
import com.yoyicue.chinesechecker.ui.game.ControllerType
import com.yoyicue.chinesechecker.ui.game.GameConfig
import com.yoyicue.chinesechecker.ui.game.PlayerConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepository(private val context: Context, private val dao: SaveGameDao) {
    companion object {
        private const val TAG = "GameRepository"
    }
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
    private val legacySaveFile get() = context.filesDir.resolve("game_save.json")

    @Serializable
    private data class HexDTO(val x: Int, val y: Int, val z: Int)

    @Serializable
    private data class OccupantDTO(val hex: HexDTO, val player: String)

    @Serializable
    private data class PlayerCfgDTO(
        val id: String,
        val controller: String, // Human or AI
        val difficulty: String?,
        val color: Long // ARGB packed
    )

    @Serializable
    private data class GameSave(
        val playerCount: Int,
        val currentPlayer: String,
        val occupant: List<OccupantDTO>,
        val lastMove: List<HexDTO>?,
        val lastOwner: String?,
        val players: List<PlayerCfgDTO>
    )

    private fun defaultColorFor(pid: Board.PlayerId): androidx.compose.ui.graphics.Color = when (pid) {
        Board.PlayerId.A -> androidx.compose.ui.graphics.Color(0xFFE53935)
        Board.PlayerId.B -> androidx.compose.ui.graphics.Color(0xFF1E88E5)
        Board.PlayerId.C -> androidx.compose.ui.graphics.Color(0xFF8E24AA)
        Board.PlayerId.D -> androidx.compose.ui.graphics.Color(0xFF43A047)
        Board.PlayerId.E -> androidx.compose.ui.graphics.Color(0xFFFDD835)
        Board.PlayerId.F -> androidx.compose.ui.graphics.Color(0xFFFF7043)
    }

    data class Restored(
        val board: Board,
        val lastMovePath: List<Hex>?,
        val lastMoveOwner: Board.PlayerId?,
        val config: GameConfig
    )

    suspend fun save(board: Board, lastMovePath: List<Hex>?, lastMoveOwner: Board.PlayerId?, config: GameConfig) = withContext(Dispatchers.IO) {
        val occ = board.occupant.map { (hex, p) -> OccupantDTO(HexDTO(hex.x, hex.y, hex.z), p.name) }
        Log.d(TAG, "save(): occ=${occ.size} current=${board.currentPlayer} lastOwner=${lastMoveOwner?.name}")
        val players = config.players.map { pc ->
            PlayerCfgDTO(
                id = pc.playerId.name,
                controller = pc.controller.name,
                difficulty = pc.difficulty?.name,
                color = pc.color.value.toLong()
            )
        }
        val save = GameSave(
            playerCount = config.playerCount,
            currentPlayer = board.currentPlayer.name,
            occupant = occ,
            lastMove = lastMovePath?.map { HexDTO(it.x, it.y, it.z) },
            lastOwner = lastMoveOwner?.name,
            players = players
        )
        val occupantJson = json.encodeToString(save.occupant)
        val lastMoveJson = save.lastMove?.let { json.encodeToString(it) }
        Log.d(TAG, "save(): occupantJsonLength=${occupantJson.length} lastMoveJsonLength=${lastMoveJson?.length ?: 0}")
        val entity = SaveGameEntity(
            id = 1,
            updatedAt = System.currentTimeMillis(),
            playerCount = save.playerCount,
            currentPlayer = save.currentPlayer,
            occupantJson = occupantJson,
            lastMoveJson = lastMoveJson,
            lastOwner = save.lastOwner,
            playersJson = json.encodeToString(save.players)
        )
        dao.upsert(entity)
    }

    suspend fun clearSave() = withContext(Dispatchers.IO) { dao.clear() }

    suspend fun load(): Restored? = withContext(Dispatchers.IO) {
        // Try DB first
        val row = dao.get()
        if (row != null) {
            Log.d(TAG, "load(): found DB row occupantJsonLength=${row.occupantJson.length} current=${row.currentPlayer}")
            return@withContext runCatching { fromEntity(row) }.getOrNull()
        }
        // Legacy file migration (one-time)
        if (!legacySaveFile.exists()) return@withContext null
        val migrated = runCatching {
            val save = json.decodeFromString<GameSave>(legacySaveFile.readText())
            val entity = SaveGameEntity(
                id = 1,
                updatedAt = System.currentTimeMillis(),
                playerCount = save.playerCount,
                currentPlayer = save.currentPlayer,
                occupantJson = json.encodeToString(save.occupant),
                lastMoveJson = save.lastMove?.let { json.encodeToString(it) },
                lastOwner = save.lastOwner,
                playersJson = json.encodeToString(save.players)
            )
            dao.upsert(entity)
            legacySaveFile.delete()
            fromEntity(entity)
        }.getOrNull()
        migrated
    }

    suspend fun hasSave(): Boolean = withContext(Dispatchers.IO) {
        // Helper to decide if a save is meaningful to continue
        fun isMeaningful(playerCount: Int, currentPlayer: String, occupantJson: String, lastMoveJson: String?): Boolean {
            return runCatching {
                // decode occupant
                val occList = json.decodeFromString<List<OccupantDTO>>(occupantJson)
                if (occList.isEmpty()) return@runCatching false
                val occ: Map<com.yoyicue.chinesechecker.game.Hex, com.yoyicue.chinesechecker.game.Board.PlayerId> = occList.associate { dto ->
                    com.yoyicue.chinesechecker.game.Hex(dto.hex.x, dto.hex.y, dto.hex.z) to com.yoyicue.chinesechecker.game.Board.PlayerId.valueOf(dto.player)
                }
                // Rebuild board and check winner
                val board = com.yoyicue.chinesechecker.game.Board.restore(
                    playerCount = playerCount,
                    occupant = occ,
                    currentPlayer = com.yoyicue.chinesechecker.game.Board.PlayerId.valueOf(currentPlayer)
                )
                if (board.winner() != null) return@runCatching false
                // Compare with initial position to detect new game state
                val init = com.yoyicue.chinesechecker.game.Board.standard(playerCount)
                val sameInit = init.occupant.size == occ.size && init.occupant.all { (h, p) -> occ[h] == p }
                // Meaningful when it's not an untouched new game state
                !sameInit
            }.getOrElse { false }
        }

        val row = dao.get()
        if (row != null) return@withContext isMeaningful(row.playerCount, row.currentPlayer, row.occupantJson, row.lastMoveJson)
        if (!legacySaveFile.exists()) return@withContext false
        // Legacy path: parse and check
        val ok = runCatching {
            val save = json.decodeFromString<GameSave>(legacySaveFile.readText())
            val occJson = json.encodeToString(save.occupant)
            isMeaningful(save.playerCount, save.currentPlayer, occJson, save.lastMove?.let { json.encodeToString(it) })
        }.getOrDefault(false)
        ok
    }

    private fun fromEntity(row: SaveGameEntity): Restored {
        val occupantList = json.decodeFromString<List<OccupantDTO>>(row.occupantJson)
        val occ: Map<Hex, Board.PlayerId> = occupantList.associate { dto ->
            Hex(dto.hex.x, dto.hex.y, dto.hex.z) to Board.PlayerId.valueOf(dto.player)
        }
        val board = Board.restore(
            playerCount = row.playerCount,
            occupant = occ,
            currentPlayer = Board.PlayerId.valueOf(row.currentPlayer)
        )
        Log.d(TAG, "fromEntity(): pieces=${occ.size} current=${board.currentPlayer}")
        val players = json.decodeFromString<List<PlayerCfgDTO>>(row.playersJson ?: "[]")
        val cfg = GameConfig(
            playerCount = row.playerCount,
            players = players.map { p ->
                val restoredColor = androidx.compose.ui.graphics.Color(p.color)
                val playerId = Board.PlayerId.valueOf(p.id)
                val safeColor = if (restoredColor.alpha <= 0f) {
                    Log.w(TAG, "fromEntity(): player=${p.id} color alpha=${restoredColor.alpha} -> fallback")
                    defaultColorFor(playerId)
                } else restoredColor
                Log.d(TAG, "fromEntity(): player=${p.id} colorLong=${p.color} alpha=${safeColor.alpha}")
                PlayerConfig(
                    playerId = playerId,
                    controller = when (p.controller) { "AI" -> ControllerType.AI else -> ControllerType.Human },
                    difficulty = p.difficulty?.let { com.yoyicue.chinesechecker.game.AiDifficulty.valueOf(it) },
                    color = safeColor
                )
            }
        )
        val lastPath = row.lastMoveJson?.let { json.decodeFromString<List<HexDTO>>(it).map { h -> Hex(h.x, h.y, h.z) } }
        val lastOwner = row.lastOwner?.let { Board.PlayerId.valueOf(it) }
        return Restored(board = board, lastMovePath = lastPath, lastMoveOwner = lastOwner, config = cfg)
    }
}
