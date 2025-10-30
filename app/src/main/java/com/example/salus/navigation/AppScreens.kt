package com.example.salus.navigation

/**
 * Sealed class para gerir as rotas da aplicação Salus de forma segura.
 */
sealed class AppScreens(val route: String) {
    // Fluxo Inicial
    object Splash : AppScreens("splash")
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
    object SelecionarPaciente : AppScreens("selecionar_paciente") // Para o cuidador escolher
    object RemediosCuidador : AppScreens("remedios_cuidador") // Precisa de ID do paciente?
    object MapaCuidador : AppScreens("mapa_cuidador") // Precisa de ID do paciente?
    object AlertaRecebidoCuidador : AppScreens("alerta_recebido_cuidador") // Precisa de ID do alerta?

    // Telas Comuns
    object Calendario : AppScreens("calendario")
    object Notificacoes : AppScreens("notificacoes")
    object Perfil : AppScreens("perfil")
    object Chatbot : AppScreens("chatbot")

    // Rota com argumento (Exemplo se precisarmos ir para detalhes de algo)
    // object DetalheConsulta : AppScreens("consulta_detalhe/{consultaId}") {
    //     fun comId(consultaId: String) = "consulta_detalhe/$consultaId"
    // }
}