package com.example.salus.ui.screens.paciente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.salus.navigation.AppScreens
import com.example.salus.ui.theme.SalusTheme
import com.example.salus.ui.theme.VermelhoGradienteInicio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FASTScreen(navController: NavHostController) {


    var fastRosto by remember { mutableStateOf<Boolean?>(null) }
    var fastBracos by remember { mutableStateOf<Boolean?>(null) }
    var fastFala by remember { mutableStateOf<Boolean?>(null) }

    val onSintomaDetectado = {
        navController.navigate(AppScreens.AlertaRecebidoCuidador.route) {
            popUpTo(AppScreens.FASTScreen.route) { inclusive = true }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Teste Rápido de AVC (FAST)") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Peça à pessoa para realizar os 3 testes abaixo.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // --- 1. Rosto (Face) ---
            FASTTestCard(
                titulo = "1. Rosto (Face)",
                instrucao = "Peça à pessoa para sorrir.",
                botaoSintomaTexto = "Rosto Caído / Assimétrico",
                botaoNormalTexto = "Sorriso Normal",
                selecao = fastRosto,
                onSintomaClick = {
//                    fastRosto = false
//                    onSintomaDetectado()
                },
                onNormalClick = { fastRosto = true }
            )

            Spacer(modifier = Modifier.height(24.dp))

            FASTTestCard(
                titulo = "2. Braços (Arms)",
                instrucao = "Peça à pessoa para levantar os dois braços.",
                botaoSintomaTexto = "Um Braço Cai ou Não Levanta",
                botaoNormalTexto = "Ambos Levantam Iguais",
                selecao = fastBracos,
                onSintomaClick = {
//                    fastBracos = false
//                    onSintomaDetectado()
                },
                onNormalClick = { fastBracos = true }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- 3. Fala (Speech) ---
            FASTTestCard(
                titulo = "3. Fala (Speech)",
                instrucao = "Peça à pessoa para repetir uma frase simples.",
                botaoSintomaTexto = "Fala Arrastada / Estranha",
                botaoNormalTexto = "Fala Normal",
                selecao = fastFala,
                onSintomaClick = {
//                    fastFala = false
//                    onSintomaDetectado()
                },
                onNormalClick = { fastFala = true }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Se todos os testes forem normais, mostra um feedback
            if (fastRosto == true && fastBracos == true && fastFala == true) {
                Text(
                    text = "Nenhum sintoma claro de AVC detetado. Monitore a pessoa e ligue 192 se tiver dúvidas.",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// --- Componente Privado para o Teste ---
@Composable
private fun FASTTestCard(
    titulo: String,
    instrucao: String,
    botaoSintomaTexto: String,
    botaoNormalTexto: String,
    selecao: Boolean?, // null = não selecionado, true = normal, false = sintoma
    onSintomaClick: () -> Unit,
    onNormalClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = instrucao,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Botão de Sintoma (Perigo)
            Button(
                onClick = onSintomaClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selecao == false) MaterialTheme.colorScheme.error else VermelhoGradienteInicio.copy(alpha = 0.7f),
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.Warning, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(botaoSintomaTexto)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botão Normal (Seguro)
            OutlinedButton(
                onClick = onNormalClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (selecao == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = BorderStroke(
                    width = if (selecao == true) 2.dp else 1.dp,
                    color = if (selecao == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(botaoNormalTexto)
            }
        }
    }
}

