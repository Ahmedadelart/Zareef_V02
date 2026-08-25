#!/bin/bash
cat << 'KOTLIN' > app/src/main/java/com/example/ui/shop/ShopScreen.kt
package com.example.ui.shop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.ZareefLogo
import com.example.ui.shop.tabs.CategoriesTab
import com.example.ui.shop.tabs.FreeTab
import com.example.ui.shop.tabs.HomeTab
import com.example.ui.shop.tabs.NewTab
import com.example.ui.shop.tabs.RankTab
import com.example.ui.shop.tabs.TopArtistsTab
import com.example.ui.theme.ChangaFamily
import com.example.ui.theme.Ink
import com.example.ui.theme.Muted

@Composable
fun ShopScreen(
    viewModel: ShopViewModel,
    onPackClick: (String) -> Unit,
    onArtistClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        R.string.tab_home,
        R.string.tab_rank,
        R.string.tab_new,
        R.string.tab_top_artists,
        R.string.tab_free,
        R.string.tab_categories
    )

    Column(modifier = modifier.fillMaxSize()) {
        ZareefLogo(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp))
        
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = com.example.ui.theme.Surface,
            contentColor = Ink,
            edgePadding = 8.dp,
            divider = {},
            indicator = { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        height = 2.dp,
                        color = Ink
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, titleRes ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = stringResource(titleRes),
                            fontFamily = ChangaFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (selectedTabIndex == index) Ink else Muted
                        )
                    }
                )
            }
        }
        
        Surface(modifier = Modifier.weight(1f), color = com.example.ui.theme.Paper) {
            when (selectedTabIndex) {
                0 -> HomeTab(viewModel, onPackClick, onArtistClick, onSubmitClick)
                1 -> RankTab(viewModel, onPackClick)
                2 -> NewTab(viewModel, onPackClick)
                3 -> TopArtistsTab(viewModel, onArtistClick)
                4 -> FreeTab(viewModel, onPackClick)
                5 -> CategoriesTab(viewModel, onCategoryClick)
            }
        }
    }
}
KOTLIN
