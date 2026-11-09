package com.example.codeleapp.screens
import com.example.codeleapp.R
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codeleapp.* // Importamos colores y modales

@Composable
fun NeonHomeScreen(onStartGame: () -> Unit) {
    var showHelpModal by remember { mutableStateOf(false) }

    // Animación de respiración para el brillo neón
    val infiniteTransition = rememberInfiniteTransition(label = "neonGlow")
    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 10.dp.value,
        targetValue = 25.dp.value,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "glowIntensity"
    )

    Surface(modifier = Modifier.fillMaxSize(), color = BackgroundDark) {
        Column(
            modifier = Modifier.fillMaxSize().statusBarsPadding().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título con sombra neón fuerte
            Text(
                text = "CODELE",
                color = NeonCyan,
                fontSize = 56.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.shadow(glowIntensity.dp, spotColor = NeonCyan)
            )

            Text(
                text = "La palabra del día",
                color = TextGray,
                fontSize = 14.sp,
                letterSpacing = 4.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(50.dp))

// Fila de logos animados con tus propios iconos
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Logo de Python (Brillo Amarillo/Azul)
                AnimatedProgrammingLogo(R.drawable.python, NeonYellow, 0)

                // Logo de Java (Brillo Púrpura/Naranja)
                AnimatedProgrammingLogo(R.drawable.java, NeonPurple, 200)

                // Logo de HTML (Brillo Rojo/Naranja)
                AnimatedProgrammingLogo(R.drawable.html, NeonRed, 400)

                // Logo de C (Brillo Cian/Azul)
                AnimatedProgrammingLogo(R.drawable.c, NeonCyan, 600)
            }

            Spacer(modifier = Modifier.height(80.dp))

            // --- BOTÓN JUGAR (DISEÑO PREMIUM NEÓN) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(80.dp)
                    .shadow(glowIntensity.dp, spotColor = NeonCyan, shape = RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(ButtonBlue, Color(0xFF1E3A5F), ButtonBlue)
                        )
                    )
                    .border(2.dp, Brush.linearGradient(listOf(NeonCyan, Color.Transparent, NeonCyan)), RoundedCornerShape(20.dp))
                    .clickable { onStartGame() },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .border(1.5.dp, NeonYellow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_media_play),
                            contentDescription = null,
                            tint = NeonYellow,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "INICIAR RETO",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- BOTÓN CÓMO JUGAR (ESTILO CRISTAL NEÓN) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(60.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.03f)) // Efecto cristal
                    .border(
                        width = 1.5.dp,
                        brush = Brush.horizontalGradient(listOf(NeonPurple, Color.Transparent, NeonPurple)),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { showHelpModal = true },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "GUÍA DE JUEGO",
                    color = NeonPurple,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
    if (showHelpModal) HowToPlayModal(onDismiss = { showHelpModal = false })
}