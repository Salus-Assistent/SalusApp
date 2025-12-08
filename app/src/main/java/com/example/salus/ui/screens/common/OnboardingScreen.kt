package com.example.salus.ui.screens.common // Ajuste o pacote se necessário

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager // <-- IMPORT DO PAGER
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.salus.R
import com.example.salus.ui.theme.*
import kotlinx.coroutines.launch
import android.Manifest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState


// --- Dados para as páginas do Onboarding ---
private data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String,
    val isPermissionPage: Boolean = false
)

private val onboardingPages = listOf(
    OnboardingPage(
        imageRes = R.drawable.salus_alerta, // TODO: Adicionar imagem
        title = "Bem-vindo ao Salus",
        description = "A sua rede de segurança e cuidado para emergências de AVC."
    ),
    OnboardingPage(
        imageRes = R.drawable.salus_onboarding_1, // TODO: Adicionar imagem
        title = "Alerta Imediato",
        description = "Com um único toque, notifique os seus contactos de emergência com a sua localização."
    ),
    OnboardingPage(
        imageRes = R.drawable.salus_onboarding_2, // TODO: Adicionar imagem
        title = "Cuidado Conectado",
        description = "Permita que Cuidadores ajudem a gerir os seus remédios, consultas e monitorem o seu bem-estar."
    ),
    OnboardingPage(
        imageRes = R.drawable.salus_onboarding_3, // TODO: Adicionar imagem ic_onboarding_permissions
        title = "Permissões Necessárias",
        description = "Para funcionar corretamente, o Salus precisa de permissão para:" +
                "1. Enviar SMS (para os seus contatos de emergência)." +
                "2. Aceder à sua Localização (para o link do mapa no SMS)."
        ,isPermissionPage = true // Marca esta como a página especial
    )
    // Podemos adicionar um 4º ecrã para permissões se quisermos
)
// ------------------------------------

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class) // <-- MUDANÇA
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit // Ação para navegar para o Login
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    // --- Lógica das Permissões ---
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO
        )
    )

    // O botão "Começar" só ativa se as permissões forem dadas
    val isFinishButtonEnabled = permissionsState.allPermissionsGranted

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Pager (O conteúdo que desliza) ---
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = !onboardingPages[pagerState.currentPage].isPermissionPage // Bloqueia o deslize na última página
            ) { pageIndex ->
                OnboardingPageItem(
                    page = onboardingPages[pageIndex],
                    // Passa o estado das permissões para a última página
                    permissionsState = if (pageIndex == onboardingPages.size - 1) permissionsState else null
                )
            }

            // --- Indicador de Página (Pontos) ---
            Row(
                Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else CinzaIcones
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(12.dp)
                    )
                }
            }

            // --- Botão "Próximo" / "Começar" ---
            Button(
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage < pagerState.pageCount - 1) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            // Está na última página (Permissões)
                            onFinish()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                // MUDANÇA: Desativa o botão "Começar" se as permissões não forem dadas
                enabled = if (pagerState.currentPage == pagerState.pageCount - 1) isFinishButtonEnabled else true
            ) {
                Text(
                    text = if (pagerState.currentPage == pagerState.pageCount - 1) "Começar" else "Próximo",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Composable que desenha o conteúdo de uma única página do Onboarding.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun OnboardingPageItem(
    page: OnboardingPage,
    permissionsState: MultiplePermissionsState? // Tipo complexo
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        // --- MUDANÇA: Mostra o botão de pedir permissão ---
        if (page.isPermissionPage && permissionsState != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { permissionsState.launchMultiplePermissionRequest() },
                // Mostra apenas se as permissões AINDA NÃO foram dadas
                enabled = !permissionsState.allPermissionsGranted
            ) {
                Text(if (permissionsState.allPermissionsGranted) "Permissões Concedidas!" else "Permitir Acesso")
            }
        }
    }
}

// --- Preview ---
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun OnboardingScreenPreview() {
    SalusTheme {
        OnboardingScreen(onFinish = {})
    }
}
