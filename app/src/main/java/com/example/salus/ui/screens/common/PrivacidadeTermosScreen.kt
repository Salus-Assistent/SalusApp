package com.example.salus.ui.screens.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.salus.ui.theme.SalusTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacidadeTermosScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacidade & Termos") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Para texto longo
        ) {
            // Formato de cabeçalhos e parágrafos (como pedido)
            Text(
                "Política de Privacidade",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                        "Phasellus imperdiet, nulla et dictum interdum, nisi lorem egestas " +
                        "nisi, vitae scelerisque enim ligula venenatis dolor. " +
                        "Maecenas nisl est, ultrices nec congue eget, auctor vitae massa. " +
                        "Fusce luctus vestibulum augue ut aliquet. Nunc sagittis dictum " +
                        "nisi, sed ullamcorper ipsum dignissim ac. ... (Texto placeholder longo aqui)",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Termos de Uso",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                "1. Aceitação dos Termos. Ao aceder e utilizar a aplicação Salus, " +
                        "você aceita e concorda em estar vinculado pelos termos e disposições " +
                        "deste acordo. \n\n" +
                        "2. Licença de Uso. É concedida permissão para baixar temporariamente " +
                        "uma cópia dos materiais (informação ou software) na aplicação Salus " +
                        "apenas para visualização transitória pessoal e não comercial. ... " +
                        "(Texto placeholder longo aqui)",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacidadeTermosScreenPreview() {
    SalusTheme {
        PrivacidadeTermosScreen(navController = rememberNavController())
    }
}