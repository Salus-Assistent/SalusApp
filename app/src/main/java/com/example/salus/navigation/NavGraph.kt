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
import com.example.salus.ui.screens.auth.ForgotPasswordScreen1
import com.example.salus.ui.screens.auth.ForgotPasswordScreen2
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

        composable(AppScreens.SignUp.route) {
            SignUpScreen(
                onSignUpClick = {
                    // TODO: Lógica de Cadastro (Chamar ViewModel)
                    // Por agora, podemos navegar de volta ao Login
                    navController.navigate(AppScreens.Login.route) {
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    // Navega de volta para o Login
                    navController.popBackStack()
                }
            )
        }

        composable(AppScreens.ForgotPassword1.route) {
            ForgotPasswordScreen1(
                onConfirmClick = {
                    // Navega para o Ecrã 2
                    navController.navigate(AppScreens.ForgotPassword2.route)
                },
                onLoginClick = { navController.navigate(AppScreens.Login.route) },
                onSignUpClick = { navController.navigate(AppScreens.SignUp.route) }
            )
        }

        composable(AppScreens.ForgotPassword2.route) {
            ForgotPasswordScreen2(
                onConfirmClick = {
                    // Volta para o Login e limpa a pilha de "esqueceu senha"
                    navController.navigate(AppScreens.Login.route) {
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate(AppScreens.Login.route) },
                onSignUpClick = { navController.navigate(AppScreens.SignUp.route) }
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
            EditarPerfilScreen(navController = navController)
        }

        composable(AppScreens.GerirCuidadores.route) {
            GerirCuidadoresScreen(navController = navController)
        }

        composable(AppScreens.AjudaTutorial.route) {
            AjudaTutorialScreen(navController = navController)
        }

        composable(AppScreens.PrivacidadeTermos.route) {
            PrivacidadeTermosScreen(navController = navController)
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