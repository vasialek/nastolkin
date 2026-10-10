package com.nastolkin.games.zakhvatkoloniy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nastolkin.R

@Composable
fun CityScreen(state: CityScreenState, onSelectCity: () -> Unit, onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(Modifier.widthIn(max = 560.dp).fillMaxWidth().verticalScroll(rememberScrollState()).padding(24.dp)) {
            Text(stringResource(R.string.zakhvat_koloniy), style = MaterialTheme.typography.headlineSmall)
            Text(stringResource(R.string.random_city), style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp))
            Spacer(Modifier.height(32.dp))
            CityCard(state, onToggleVisibility)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onSelectCity, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)) {
                Text(stringResource(R.string.choose_city))
            }
        }
    }
}

@Composable
private fun CityCard(state: CityScreenState, onToggleVisibility: () -> Unit) {
    val actionLabel = stringResource(if (state.isCityVisible) R.string.hide_city else R.string.show_city)
    Card(onClick = onToggleVisibility, enabled = state.city != null,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth().semantics {
            if (state.city != null) { contentDescription = actionLabel }
        }) {
        Column(Modifier.fillMaxWidth().heightIn(min = 208.dp).padding(24.dp),
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            CityCardContent(state)
        }
    }
}

@Composable
private fun CityCardContent(state: CityScreenState) {
    val city = state.city
    val title = when {
        city == null -> stringResource(R.string.no_city_selected)
        state.isCityVisible -> city.name
        else -> stringResource(R.string.city_hidden)
    }
    Text(title, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
    if (state.city != null) {
        Spacer(Modifier.height(16.dp))
        Text(stringResource(if (state.isCityVisible) R.string.tap_to_hide else R.string.tap_to_show),
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center)
    }
}
