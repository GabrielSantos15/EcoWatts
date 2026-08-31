package br.com.fiap.EcoWatts.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.fiap.EcoWatts.ui.theme.EcoWatssTheme

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
        TopEndCard()
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
        BottomStartCard()
    }
}

@Composable
fun GradientTopBackground(
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 210.dp,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .align(Alignment.TopCenter)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(200f, 200f)
                    ),
                    shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                )
        )
        content()
    }
}

@Preview
@Composable
private fun GradientTopBackgroundPreview() {
    EcoWatssTheme() {
        GradientTopBackground{
            Text( text = "Teste")
        }
    }
}