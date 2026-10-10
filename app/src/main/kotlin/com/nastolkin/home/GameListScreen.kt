package com.nastolkin.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nastolkin.R

@Composable
fun GameListScreen(games: List<Game>, onSelectGame: (Game) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier.testTag("game-list"), contentPadding = PaddingValues(24.dp)) {
        item {
            Text(stringResource(R.string.board_games), style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp))
        }
        items(games, key = { it.name }) { game ->
            GameRow(game, onSelectGame)
        }
    }
}

@Composable
private fun GameRow(game: Game, onSelectGame: (Game) -> Unit) {
    Column {
        ListItem(
            headlineContent = { Text(stringResource(game.titleResource)) },
            supportingContent = { Text(stringResource(game.descriptionResource)) },
            trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().clickable { onSelectGame(game) }
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}
