package com.example.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.PackCard
import com.example.ui.theme.*

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onPackClick: (String) -> Unit
) {
    val query by viewModel.query.collectAsState()
    val results by viewModel.results.collectAsState()
    
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxSize().background(Paper)) {
        // Search bar
        Box(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Surface, RoundedCornerShape(12.dp))
                    .border(if (query.isNotEmpty()) 1.5.dp else 1.dp, if (query.isNotEmpty()) Ink else Line, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Ink)
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = query,
                    onValueChange = { viewModel.onQueryChange(it) },
                    textStyle = TextStyle(color = Ink, fontSize = 16.sp, fontFamily = AlexandriaFamily),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { 
                        viewModel.performSearch()
                        focusManager.clearFocus()
                    }),
                    singleLine = true,
                    cursorBrush = SolidColor(Ink),
                    modifier = Modifier.weight(1f).focusRequester(focusRequester),
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text("بحث", color = Muted, fontSize = 16.sp, fontFamily = AlexandriaFamily)
                        }
                        innerTextField()
                    }
                )
                if (query.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onQueryChange("") }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Ink, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }

        if (query.isEmpty()) {
            // Suggestions
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(stringResource(R.string.search_popular), color = Ink, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(viewModel.popularSearches) { tag ->
                        Box(
                            modifier = Modifier
                                .border(1.dp, Line, RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.setFilter(tag) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(tag, color = Ink, fontSize = 14.sp)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Text(stringResource(R.string.search_emojis), color = Ink, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(viewModel.popularEmojis) { emoji ->
                        Text(emoji, fontSize = 32.sp, modifier = Modifier.clickable { viewModel.setFilter(emoji) })
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                // Filters
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val filters = listOf(R.string.filter_animated, R.string.filter_free, R.string.filter_egyptian, R.string.filter_gulf)
                    items(filters) { f ->
                        val text = stringResource(f)
                        Box(
                            modifier = Modifier
                                .background(Surface, RoundedCornerShape(8.dp))
                                .border(1.dp, Line, RoundedCornerShape(8.dp))
                                .clickable { viewModel.setFilter(text) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(text, color = Ink, fontSize = 14.sp)
                        }
                    }
                }
            }
        } else {
            if (results.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(R.string.search_empty), color = Ink, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.search_empty_hint), color = Muted, fontSize = 16.sp)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(results) { pack ->
                        PackCard(pack = pack, onClick = { onPackClick(pack.slug) })
                    }
                }
            }
        }
    }
}
