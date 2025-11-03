package com.example.salus.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction // Importe ImeAction se quiser configurar o botão Enter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.salus.ui.theme.AuthIconeCampo // Importe a cor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email // Importe um ícone para o preview
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.salus.ui.theme.SalusTheme // Importe seu tema


val CorBordaCampo = Color(0x26000000)
@Composable
fun SalusTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default, // Adicionamos ImeAction como parâmetro
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
//            .height(55.dp),
            .heightIn(min = 56.dp),
//            .padding(vertical = 2.dp),
        label = { Text(text = labelText, color = AuthIconeCampo) },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null, tint = AuthIconeCampo) },
        shape = RoundedCornerShape(15.dp),
        colors = OutlinedTextFieldDefaults.colors(

            //cor da borda
            focusedBorderColor = AuthIconeCampo,
            unfocusedBorderColor = CorBordaCampo,

            //cor de fundo campo
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,

            //cor do texto digitado
            focusedTextColor = AuthIconeCampo,
            unfocusedTextColor = AuthIconeCampo,

            cursorColor = AuthIconeCampo,

            //cor do label
            focusedLabelColor = AuthIconeCampo,
            unfocusedLabelColor = AuthIconeCampo.copy(alpha = 0.7f)

        ),
        singleLine = true,
        // Usamos o KeyboardOptions.Default e modificamos apenas o keyboardType e imeAction
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation
        // keyboardActions = KeyboardActions(...) // Você pode adicionar isso se precisar lidar com o clique no botão de ação do teclado
    )
}
