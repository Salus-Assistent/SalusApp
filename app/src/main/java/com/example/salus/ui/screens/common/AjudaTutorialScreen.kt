package com.example.salus.ui.screens.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.salus.ui.theme.SalusTheme

// --- Dados Simulados para o Tutorial ---
private data class TutorialTopico(
    val id: Int,
    val titulo: String,
    val resposta: String
)
private val topicosAjuda = listOf(
    TutorialTopico(1, "Como funciona os Registros?", "Para se registar, clique em 'SIGN UP' na tela inicial. Preencha o seu nome, email, telefone e senha. Depois, escolha se a sua conta será de 'Paciente' ou 'Cuidador'."),
    TutorialTopico(2, "Como funcionam os Remédios?", "Na tela 'Home', clique em 'Remédios'. O seu cuidador pode adicionar os seus remédios e horários. Você receberá notificações para o lembrar de tomar cada dose."),
    TutorialTopico(3, "Como funciona a Localização?", "A sua localização só é partilhada com os seus cuidadores autorizados. Se você disparar um alerta de AVC, a sua localização exata é enviada para eles."),
    TutorialTopico(4, "Como funcionam os Alertas de AVC?", "O botão vermelho 'Alertar!' na Home é para emergências. Ao clicar, ele notifica imediatamente todos os seus cuidadores e contatos de emergência com a sua localização e BPM.")
)
// ---------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjudaTutorialScreen(navController: NavHostController) {

    // Estado para controlar qual item está expandido (-1 = nenhum)
    var itemExpandidoId by remember { mutableStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajuda & Tutorial") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
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

        // Usamos LazyColumn para a lista de tópicos
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(topicosAjuda) { topico ->
                ExpandableHelpCard(
                    titulo = topico.titulo,
                    resposta = topico.resposta,
                    isExpanded = itemExpandidoId == topico.id,
                    onClick = {
                        // Se clicar no que já está aberto, fecha-o. Senão, abre o novo.
                        itemExpandidoId = if (itemExpandidoId == topico.id) -1 else topico.id
                    }
                )
            }
        }
    }
}

// --- Componente Privado (Acordeão) ---
@Composable
private fun ExpandableHelpCard(
    titulo: String,
    resposta: String,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // O Card inteiro é clicável
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Linha do Título (Sempre visível)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Fechar" else "Expandir"
                )
            }

            // Conteúdo Expansível (Animado)
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Divider(modifier = Modifier.padding(vertical = 12.dp))
                    Text(
                        text = resposta,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


// --- Preview ---
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun AjudaTutorialScreenPreview() {
    SalusTheme {
        AjudaTutorialScreen(navController = rememberNavController())
    }
}