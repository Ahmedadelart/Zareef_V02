package com.example.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.components.PackCard
import com.example.ui.theme.*

@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    onSettingsClick: () -> Unit,
    onRedeemClick: () -> Unit,
    onPackClick: (String) -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val favouritePacks by viewModel.favouritePacks.collectAsState()
    val ownedPacks by viewModel.ownedPacks.collectAsState()
    
    LazyColumn(modifier = Modifier.fillMaxSize().background(Paper)) {
        item {
            if (user == null) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(80.dp).background(Line, CircleShape))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(stringResource(R.string.account_welcome), fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Ink)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.account_signin_prompt), color = Muted, fontSize = 14.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedButton(
                        onClick = { viewModel.signIn() },
                        modifier = Modifier.height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Surface, contentColor = Ink)
                    ) {
                        Text(stringResource(R.string.sign_in_google), fontFamily = ChangaFamily, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(80.dp).background(Line, CircleShape).clip(CircleShape)) {
                        AsyncImage(model = user!!.photoUrl, contentDescription = null, modifier = Modifier.fillMaxSize())
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(user!!.displayName, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Ink)
                    Text(user!!.email, color = Muted, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        stringResource(R.string.sign_out),
                        color = Muted,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { viewModel.signOut() }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
                if (favouritePacks.isNotEmpty()) {
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                    Text(
                        stringResource(R.string.favorites),
                        fontFamily = ChangaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Ink,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(favouritePacks) { pack ->
                            PackCard(pack = pack, onClick = { onPackClick(pack.slug) }, modifier = Modifier.width(120.dp))
                        }
                    }
                }
            }
        }
        
        if (ownedPacks.isNotEmpty()) {
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                    Text(
                        stringResource(R.string.my_packs),
                        fontFamily = ChangaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Ink,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(ownedPacks) { pack ->
                            PackCard(pack = pack, onClick = { onPackClick(pack.slug) }, modifier = Modifier.width(120.dp))
                        }
                    }
                }
            }
        }
        
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Line, RoundedCornerShape(12.dp))
                        .background(Surface, RoundedCornerShape(12.dp))
                        .clickable { onRedeemClick() }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.have_gift_code), color = Ink, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null, tint = Ink)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Line, RoundedCornerShape(12.dp))
                        .background(Surface, RoundedCornerShape(12.dp))
                        .clickable { onSettingsClick() }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.settings), color = Ink, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null, tint = Ink)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
