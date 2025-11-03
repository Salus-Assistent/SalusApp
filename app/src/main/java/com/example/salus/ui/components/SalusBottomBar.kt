package com.example.salus.ui.components // Ajuste o pacote se necessário

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.salus.navigation.AppScreens
import com.example.salus.ui.theme.AzulGradienteFim
import com.example.salus.ui.theme.CinzaIcones

/**
 * A Barra de Navegação inferior principal. (Versão Manual)
 * Agora aceita um Modifier e é construída com Surface + Row.
 */
@Composable
fun SalusBottomBar(
    modifier: Modifier = Modifier, // <-- Aceita um modificador para alinhamento
    currentRoute: String?,
    onHomeClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp), // Altura fixa da barra
        color = MaterialTheme.colorScheme.surface, // Fundo branco
        shadowElevation = 8.dp // Sombra para destacar
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            // Distribui os 5 "slots" (4 ícones + 1 Spacer) igualmente
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home
            SalusNavigationBarItem(
                icon = Icons.Default.Home,
                isSelected = currentRoute == AppScreens.HomePaciente.route || currentRoute == AppScreens.HomeCuidador.route,
                onClick = onHomeClick
            )

            // Calendário
            SalusNavigationBarItem(
                icon = Icons.Default.CalendarMonth,
                isSelected = currentRoute == AppScreens.CalendarioPaciente.route,
                onClick = onCalendarClick
            )

            // ESPAÇO VAZIO PARA O FAB
            // Este Spacer ocupa o espaço do meio onde o FAB vai ficar
            Spacer(modifier = Modifier.weight(1f))

            // Notificações
            SalusNavigationBarItem(
                icon = Icons.Default.Notifications,
                isSelected = currentRoute == AppScreens.NotificacoesPaciente.route || currentRoute == AppScreens.NotificacoesCuidador.route,
                onClick = onNotificationsClick
            )

            // Definições
            SalusNavigationBarItem( // (ou o seu IconButton)
                icon = Icons.Default.Settings,
                isSelected = currentRoute == AppScreens.PerfilPaciente.route || currentRoute == AppScreens.PerfilCuidador.route,
                onClick = onSettingsClick
            )
        }
    }
}

/**
 * O Floating Action Button (FAB) para o comando de voz.
 * Agora também aceita um modificador.
 */
@Composable
fun SalusVoiceFAB(
    modifier: Modifier = Modifier, // <-- Aceita um modificador para alinhamento/offset
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .size(75.dp), // <-- Aplica o modificador
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Comando de Voz",
            modifier = Modifier.size(30.dp)
        )
    }
}

/**
 * Componente de item de navegação customizado.
 * Usa Modifier.weight(1f) para ocupar espaço igual.
 */
@Composable
private fun RowScope.SalusNavigationBarItem(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Usamos um IconButton simples para os ícones
    IconButton(
        onClick = onClick,
        modifier = Modifier.weight(1f) // Ocupa espaço igual
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            // Define a cor baseada na seleção
            tint = if (isSelected) AzulGradienteFim else CinzaIcones
        )
    }
}

