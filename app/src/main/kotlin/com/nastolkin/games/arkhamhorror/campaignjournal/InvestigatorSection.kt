package com.nastolkin.games.arkhamhorror.campaignjournal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nastolkin.R

@Composable
fun InvestigatorSection(journal: CampaignJournal, viewModel: CampaignJournalViewModel) {
    var adding by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<Investigator?>(null) }
    var retiring by remember { mutableStateOf<Investigator?>(null) }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
        if (journal.activeInvestigators.isEmpty()) {
            item { Text(stringResource(R.string.no_investigators), modifier = Modifier.padding(vertical = 16.dp)) }
        }
        items(journal.activeInvestigators, key = { it.id }) { investigator ->
            InvestigatorCard(investigator, onEdit = { editing = investigator }, onRetire = { retiring = investigator })
        }
        if (journal.activeInvestigators.size < 4) {
            item { OutlinedButton(onClick = { adding = true }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                Text(stringResource(R.string.add_investigator))
            } }
        }
    }
    if (adding || editing != null) {
        InvestigatorDialog(editing, onDismiss = { adding = false; editing = null }) { investigator ->
            if (editing == null) viewModel.addInvestigator(investigator.playerName, investigator.identity)
            else viewModel.updateInvestigator(investigator)
            if (viewModel.state.value.error == null) { adding = false; editing = null }
        }
    }
    retiring?.let { investigator ->
        RetirementDialog(investigator, onDismiss = { retiring = null }) { status ->
            viewModel.retireInvestigator(investigator.id, status)
            retiring = null
        }
    }
}

@Composable
private fun InvestigatorCard(investigator: Investigator, onEdit: () -> Unit, onRetire: () -> Unit) {
    OutlinedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(investigator.identity, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(investigator.playerName, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("${stringResource(R.string.experience)}: ${investigator.experience}")
            Text("${stringResource(R.string.physical_trauma)}: ${investigator.physicalTrauma}")
            Text("${stringResource(R.string.mental_trauma)}: ${investigator.mentalTrauma}")
            if (investigator.storyCards.isNotBlank()) { Text(investigator.storyCards) }
            TextButton(onClick = onEdit) { Text(stringResource(R.string.edit)) }
            TextButton(onClick = onRetire) { Text(stringResource(R.string.retire)) }
        }
    }
}

@Composable
private fun RetirementDialog(investigator: Investigator, onDismiss: () -> Unit, onRetire: (InvestigatorStatus) -> Unit) {
    AlertDialog(onDismissRequest = onDismiss,
        text = { Text(stringResource(R.string.confirm_retirement, investigator.identity)) },
        confirmButton = {
            Column {
                TextButton(onClick = { onRetire(InvestigatorStatus.KILLED) }) { Text(stringResource(R.string.killed)) }
                TextButton(onClick = { onRetire(InvestigatorStatus.INSANE) }) { Text(stringResource(R.string.insane)) }
            }
        }, dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } })
}

@Composable
fun RetiredSection(journal: CampaignJournal) {
    val retired = journal.investigators.filter { it.status != InvestigatorStatus.ACTIVE }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (retired.isEmpty()) { item { Text(stringResource(R.string.no_retired)) } }
        items(retired, key = { it.id }) { investigator ->
            ListItem(headlineContent = { Text(investigator.identity) }, supportingContent = {
                Text(stringResource(if (investigator.status == InvestigatorStatus.KILLED) R.string.killed else R.string.insane))
            })
        }
    }
}
