package com.nastolkin.games.arkhamhorror.campaigns.hemlockvale

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nastolkin.R
import com.nastolkin.games.arkhamhorror.campaignjournal.*

@Composable
fun ResidentSection(residents: List<HemlockResident>, journal: CampaignJournal, viewModel: CampaignJournalViewModel) {
    LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
        items(residents, key = { it.id }) { resident ->
            ResidentCard(resident, journal.residentProgress(resident.id),
                onChangeTrust = { viewModel.changeTrust(resident.id, it) },
                onChangeNotes = { viewModel.setResidentNotes(resident.id, it) })
        }
    }
}

@Composable
private fun ResidentCard(resident: HemlockResident, progress: ResidentProgress,
    onChangeTrust: (Int) -> Unit, onChangeNotes: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val name = stringResource(residentTitle(resident.id))
    Column {
        Row(Modifier.fillMaxWidth().heightIn(min = 56.dp), verticalAlignment = Alignment.CenterVertically) {
            Row(Modifier.weight(1f).heightIn(min = 48.dp).testTag("resident-toggle-${resident.id}")
                .clickable { expanded = expanded.not() }, verticalAlignment = Alignment.CenterVertically) {
                Text(if (expanded) "▾" else "▸", Modifier.padding(end = 8.dp))
                Text(name, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium)
            }
            TrustControls(resident.id, name, progress.trust, onChangeTrust)
        }
        if (expanded) {
            Text(name, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 8.dp))
            OutlinedTextField(progress.notes, onChangeNotes, label = { Text(stringResource(R.string.resident_notes)) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 128.dp).testTag("resident-notes-${resident.id}"))
            Spacer(Modifier.height(12.dp))
        }
        HorizontalDivider()
    }
}

@Composable
private fun TrustControls(id: String, name: String, trust: Int, onChange: (Int) -> Unit) {
    val decrease = stringResource(R.string.decrease_value, name)
    val increase = stringResource(R.string.increase_value, name)
    IconButton(onClick = { onChange(-1) }, enabled = trust > 0,
        modifier = Modifier.size(48.dp).testTag("trust-minus-$id").semantics { contentDescription = decrease }) {
        Text("−", style = MaterialTheme.typography.titleLarge)
    }
    Text(stringResource(R.string.trust_value, trust), style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(horizontal = 8.dp))
    IconButton(onClick = { onChange(1) }, enabled = trust < 6,
        modifier = Modifier.size(48.dp).testTag("trust-plus-$id").semantics { contentDescription = increase }) {
        Text("+", style = MaterialTheme.typography.titleLarge)
    }
}
