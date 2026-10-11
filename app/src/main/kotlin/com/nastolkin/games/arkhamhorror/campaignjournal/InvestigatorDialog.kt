package com.nastolkin.games.arkhamhorror.campaignjournal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.nastolkin.R

@Composable
fun InvestigatorDialog(investigator: Investigator?, onDismiss: () -> Unit, onSave: (Investigator) -> Unit) {
    var player by rememberSaveable { mutableStateOf(investigator?.playerName.orEmpty()) }
    var identity by rememberSaveable { mutableStateOf(investigator?.identity.orEmpty()) }
    var experience by rememberSaveable { mutableStateOf((investigator?.experience ?: 0).toString()) }
    var physical by rememberSaveable { mutableStateOf((investigator?.physicalTrauma ?: 0).toString()) }
    var mental by rememberSaveable { mutableStateOf((investigator?.mentalTrauma ?: 0).toString()) }
    var cards by rememberSaveable { mutableStateOf(investigator?.storyCards.orEmpty()) }
    AlertDialog(onDismissRequest = onDismiss,
        modifier = Modifier.padding(24.dp).widthIn(max = 560.dp).fillMaxWidth(),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = { Text(stringResource(if (investigator == null) R.string.add_investigator else R.string.edit_investigator)) },
        text = {
            Column(Modifier.fillMaxWidth().heightIn(max = 480.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(player, { player = it }, label = { Text(stringResource(R.string.player_name)) }, modifier = Modifier.testTag("player-name"))
                OutlinedTextField(identity, { identity = it }, label = { Text(stringResource(R.string.investigator_identity)) }, modifier = Modifier.testTag("investigator-identity"))
                if (investigator != null) {
                    NumericField(experience, R.string.experience) { experience = it }
                    NumericField(physical, R.string.physical_trauma) { physical = it }
                    NumericField(mental, R.string.mental_trauma) { mental = it }
                    OutlinedTextField(cards, { cards = it }, label = { Text(stringResource(R.string.story_cards)) })
                }
            }
        },
        confirmButton = { TextButton(onClick = {
            onSave(Investigator(investigator?.id.orEmpty(), player, identity, experience.toIntOrNull() ?: 0,
                physical.toIntOrNull() ?: 0, mental.toIntOrNull() ?: 0, cards, investigator?.status ?: InvestigatorStatus.ACTIVE))
        }) { Text(stringResource(R.string.save)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } })
}

@Composable
private fun NumericField(value: String, label: Int, onChange: (String) -> Unit) {
    OutlinedTextField(value, { text ->
        if (text.isEmpty() || text.toIntOrNull()?.let { it >= 0 } == true) { onChange(text) }
    }, label = { Text(stringResource(label)) }, singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
}
