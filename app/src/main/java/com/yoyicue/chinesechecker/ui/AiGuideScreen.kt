package com.yoyicue.chinesechecker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AiGuideScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        TextButton(onClick = onBack) { Text("返回") }
        Text("AI 策略说明", style = MaterialTheme.typography.headlineMedium)
        AiGuideContent(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun AiGuideContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "弱 (随机偏好)",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "以随机权重选择走法：优先奖励能前进、跳跃、离开营地的走法，即便如此仍保留一定随机性，适合体验随意的对局。",
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = "中 (贪心评估)",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "遍历所有合法走法，计算走后己方所有棋子与目标营地之间的距离和，并加上营地滞留处罚；每回合只考虑眼前一步的最小距离和，因此执行快速但不考虑对手回应。",
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = "强 (Minimax 搜索)",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "使用迭代加深 + Alpha-Beta 剪枝的极小化极大搜索：在给定时间内探索多步分支，并结合距离评估、营地惩罚与置换表进行剪枝。既考虑自己前进，也预测其他玩家的潜在反击。",
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = "提示",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(
            text = "可在设置中调整默认难度，也可以在开局配置对单个玩家选择不同的 AI 等级与是否允许使用长跳。",
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
