package com.yoyicue.chinesechecker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yoyicue.chinesechecker.R

@Composable
fun AiGuideScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        TextButton(onClick = onBack) { Text(stringResource(R.string.common_back)) }
        Text(stringResource(R.string.ai_guide_title), style = MaterialTheme.typography.headlineMedium)
        AiGuideContent(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun AiGuideContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val sections = listOf(
            R.string.ai_guide_weak_title to R.string.ai_guide_weak_desc,
            R.string.ai_guide_medium_title to R.string.ai_guide_medium_desc,
            R.string.ai_guide_strong_title to R.string.ai_guide_strong_desc
        )
        sections.forEachIndexed { index, (titleRes, bodyRes) ->
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = if (index == 0) 8.dp else 16.dp)
            )
            Text(stringResource(bodyRes), modifier = Modifier.padding(top = 4.dp))
        }
        Text(
            text = stringResource(R.string.ai_guide_tip_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(stringResource(R.string.ai_guide_tip_body), modifier = Modifier.padding(top = 4.dp))
    }
}
