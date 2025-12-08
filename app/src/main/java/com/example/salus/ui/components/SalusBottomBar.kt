package com.example.salus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
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

@Composable
fun SalusBottomBar(
    modifier: Modifier = Modifier,
    currentRoute: String?,
    onHomeClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SalusNavigationBarItem(
                icon = Icons.Default.Home,
                isSelected = currentRoute == AppScreens.HomePaciente.route || currentRoute == AppScreens.HomeCuidador.route,
                onClick = onHomeClick
            )

            SalusNavigationBarItem(
                icon = Icons.Default.CalendarMonth,
                isSelected = currentRoute == AppScreens.CalendarioPaciente.route,
                onClick = onCalendarClick
            )

            Spacer(modifier = Modifier.weight(1f))

            SalusNavigationBarItem(
                icon = Icons.Default.Notifications,
                isSelected = currentRoute == AppScreens.NotificacoesPaciente.route || currentRoute == AppScreens.NotificacoesCuidador.route,
                onClick = onNotificationsClick
            )

            SalusNavigationBarItem(
                icon = Icons.Default.Settings,
                isSelected = currentRoute == AppScreens.PerfilPaciente.route || currentRoute == AppScreens.PerfilCuidador.route,
                onClick = onSettingsClick
            )
        }
    }
}

@Composable
private fun RowScope.SalusNavigationBarItem(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.weight(1f)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) AzulGradienteFim else CinzaIcones
        )
    }
}
