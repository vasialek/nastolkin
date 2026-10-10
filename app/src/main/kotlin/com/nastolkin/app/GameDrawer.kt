package com.nastolkin.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nastolkin.R
import com.nastolkin.home.Game
import com.nastolkin.shared.ui.ThemeMode

@Composable
fun GameDrawer(games: List<Game>, selectedGame: Game?, themeMode: ThemeMode,
    onSelectGame: (Game?) -> Unit, onSelectTheme: (ThemeMode) -> Unit) {
    ModalDrawerSheet {
        Column(Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
            Text(stringResource(R.string.board_games), style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(12.dp))
            NavigationDrawerItem(label = { Text(stringResource(R.string.all_games)) },
                selected = selectedGame == null, onClick = { onSelectGame(null) })
            games.forEach { game -> DrawerGameItem(game, selectedGame, onSelectGame) }
            HorizontalDivider(Modifier.padding(vertical = 24.dp))
            ThemeOptions(themeMode, onSelectTheme)
        }
    }
}

@Composable
private fun DrawerGameItem(game: Game, selectedGame: Game?, onSelectGame: (Game?) -> Unit) {
    val label = stringResource(game.titleResource)
    val description = stringResource(R.string.open_game, label)
    NavigationDrawerItem(label = { Text(label) }, selected = selectedGame == game,
        onClick = { onSelectGame(game) }, modifier = Modifier.semantics { contentDescription = description })
}

@Composable
private fun ThemeOptions(themeMode: ThemeMode, onSelectTheme: (ThemeMode) -> Unit) {
    Text(stringResource(R.string.theme), style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(12.dp))
    Column(Modifier.selectableGroup()) {
        ThemeMode.entries.forEach { option ->
            Row(Modifier.fillMaxWidth().sizeIn(minHeight = 48.dp)
                .selectable(selected = themeMode == option, role = Role.RadioButton, onClick = { onSelectTheme(option) })
                .padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = themeMode == option, onClick = null)
                Text(stringResource(option.labelResource), modifier = Modifier.padding(start = 12.dp))
            }
        }
    }
}
