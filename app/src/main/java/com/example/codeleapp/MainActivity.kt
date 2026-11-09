package com.example.codeleapp
import androidx.compose.animation.*
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.codeleapp.ui.theme.CodeleAppTheme
import com.example.codeleapp.screens.NeonHomeScreen
import com.example.codeleapp.screens.GameScreen
import java.util.Calendar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.os.Handler
import android.os.Looper
import androidx.compose.animation.core.tween
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AnticipateInterpolator


// --- 1. CONFIGURACIÓN GLOBAL (Colores y Modelos) ---
val BackgroundDark = Color(0xFF0D1117)
val NeonCyan = Color(0xFF00FBFF)
val NeonYellow = Color(0xFFFFD54F)
val NeonPurple = Color(0xFF8B9DFF)
val NeonRed = Color(0xFFFF5252)
val TextGray = Color(0xFF8B949E)
val ButtonBlue = Color(0xFF3774A6)

// Modelo de datos para las palabras
data class WordConcept(val word: String, val hint: String)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Instalar el Splash
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        // Mantener el splash 1.5 segundos para que se vea el logo
        var keepSplashScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        Handler(Looper.getMainLooper()).postDelayed({
            keepSplashScreen = false
        }, 1500)

        // =================================================###########
        // 2. ANIMACIÓN AVANZADA DE SALIDA DEL LOGO
        // =================================================###########
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            // El icono real dentro de la vista del splash
            val iconView = splashScreenView.iconView

            // A) Animación de Escala (Se hace grande y luego pequeño)
            val scaleX = ObjectAnimator.ofFloat(iconView, View.SCALE_X, 1f, 1.2f, 0f)
            val scaleY = ObjectAnimator.ofFloat(iconView, View.SCALE_Y, 1f, 1.2f, 0f)

            // B) Animación de Rotación (Gira un poco)
            val rotation = ObjectAnimator.ofFloat(iconView, View.ROTATION, 0f, -15f, 90f)

            // C) Animación de Transparencia (Fade Out)
            val fadeOut = ObjectAnimator.ofFloat(splashScreenView.view, View.ALPHA, 1f, 0f)

            // Juntamos todas las animaciones
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(scaleX, scaleY, rotation, fadeOut)

            // Configuraciones de la animación
            animatorSet.duration = 600L
            // Interpolador "Anticipate": hace un pequeño movimiento hacia atrás antes de salir
            animatorSet.interpolator = AnticipateInterpolator()

            // Cuando termine, eliminamos el Splash
            animatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    splashScreenView.remove()
                }
            })

            // ¡Arrancamos la animación!
            animatorSet.start()
        }
        // =================================================###########

        setContent {
            CodeleAppTheme {
                val prefs = getSharedPreferences("codele_prefs", MODE_PRIVATE)
                val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                val lastPlayedDay = prefs.getInt("saved_day", -1)
                val isGameOver = prefs.getBoolean("is_game_over", false)

                var currentScreen by remember {
                    mutableStateOf(if (lastPlayedDay == today && isGameOver) "game" else "home")
                }

                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(500)) + scaleIn(initialScale = 0.9f))
                            .togetherWith(fadeOut(animationSpec = tween(500)))
                    }, label = "ScreenTransition"
                ) { screen ->
                    when (screen) {
                        "home" -> NeonHomeScreen(onStartGame = { currentScreen = "game" })
                        "game" -> GameScreen(onBackToHome = { currentScreen = "home" })
                    }
                }
            }
        }
    }
}
@Composable
fun AnimatedProgrammingLogo(resId: Int, glowColor: Color, delayMillis: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val translationY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, delayMillis = delayMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "yAnimation"
    )

    Box(
        modifier = Modifier.size(55.dp).graphicsLayer { this.translationY = translationY },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .blur(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) 15.dp else 0.dp)
                .background(glowColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .shadow(8.dp, spotColor = glowColor, shape = RoundedCornerShape(10.dp))
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                .border(1.5.dp, glowColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(id = resId), contentDescription = null, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun HintModal(hintText: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(BackgroundDark, RoundedCornerShape(24.dp))
                .border(2.dp, NeonCyan.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("CONSEJO:", color = NeonYellow, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = hintText, color = Color.White, textAlign = TextAlign.Center, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("¡ENTENDIDO!", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun HowToPlayModal(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f) // Un poquito más ancho para que quepa bien el texto
                .clip(RoundedCornerShape(24.dp))
                .background(BackgroundDark)
                .border(2.dp, NeonPurple.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("REGLAS DEL JUEGO", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(20.dp))

                // Reglas de Colores
                RuleItem(ButtonBlue, "La letra es correcta y está en su lugar.")
                Spacer(modifier = Modifier.height(12.dp))
                RuleItem(NeonYellow, "La letra está en la palabra pero en otro lugar.")
                Spacer(modifier = Modifier.height(12.dp))
                RuleItem(NeonRed, "La letra no existe en esta palabra.")

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // --- NUEVA REGLA: PALABRAS CORTAS ---
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        // El mismo puntito que pusimos en el GameScreen
                        Box(modifier = Modifier.size(4.dp).background(Color.White.copy(alpha = 0.3f), CircleShape))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Las palabras varían de 2 a 5 letras. Los cuadros con un punto están bloqueados.",
                        color = TextGray,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("¡ENTENDIDO!", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RuleItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color.copy(alpha = 0.2f))
                .border(2.dp, color, RoundedCornerShape(8.dp))
                .shadow(10.dp, spotColor = color)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, color = Color.White, fontSize = 14.sp)
    }
}