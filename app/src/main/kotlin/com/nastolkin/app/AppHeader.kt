package com.nastolkin.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nastolkin.BuildConfig
import com.nastolkin.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(onOpenMenu: () -> Unit) {
    TopAppBar(
        title = { BrandTitle() },
        navigationIcon = {
            IconButton(onClick = onOpenMenu) {
                Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.open_game_menu))
            }
        },
        actions = {
            Text(stringResource(R.string.apk_version, BuildConfig.VERSION_NAME),
                style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp))
        }
    )
}

@Composable
private fun BrandTitle() {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(painterResource(R.drawable.ic_app), contentDescription = null,
            modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.titleMedium)
    }
}
