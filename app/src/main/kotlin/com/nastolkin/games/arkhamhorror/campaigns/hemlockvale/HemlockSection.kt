package com.nastolkin.games.arkhamhorror.campaigns.hemlockvale

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nastolkin.R
import com.nastolkin.games.arkhamhorror.JournalSection
import com.nastolkin.games.arkhamhorror.campaignjournal.*

@Composable
fun HemlockSection(section: JournalSection, journal: CampaignJournal, viewModel: CampaignJournalViewModel) {
    val repository: IHemlockRepository = remember { HemlockRepositoryMemory() }
    when (section) {
        JournalSection.LOCATIONS -> LocationSection(repository.getLocations(), journal, viewModel::setFlag)
        JournalSection.PERIODS -> PeriodSection(repository.getDays(), journal, viewModel::setFlag)
        JournalSection.RESIDENTS -> ResidentSection(repository.getResidents(), journal, viewModel)
        else -> Unit
    }
}

@Composable
private fun LocationSection(locations: List<HemlockLocation>, journal: CampaignJournal, onChange: (String, Boolean) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
        items(locations, key = { it.id }) { location ->
            val flag = "location:${location.id}"
            val checked = flag in journal.flags
            Row(Modifier.fillMaxWidth().heightIn(min = 80.dp)
                .toggleable(checked, role = Role.Checkbox, onValueChange = { onChange(flag, it) })
                .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked, onCheckedChange = null)
                Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                    Text(stringResource(locationTitle(location.id)), style = MaterialTheme.typography.titleSmall)
                    Text(stringResource(scenarioTitle(location.id)), style = MaterialTheme.typography.bodySmall)
                }
                Text(stringResource(R.string.page_number, location.page), style = MaterialTheme.typography.labelSmall)
            }
            HorizontalDivider()
        }
    }
}

@Composable
private fun PeriodSection(days: List<Int>, journal: CampaignJournal, onChange: (String, Boolean) -> Unit) {
    LazyColumn {
        items(days) { day ->
            Text(stringResource(R.string.day_number, day), style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp))
            PeriodCheckbox(day, true, "day:$day" in journal.flags) { onChange("day:$day", it) }
            PeriodCheckbox(day, false, "night:$day" in journal.flags) { onChange("night:$day", it) }
            HorizontalDivider()
        }
    }
}

@Composable
private fun PeriodCheckbox(day: Int, daytime: Boolean, checked: Boolean, onChange: (Boolean) -> Unit) {
    val title = stringResource(if (daytime) R.string.daytime else R.string.nighttime)
    val description = stringResource(R.string.period_label, day, title)
    Row(Modifier.fillMaxWidth().heightIn(min = 48.dp).semantics { contentDescription = description }
        .toggleable(checked, role = Role.Checkbox, onValueChange = onChange), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked, onCheckedChange = null)
        Text(title, Modifier.padding(start = 12.dp))
    }
}
