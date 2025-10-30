package com.example.salus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.salus.navigation.AppScreens // Importe as suas definições de rotas
import com.example.salus.ui.theme.AzulGradienteFim
import com.example.salus.ui.theme.AzulGradienteInicio
import com.example.salus.ui.theme.CinzaIcones
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.salus.ui.theme.SalusTheme

/**
 * A Barra de Navegação inferior principal.
 * Contém os 4 ícones de navegação.
 * É desenhada para ser usada no slot 'bottomBar' de um Scaffold.
 */
@Composable
fun SalusBottomBar(
    currentRoute: String?, // Rota atual para saber qual ícone destacar
    onHomeClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface, // Branco
        // O cutoutShape será aplicado automaticamente pelo Scaffold se o FAB estiver presente
    ){
        // Home
        SalusNavigationBarItem(
            label = "Home",
            icon = Icons.Default.Home,
            // Verifica se a rota atual é uma das rotas "Home"
            isSelected = currentRoute == AppScreens.HomePaciente.route || currentRoute == AppScreens.HomeCuidador.route,
            onClick = onHomeClick
        )

        // Calendário
        SalusNavigationBarItem(
            label = "Calendário",
            icon = Icons.Default.CalendarMonth,
            isSelected = currentRoute == AppScreens.Calendario.route,
            onClick = onCalendarClick
        )

        // Notificações
        SalusNavigationBarItem(
            label = "Notificações",
            icon = Icons.Default.Notifications,
            isSelected = currentRoute == AppScreens.Notificacoes.route,
            onClick = onNotificationsClick
        )

        // Perfil
        SalusNavigationBarItem(
            label = "Perfil",
            icon = Icons.Default.Person,
            isSelected = currentRoute == AppScreens.Perfil.route,
            onClick = onProfileClick
        )
    }
}

/**
 * Componente de item de navegação customizado para a SalusBottomBar.
 */
@Composable
private fun RowScope.SalusNavigationBarItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label
                // A cor será controlada pelo 'colors' abaixo
            )
        },
        // Define as cores do ícone e do texto
        colors = NavigationBarItemDefaults.colors(
            // Ícone selecionado usa a cor AzulGradienteFim
            selectedIconColor = AzulGradienteFim,
            // Ícone não selecionado usa CinzaIcones
            unselectedIconColor = CinzaIcones,
            // Texto selecionado usa a cor AzulGradienteFim
            selectedTextColor = AzulGradienteFim,
            // Texto não selecionado usa CinzaIcones
            unselectedTextColor = CinzaIcones,
            // Cor da "pílula" de fundo (indicador) - definimos como transparente
            indicatorColor = Color.Transparent
        )
    )
}

/**
 * O Floating Action Button (FAB) para o comando de voz.
 */
@Composable
fun SalusVoiceFAB(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.surfaceVariant, // Um cinza claro do tema
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Comando de Voz",
            modifier = Modifier.size(24.dp)
        )
    }
}