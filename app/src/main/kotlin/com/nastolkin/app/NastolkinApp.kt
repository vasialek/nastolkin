package com.nastolkin.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nastolkin.games.zakhvatkoloniy.CityScreen
import com.nastolkin.games.zakhvatkoloniy.CityViewModel
import com.nastolkin.home.Game
import com.nastolkin.home.GameListScreen
import com.nastolkin.home.GameRepositoryMemory
import com.nastolkin.home.IGameRepository
import com.nastolkin.shared.ui.NastolkinTheme
import com.nastolkin.shared.ui.ThemeMode
import kotlinx.coroutines.launch

@Composable
fun NastolkinApp(cityViewModel: CityViewModel, gameRepository: IGameRepository = remember { GameRepositoryMemory() }) {
    val games = remember(gameRepository) { gameRepository.getGames() }
    var selectedGame by rememberSaveable { mutableStateOf<Game?>(null) }
    var themeMode by rememberSaveable { mutableStateOf(ThemeMode.SYSTEM) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    BackHandler(enabled = selectedGame != null && drawerState.isClosed) { selectedGame = null }
    NastolkinTheme(themeMode) {
        ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
            GameDrawer(games, selectedGame, themeMode, onSelectGame = { game ->
                selectedGame = game
                coroutineScope.launch { drawerState.close() }
            }, onSelectTheme = { themeMode = it })
        }) {
            Scaffold(topBar = { AppHeader { coroutineScope.launch { drawerState.open() } } }) { padding ->
                Box(Modifier.fillMaxSize().padding(padding)) {
                    GameContent(selectedGame, games, cityViewModel, onSelectGame = { selectedGame = it })
                }
            }
        }
    }
}

@Composable
private fun GameContent(selectedGame: Game?, games: List<Game>, cityViewModel: CityViewModel,
    onSelectGame: (Game) -> Unit) {
    when (selectedGame) {
        null -> GameListScreen(games, onSelectGame, Modifier.fillMaxSize())
        Game.ZAKHVAT_KOLONIY -> {
            val cityState by cityViewModel.state.collectAsStateWithLifecycle()
            CityScreen(cityState, onSelectCity = { cityViewModel.selectRandomCity() },
                onToggleVisibility = { cityViewModel.toggleCityVisibility() })
        }
    }
}
