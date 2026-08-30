package br.com.fiap.EcoWatts.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.fiap.EcoWatts.ui.theme.EcoWatssTheme

/**
 * Brush animado de "shimmer" (luz passando). Reutilizável em qualquer skeleton.
 */
@Composable
fun rememberShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.1f),
        Color.White.copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.1f)
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 500f, 0f),
        end = Offset(translateAnim, 0f)
    )
}

/**
 * Bloco retangular (ou qualquer shape) de skeleton. Peça base — qualquer
 * outro skeleton específico de tela pode ser montado combinando isso.
 *
 * @param width largura fixa; se null, ocupa a largura disponível (fillMaxWidth)
 * @param height altura fixa
 * @param shape formato do bloco (retângulo arredondado, círculo, etc.)
 */
@Composable
fun SkeletonBox(
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp = 16.dp,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    val brush = rememberShimmerBrush()

    val sizedModifier = if (width != null) {
        modifier.size(width = width, height = height)
    } else {
        modifier.height(height)
    }

    Column(
        modifier = sizedModifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
            .background(brush)
    ) {}
}

/**
 * Círculo de skeleton — para avatares, ícones, etc.
 */
@Composable
fun SkeletonCircle(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    SkeletonBox(
        modifier = modifier,
        width = size,
        height = size,
        shape = CircleShape
    )
}

/**
 * Linha de texto de skeleton — atalho para SkeletonBox com proporções de texto.
 */
@Composable
fun SkeletonTextLine(
    modifier: Modifier = Modifier,
    width: Dp = 120.dp,
    height: Dp = 14.dp
) {
    SkeletonBox(
        modifier = modifier,
        width = width,
        height = height,
        shape = RoundedCornerShape(4.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun SkeletonPreview() {
    EcoWatssTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SkeletonCircle()
                Column {
                    SkeletonTextLine(width = 100.dp, height = 20.dp)
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(6.dp))
                    SkeletonTextLine(width = 160.dp)
                }
            }
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
            SkeletonBox(height = 90.dp, shape = RoundedCornerShape(16.dp))
        }
    }
}