package br.com.fiap.EcoWatts.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.fiap.EcoWatts.ui.theme.EcoWatssTheme

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun TopEndCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(160.dp)
            .height(85.dp),
        shape = RoundedCornerShape(bottomStart = 40.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) { }
}

@Preview
@Composable
private fun TopEndCardPreview() {
    EcoWatssTheme {
        TopEndCard() // Corrigido para chamar o componente correto
    }
}

@Composable
fun BottomStartCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(160.dp)
            .height(85.dp),
        shape = RoundedCornerShape(topEnd = 40.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) { }
}

@Preview
@Composable
private fun BottomStartCardPreview() {
    EcoWatssTheme {
        BottomStartCard() // Corrigido para chamar o componente correto
    }
}