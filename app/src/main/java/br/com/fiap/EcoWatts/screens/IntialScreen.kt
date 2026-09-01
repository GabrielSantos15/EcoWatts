package br.com.fiap.EcoWatts.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.EcoWatts.R
import br.com.fiap.EcoWatts.components.BottomStartCard
import br.com.fiap.EcoWatts.components.TopEndCard
import br.com.fiap.EcoWatts.navigation.Destination
import br.com.fiap.EcoWatts.ui.theme.EcoWatssTheme
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun InitialScreen(navController: NavController) {

    val isDarkTheme = isSystemInDarkTheme()

    val backgroundGradient = if (isDarkTheme) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF3646A6),
                Color(0xFF2E207A)
            )
        )
    } else {

        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF81B2FE),
                Color(0xFF5378FD)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
//            .background(color  = MaterialTheme.colorScheme.background)
    ) {
        TopEndCard(modifier = Modifier.align(Alignment.TopEnd))
        BottomStartCard(modifier = Modifier.align(Alignment.BottomStart))

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .systemBarsPadding(),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HouseAnimate()
            Spacer(modifier = Modifier.height(60.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = stringResource(R.string.smart_management),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.displayMedium,
                )
                Text(
                    text = stringResource(R.string.desc_smart_management),
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodyLarge,
                )

                Spacer(modifier = Modifier.height(60.dp))

                Button(
                    onClick = {
                        navController.navigate(Destination.LoginScreen.route)
                    },
                    colors = ButtonDefaults
                        .buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary
                        ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_login),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        navController.navigate(Destination.SignupScreen.route)
                    },
                    colors = ButtonDefaults
                        .buttonColors(
                            containerColor = Color.Transparent
                        ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        width = 1.dp, color = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_create_account),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

            }
        }

    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
fun InitialScreenPreview() {
    EcoWatssTheme() {
        InitialScreen(rememberNavController())
    }
}

@Composable
fun HouseAnimate() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.house_animation))

    // Configura para rodar a animação em loop infinito
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Preview
@Composable
private fun HouseAnimatePreview() {

}