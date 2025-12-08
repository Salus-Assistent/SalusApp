package com.example.salus.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.salus.ui.theme.AzulGradienteFim

@Composable
fun SalusVoiceFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isListening: Boolean = false
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(90.dp),
        shape = CircleShape,
        containerColor = if (isListening) AzulGradienteFim else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (isListening) Color.White else AzulGradienteFim
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Comando de Voz",
            modifier = Modifier.size(48.dp)
        )
    }
}