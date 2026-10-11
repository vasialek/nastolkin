package com.nastolkin.games.arkhamhorror

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nastolkin.R
import com.nastolkin.games.arkhamhorror.campaignjournal.*
import com.nastolkin.games.arkhamhorror.campaignjournal.storage.*

@Composable
fun ArkhamScreen(storage: ICampaignJournalStorage? = null) {
    val context = LocalContext.current
    val journalStorage = storage ?: remember { CampaignJournalStorageRoom(CampaignDatabase.getInstance(context)) }
    val campaigns = remember { CampaignRepositoryMemory().getCampaigns() }
    var campaignId by rememberSaveable { mutableStateOf<String?>(null) }
    val campaign = campaigns.firstOrNull { it.id == campaignId }
    BackHandler(enabled = campaign != null) { campaignId = null }
    if (campaign == null) {
        CampaignList(campaigns) { campaignId = it.id }
    } else {
        val factory = remember(campaign.id, journalStorage) {
            viewModelFactory { initializer { CampaignJournalViewModel(campaign.id, journalStorage) } }
        }
        val journalViewModel: CampaignJournalViewModel = viewModel(key = "journal-${campaign.id}", factory = factory)
        CampaignScreen(campaign, campaigns, journalViewModel) { campaignId = it.id }
    }
}

@Composable
private fun CampaignList(campaigns: List<CampaignDefinition>, onSelect: (CampaignDefinition) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text(stringResource(R.string.campaign_journals), style = MaterialTheme.typography.headlineSmall) }
        items(campaigns, key = { it.id }) { campaign ->
            OutlinedCard(onClick = { onSelect(campaign) }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(campaignTitle(campaign.id)), modifier = Modifier.padding(20.dp))
            }
        }
    }
}
