package com.example.salus.navigation

/**
 * Sealed class para gerir as rotas da aplicação Salus de forma segura.
 */
sealed class AppScreens(val route: String) {
    // Fluxo Inicial
    object Splash : AppScreens("splash")
    object Onboarding : AppScreens("onboarding")
    object Login : AppScreens("login")
    object SignUp : AppScreens("signup")
    object ForgotPassword1 : AppScreens("forgot_password_1")
    object ForgotPassword2 : AppScreens("forgot_password_2")

    // Fluxo Paciente
    object HomePaciente : AppScreens("home_paciente")
    object RemediosPaciente : AppScreens("remedios_paciente")
    object MapaPaciente : AppScreens("mapa_paciente")

    // Fluxo Cuidador
    object HomeCuidador : AppScreens("home_cuidador")
    object RegistrosCuidador : AppScreens("registros_cuidador")
    object RemediosCuidador : AppScreens("remedios_cuidador") // Precisa de ID do paciente?
    object MapaCuidador : AppScreens("mapa_cuidador") // Precisa de ID do paciente?
    object AlertaRecebidoCuidador : AppScreens("alerta_recebido_cuidador") // Precisa de ID do alerta?
    object SelecionarPaciente : AppScreens("selecionar_paciente")
    object ChatbotCuidador : AppScreens("chatbot_cuidador")

    object CalendarioPaciente : AppScreens("calendario_paciente")
    object CalendarioCuidador : AppScreens("calendario_cuidador")
    object NotificacoesPaciente : AppScreens("notificacoes_paciente")
    object NotificacoesCuidador : AppScreens("notificacoes_cuidado")
    object PerfilPaciente : AppScreens("perfil_paciente")
    object PerfilCuidador : AppScreens("perfil_cuidador")
    object Chatbot : AppScreens("chatbot")

    object EditarPerfil : AppScreens("editar_perfil")
    object GerirCuidadores : AppScreens("gerir_cuidadores")
    object GerirPacientes : AppScreens("gerir_pacientes")
    object AjudaTutorial : AppScreens("ajuda_tutorial")
    object PrivacidadeTermos : AppScreens("privacidade_termos")
    object FASTScreen : AppScreens("fast_screen")


    // Rota com argumento (Exemplo se precisarmos ir para detalhes de algo)
    // object DetalheConsulta : AppScreens("consulta_detalhe/{consultaId}") {
    //     fun comId(consultaId: String) = "consulta_detalhe/$consultaId"
    // }
    object GraphRoutes {
        const val CUIDADOR = "cuidador_graph"
    }
}