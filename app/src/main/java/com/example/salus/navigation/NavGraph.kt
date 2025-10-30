package com.example.salus.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.salus.ui.screens.SplashScreen // Import real
import com.example.salus.ui.screens.auth.LoginScreen // Import real
import com.example.salus.ui.screens.auth.SignUpScreen // <<< IMPORT NOVO E REAL
import com.example.salus.ui.screens.paciente.HomeScreenPaciente

// ... (imports para outras telas placeholder)

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppScreens.Splash.route,

) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        // --- Fluxo Inicial ---

        // 1. Splash -> Login
        composable(AppScreens.Splash.route) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(AppScreens.Login.route) {
                        popUpTo(AppScreens.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Login -> SignUp
        composable(AppScreens.Login.route) {
            LoginScreen(
                onLoginClick = { email, senha ->
                    // Para o MVP, navegar direto para a Home do Paciente
                    // e limpar a pilha de navegação de autenticação
                    navController.navigate(AppScreens.HomePaciente.route) {
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                        // Alternativamente, popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate(AppScreens.SignUp.route) },
                onForgotPasswordClick = { navController.navigate(AppScreens.ForgotPassword1.route) }
            )
        }

        // 3. SignUp (Substituindo o Placeholder)
        composable(AppScreens.SignUp.route) {
            SignUpScreen( // <<< USANDO A TELA REAL
                onSignUpClick = {
                    // TODO: Lógica de Cadastro (Chamar ViewModel)
                },
                onLoginClick = {
                    // Navega de volta para o Login e limpa a pilha
                    navController.navigate(AppScreens.Login.route) {
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // --- Fluxo Paciente ---

        // ===== ADICIONE ESTE BLOCO DE CÓDIGO =====
        composable(AppScreens.HomePaciente.route) {
            HomeScreenPaciente(
                // Passamos o navController para que a Home consiga navegar
                // para o Calendário, Notificações, Perfil, etc.
                navController = navController
            )
        }

        // ... (o resto dos placeholders para ForgotPassword, Home, etc.)
    }
}