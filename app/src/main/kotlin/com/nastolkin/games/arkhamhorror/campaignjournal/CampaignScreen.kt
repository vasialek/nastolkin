package com.nastolkin.games.arkhamhorror.campaignjournal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nastolkin.R
import com.nastolkin.games.arkhamhorror.*
import com.nastolkin.games.arkhamhorror.campaigns.hemlockvale.HemlockSection

@Composable
fun CampaignScreen(campaign: CampaignDefinition, campaigns: List<CampaignDefinition>,
    viewModel: CampaignJournalViewModel, onSelectCampaign: (CampaignDefinition) -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var section by rememberSaveable(campaign.id) { mutableStateOf(JournalSection.INVESTIGATORS) }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        JournalSelector(stringResource(campaignTitle(campaign.id)), stringResource(R.string.choose_campaign),
            campaigns.map { stringResource(campaignTitle(it.id)) }) { onSelectCampaign(campaigns[it]) }
        JournalSelector(stringResource(sectionTitle(section)), stringResource(R.string.choose_section),
            campaign.sections.map { stringResource(sectionTitle(it)) }) { section = campaign.sections[it] }
        SaveIndicator(state, viewModel::retry)
        val journal = state.journal
        if (journal != null) {
            key(campaign.id, section) { JournalContent(section, journal, viewModel) }
        }
    }
    state.error?.let { error ->
        AlertDialog(onDismissRequest = viewModel::clearError,
            text = { Text(stringResource(errorMessage(error))) },
            confirmButton = { TextButton(onClick = viewModel::clearError) { Text(stringResource(android.R.string.ok)) } })
    }
}

@Composable
private fun JournalSelector(label: String, description: String, options: List<String>, onSelect: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        TextButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
            .semantics { contentDescription = description }) {
            Text(label, Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("▾")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEachIndexed { index, text ->
                DropdownMenuItem(text = { Text(text) }, onClick = { expanded = false; onSelect(index) })
            }
        }
    }
}

@Composable
private fun SaveIndicator(state: JournalState, onRetry: () -> Unit) {
    val label = when (state.saveStatus) {
        SaveStatus.LOADING -> R.string.loading
        SaveStatus.SAVING -> R.string.saving
        SaveStatus.SAVED -> R.string.saved
        SaveStatus.FAILED -> if (state.journal == null) R.string.load_failed else R.string.save_failed
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(stringResource(label), Modifier.weight(1f).padding(vertical = 8.dp),
            style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (state.saveStatus == SaveStatus.FAILED) {
            TextButton(onClick = onRetry) { Text(stringResource(R.string.retry)) }
        }
    }
}

@Composable
private fun JournalContent(section: JournalSection, journal: CampaignJournal, viewModel: CampaignJournalViewModel) {
    when (section) {
        JournalSection.INVESTIGATORS -> InvestigatorSection(journal, viewModel)
        JournalSection.RETIRED -> RetiredSection(journal)
        JournalSection.NOTES -> Column(Modifier.verticalScroll(rememberScrollState())) {
            OutlinedTextField(value = journal.notes, onValueChange = viewModel::setNotes,
                label = { Text(stringResource(R.string.campaign_notes)) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 240.dp).testTag("campaign-notes"))
        }
        else -> HemlockSection(section, journal, viewModel)
    }
}

private fun errorMessage(error: JournalError): Int = when (error) {
    JournalError.IDENTITY_REQUIRED -> R.string.identity_required
    JournalError.DUPLICATE_IDENTITY -> R.string.duplicate_identity
    JournalError.PARTY_FULL -> R.string.party_full
}
