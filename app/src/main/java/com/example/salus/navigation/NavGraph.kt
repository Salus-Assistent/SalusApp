package com.example.salus.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember // <-- IMPORTANTE
import androidx.hilt.navigation.compose.hiltViewModel // <-- IMPORTANTE
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation // <-- IMPORTANTE para o 'navigation'
import com.example.salus.ui.screens.common.*
import com.example.salus.ui.screens.auth.*
import com.example.salus.ui.screens.paciente.*
import com.example.salus.ui.screens.cuidador.*
import com.example.salus.viewmodel.CuidadorSharedViewModel // Importe o ViewModel

// 1. Rota "mãe" para todo o fluxo de cuidador
object GraphRoutes {
    const val CUIDADOR = "cuidador_graph"
}

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
        // (O seu código para Splash, Onboarding, Login, SignUp, ForgotPassword 1 & 2
        // vem aqui, exatamente como você o tinha)
        composable(AppScreens.Splash.route) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(AppScreens.Onboarding.route) {
                        popUpTo(AppScreens.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(AppScreens.Onboarding.route){
            OnboardingScreen(
                onFinish = {
                    navController.navigate(AppScreens.Login.route){
                        popUpTo(AppScreens.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(AppScreens.Login.route) {
            LoginScreen(
                onLoginClick = { email, senha ->
                    // Para o MVP, navegar direto para o GRUPO CUIDADOR
                    navController.navigate(GraphRoutes.CUIDADOR) {
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate(AppScreens.SignUp.route) },
                onForgotPasswordClick = { navController.navigate(AppScreens.ForgotPassword1.route) }
            )
        }
        composable(AppScreens.SignUp.route) {
            SignUpScreen(
                // A lambda agora recebe a 'userRole' (String)
                onSignUpClick = { userRole ->
                    // Lógica de navegação condicional
                    if (userRole == "Paciente") {
                        navController.navigate(AppScreens.HomePaciente.route) {
                            // Limpa toda a pilha de navegação (para não voltar ao Login/Splash)
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    } else if (userRole == "Cuidador") {
                        navController.navigate(GraphRoutes.CUIDADOR) { // Navega para o GRUPO de cuidador
                            popUpTo(navController.graph.id) { inclusive = true } // Limpa toda a pilha
                        }
                    }
                    // (Se o 'userRole' for "Selecionar", o botão estará desativado,
                    // então não precisamos de um 'else' aqui)
                },
                onLoginClick = {
                    navController.popBackStack() // Volta para o Login
                }
            )
        }
        composable(AppScreens.ForgotPassword1.route) {
            ForgotPasswordScreen1(
                onConfirmClick = { navController.navigate(AppScreens.ForgotPassword2.route) },
                onLoginClick = { navController.navigate(AppScreens.Login.route) },
                onSignUpClick = { navController.navigate(AppScreens.SignUp.route) }
            )
        }
        composable(AppScreens.ForgotPassword2.route) {
            ForgotPasswordScreen2(
                onConfirmClick = {
                    navController.navigate(AppScreens.Login.route) {
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate(AppScreens.Login.route) },
                onSignUpClick = { navController.navigate(AppScreens.SignUp.route) }
            )
        }

        // --- Fluxo Paciente ---
        // (O seu código para os composables de Paciente vem aqui)
        composable(AppScreens.HomePaciente.route) {
            HomeScreenPaciente(navController = navController)
        }
        composable(AppScreens.RemediosPaciente.route){
            RemediosScreenPaciente(navController = navController)
        }
        composable(AppScreens.MapaPaciente.route){
            MapaScreenPaciente(navController = navController)
        }
        composable(AppScreens.PerfilPaciente.route) {
            PerfilScreenPaciente(navController = navController)
        }
        composable(AppScreens.GerirCuidadores.route) {
            GerirCuidadoresScreen(navController = navController)
        }
        composable(AppScreens.CalendarioPaciente.route) {
            CalendarioScreenPaciente(navController = navController)
        }
        composable(AppScreens.NotificacoesPaciente.route) {
            NotificacoesScreenPaciente(navController = navController)
        }
        composable(AppScreens.Chatbot.route){
            ChatbotScreen(navController = navController)
        }

        //--- Comum ---
        composable(AppScreens.EditarPerfil.route) {
            EditarPerfilScreen(navController = navController)
        }
        composable(AppScreens.AjudaTutorial.route) {
            AjudaTutorialScreen(navController = navController)
        }
        composable(AppScreens.PrivacidadeTermos.route) {
            PrivacidadeTermosScreen(navController = navController)
        }

        // ===== INÍCIO DO FLUXO CUIDADOR (COPIADO DO CUIDADORNAVGRAPH.KT) =====
        navigation(
            startDestination = AppScreens.HomeCuidador.route,
            route = GraphRoutes.CUIDADOR // A rota "mãe"
        ) {
            // !!! O ViewModel NÃO é criado aqui !!!

            // --- Ecrãs do Cuidador ---
            composable(AppScreens.HomeCuidador.route) {
                // 1. Obter o backStackEntry da rota "mãe" (o grupo)
                val backStackEntry = remember(it) { navController.getBackStackEntry(GraphRoutes.CUIDADOR) }
                // 2. Pedir o ViewModel "ligado" a esse backStackEntry
                val sharedViewModel: CuidadorSharedViewModel = hiltViewModel(backStackEntry)

                HomeScreenCuidador(navController = navController, sharedViewModel = sharedViewModel)
            }
            composable(AppScreens.RegistrosCuidador.route) {
                val backStackEntry = remember(it) { navController.getBackStackEntry(GraphRoutes.CUIDADOR) }
                val sharedViewModel: CuidadorSharedViewModel = hiltViewModel(backStackEntry)

                RegistrosScreenCuidador(navController = navController, sharedViewModel = sharedViewModel)
            }
            composable(AppScreens.RemediosCuidador.route) {
                val backStackEntry = remember(it) { navController.getBackStackEntry(GraphRoutes.CUIDADOR) }
                val sharedViewModel: CuidadorSharedViewModel = hiltViewModel(backStackEntry)

                RemediosScreenCuidador(navController = navController, sharedViewModel = sharedViewModel)
            }
            composable(AppScreens.MapaCuidador.route) {
                val backStackEntry = remember(it) { navController.getBackStackEntry(GraphRoutes.CUIDADOR) }
                val sharedViewModel: CuidadorSharedViewModel = hiltViewModel(backStackEntry)

                MapaScreenCuidador(navController = navController, sharedViewModel = sharedViewModel)
            }
            // --- Ecrãs Comuns (versão Cuidador) ---
            composable(AppScreens.CalendarioCuidador.route) {
                val backStackEntry = remember(it) { navController.getBackStackEntry(GraphRoutes.CUIDADOR) }
                val sharedViewModel: CuidadorSharedViewModel = hiltViewModel(backStackEntry)

                CalendarioScreenCuidador(navController = navController, sharedViewModel = sharedViewModel) // Passar o VM
            }
            composable(AppScreens.NotificacoesCuidador.route) {
                NotificacoesScreenCuidador(navController = navController)
            }

            composable(AppScreens.PerfilCuidador.route) {
                PerfilScreenCuidador(navController = navController)
            }
            composable(AppScreens.ChatbotCuidador.route){
                ChatbotScreenCuidador(navController = navController)
            }
            // Não precisa de duplicar o Chatbot, a menos que AppScreens.Chatbot seja diferente
        }
        // ===== FIM DO FLUXO CUIDADOR =====
    }
}