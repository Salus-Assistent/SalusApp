package com.example.salus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.salus.navigation.NavGraph // Importe seu NavGraph
import com.example.salus.ui.theme.SalusTheme // Importe seu Tema
import dagger.hilt.android.AndroidEntryPoint
import com.example.salus.navigation.AppScreens

@AndroidEntryPoint // Essencial para o Hilt funcionar na Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController = androidx.core.view.WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(androidx.core.view.WindowInsetsCompat.Type.systemBars())
        val startDestination = intent.getStringExtra("START_DESTINATION")
            ?: AppScreens.Splash.route
        setContent {
            SalusTheme { // Aplica seu tema (cores, fontes)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph() // Inicia o seu gráfico de navegação (Splash -> Login -> etc.)
                }
            }
        }
    }
}