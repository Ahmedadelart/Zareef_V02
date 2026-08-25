package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.ChangaFamily
import com.example.ui.theme.Ink
import com.example.ui.theme.Muted

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    onViewAllClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = ChangaFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Ink
        )
        if (onViewAllClick != null) {
            Text(
                text = stringResource(R.string.view_all),
                fontSize = 12.sp,
                color = Muted,
                modifier = Modifier.clickable(onClick = onViewAllClick)
            )
        }
    }
}
