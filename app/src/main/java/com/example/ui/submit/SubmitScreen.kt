package com.example.ui.submit

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.Accent
import com.example.ui.theme.ChangaFamily
import com.example.ui.theme.Ink
import com.example.ui.theme.Muted
import com.example.ui.theme.Paper
import com.example.ui.theme.Surface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Ink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Paper)
            )
        },
        containerColor = Paper
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.submit_screen_title),
                fontFamily = ChangaFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp,
                color = Ink
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("1", fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Accent, modifier = Modifier.width(32.dp))
                Text(stringResource(R.string.submit_point_1), color = Ink, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("2", fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Accent, modifier = Modifier.width(32.dp))
                Text(stringResource(R.string.submit_point_2), color = Ink, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("3", fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Accent, modifier = Modifier.width(32.dp))
                Text(stringResource(R.string.submit_point_3), color = Ink, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:hello@zareef.app")
                        putExtra(Intent.EXTRA_SUBJECT, "New Sticker Pack Submission")
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Surface)
            ) {
                Text(stringResource(R.string.submit_email_button), fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}
