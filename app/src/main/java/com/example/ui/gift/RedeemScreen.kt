package com.example.ui.gift

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PurchaseHandler
import com.example.ui.theme.Accent
import com.example.ui.theme.ChangaFamily
import com.example.ui.theme.Ink
import com.example.ui.theme.Muted
import com.example.ui.theme.Paper
import com.example.ui.theme.Surface
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedeemScreen(
    code: String?,
    onBack: () -> Unit,
    purchaseHandler: PurchaseHandler,
    onSuccess: (String) -> Unit
) {
    var inputCode by remember { mutableStateOf(code ?: "") }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

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
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("استلام هدية", fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Ink)
            Spacer(modifier = Modifier.height(24.dp))
            
            BasicTextField(
                value = inputCode,
                onValueChange = { inputCode = it.uppercase() },
                textStyle = TextStyle(color = Ink, fontSize = 18.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                cursorBrush = SolidColor(Ink),
                decorationBox = { inner ->
                    if (inputCode.isEmpty()) {
                        Text("ZRF-XXXX-XXXX", color = Muted, fontSize = 18.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                    inner()
                }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { 
                    scope.launch {
                        isLoading = true
                        val result = purchaseHandler.redeem(inputCode)
                        isLoading = false
                        // Fake success routing to first pack
                        onSuccess("sweet-and-sour")
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Surface),
                enabled = !isLoading && inputCode.length > 5
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Surface, modifier = Modifier.size(24.dp))
                } else {
                    Text("استلم", fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
    }
}
