package com.example.salus.ui.screens.cuidador // Ajuste o pacote se necessário

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.salus.R
import com.example.salus.ui.theme.* // Importe suas cores

@Composable
fun AlertaScreenCuidador(
    onEntendidoClick: () -> Unit // Ação para quando clicar em "Entendido!"
) {
    // --- Dados Simulados para o Alerta ---
    val bpmSimulado = 100
    val localizacaoSimulada = "R. Tito, 54 - Vila Romana, São Paulo - SP, 05051-000"
    // TODO: Adicionar nome e foto do paciente

    Box(
        modifier = Modifier
            .fillMaxSize()
            // Usa a cor de fundo vermelha que você especificou
            .background(AlertaFundoVermelho) // FF0000
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // Empurra os itens para as bordas
        ) {
            // 1. Logo (Topo)
            Image(
                painter = painterResource(id = R.drawable.logo_branca), // Logo branca
                contentDescription = "Logo Salus",
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.Start) // Alinha à esquerda
            )

            // 2. Ícone de Alerta (Centro)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Alerta",
                    modifier = Modifier.size(150.dp),
                    tint = AlertaIconeAmarelo // FFBE00
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 3. Botão "Entendido!"
                Button(
                    onClick = onEntendidoClick, // Navega para a próxima tela
                    shape = RoundedCornerShape(50), // Arredondado
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AlertaBotaoEntendidoFundo, // E9E9E9
                        contentColor = AlertaBotaoEntendidoTexto // 384C78
                    )
                ) {
                    Text(
                        text = "Entendido!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }
            }

            // 4. Card de Informações (Base)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AlertaInfoFundo // E9E9E9
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Coluna da Esquerda (Localização)
                    Column(modifier = Modifier.weight(1f)) {
                        // TODO: Adicionar Nome e Foto do Paciente aqui
                        Text(
                            text = "Localização:", // Rótulo
                            style = MaterialTheme.typography.labelSmall,
                            color = AlertaInfoTextoIcone, // 384C78
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = localizacaoSimulada,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AlertaInfoTextoIcone, // 384C78
                            maxLines = 3
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Coluna da Direita (BPM)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.batimentos),
                            contentDescription = "BPM",
                            modifier = Modifier.height(40.dp),
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(AlertaInfoTextoIcone) // 384C78
                        )
                        Text(
                            text = "$bpmSimulado bpm",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = AlertaInfoTextoIcone // 384C78
                        )
                    }
                }

            }
        }
    }
}
