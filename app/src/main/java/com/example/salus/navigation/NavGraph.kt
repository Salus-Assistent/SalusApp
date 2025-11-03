package com.example.salus.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.salus.ui.screens.SplashScreen // Import real
import com.example.salus.ui.screens.auth.LoginScreen // Import real
import com.example.salus.ui.screens.auth.SignUpScreen // <<< IMPORT NOVO E REAL
import com.example.salus.ui.screens.paciente.*
import com.example.salus.ui.screens.common.*
import com.example.salus.ui.screens.cuidador.*


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
        exitTransition = { ExitTransition.None },
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


        composable(AppScreens.HomePaciente.route) {
            HomeScreenPaciente(navController = navController)
        }
        composable(AppScreens.RemediosPaciente.route){
            RemediosScreenPaciente(navController = navController)
        }
        composable(AppScreens.MapaPaciente.route){
            MapaScreenPaciente(navController = navController)
        }
        composable(AppScreens.Chatbot.route){
            ChatbotScreen(navController = navController)
        }
        composable(AppScreens.PerfilPaciente.route) {
            PerfilScreenPaciente(navController = navController)
        }



        // --- Fluxo Cuidador ---


        composable(AppScreens.AlertaRecebidoCuidador.route) {
            AlertaScreenCuidador(
                onEntendidoClick = {
                    // Navega para a Home do Cuidador e limpa a pilha
                    navController.navigate(AppScreens.HomeCuidador.route) {
                        // Limpa a pilha de volta até o Login (ou HomePaciente)
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(AppScreens.HomeCuidador.route) {
            HomeScreenCuidador(navController = navController)
        }
        composable(AppScreens.RegistrosCuidador.route) {
            RegistrosScreenCuidador(navController = navController)
        }
        composable(AppScreens.RemediosCuidador.route){
            RemediosScreenCuidador(navController = navController)
        }
        composable(AppScreens.MapaCuidador.route){
            MapaScreenCuidador(navController = navController)
        }
        composable(AppScreens.ChatbotCuidador.route){
            ChatbotScreenCuidador(navController = navController)
        }
        composable(AppScreens.PerfilCuidador.route) {
            PerfilScreenCuidador(navController = navController)
        }
        composable(AppScreens.EditarPerfil.route) {
            // TODO: Implementar UI
            Text("Ecrã Editar Perfil (Placeholder)")
        }
        composable(AppScreens.GerirCuidadores.route) {
            // TODO: Implementar UI
            Text("Ecrã Gerir Cuidadores (Placeholder)")
        }
        composable(AppScreens.AjudaTutorial.route) {
            // TODO: Implementar UI
            Text("Ecrã Ajuda & Tutorial (Placeholder)")
        }
        composable(AppScreens.PrivacidadeTermos.route) {
            // TODO: Implementar UI
            Text("Ecrã Privacidade & Termos (Placeholder)")
        }
        composable(AppScreens.CalendarioPaciente.route) {
            CalendarioScreenPaciente(navController = navController)
        }

        composable(AppScreens.CalendarioCuidador.route) {
            CalendarioScreenCuidador(navController = navController)
        }
        composable(AppScreens.NotificacoesPaciente.route) {
            NotificacoesScreenPaciente(navController = navController)
        }

        composable(AppScreens.NotificacoesCuidador.route) {
            NotificacoesScreenCuidador(navController = navController)
        }

        // ... (o resto dos placeholders para ForgotPassword, Home, etc.)
    }
}