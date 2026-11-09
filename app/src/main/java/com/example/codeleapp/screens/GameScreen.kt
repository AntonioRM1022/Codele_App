package com.example.codeleapp.screens

import android.app.Activity
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codeleapp.*
import com.example.codeleapp.data.CodeleRepository
import kotlinx.coroutines.delay
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GameScreen(onBackToHome: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("codele_prefs", Context.MODE_PRIVATE) }
    val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

    // --- PERSISTENCIA Y LÓGICA DE INICIO ---
    val savedDay = prefs.getInt("saved_day", -1)
    if (savedDay != today) {
        val newKey = CodeleRepository.getRandomLanguage()
        val newConcept = CodeleRepository.getRandomWordFor(newKey)
        prefs.edit()
            .putInt("saved_day", today)
            .putString("daily_key", newKey)
            .putString("daily_word", newConcept.word.uppercase().trim())
            .putString("daily_hint", newConcept.hint)
            .putBoolean("is_game_over", false)
            .putBoolean("user_won", false)
            .apply()
    }

    val randomKey = prefs.getString("daily_key", "") ?: ""
    val targetWord = prefs.getString("daily_word", "") ?: ""
    val hintText = prefs.getString("daily_hint", "") ?: ""

    var isGameOver by remember { mutableStateOf(prefs.getBoolean("is_game_over", false)) }
    var won by remember { mutableStateOf(prefs.getBoolean("user_won", false)) }
    var currentRow by remember { mutableIntStateOf(0) }
    var showHint by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    val history = remember { mutableStateListOf<Pair<String, List<Color>>>() }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    AnimatedContent(
        targetState = isGameOver,
        transitionSpec = {
            (fadeIn(animationSpec = tween(600)) + slideInVertically(initialOffsetY = { it / 2 }))
                .togetherWith(fadeOut(animationSpec = tween(400)))
        }, label = "GameResultTransition"
    ) { targetIsGameOver ->
        if (targetIsGameOver) {
            ResultScreen(
                won = won,
                history = history,
                targetWord = targetWord,
                onCloseApp = { (context as? Activity)?.finishAffinity() }
            )
        } else {
            Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {

                // Botón Regresar
                IconButton(
                    onClick = { onBackToHome() },
                    modifier = Modifier.statusBarsPadding().padding(16.dp).size(45.dp)
                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                        .border(1.dp, NeonPurple.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(painterResource(id = android.R.drawable.ic_menu_revert), "Regresar", tint = NeonPurple)
                }

                Column(
                    modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().imePadding().padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("El día de hoy:", color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(modifier = Modifier.size(120.dp, 45.dp).background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(8.dp)).border(2.dp, NeonYellow, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                        Text(randomKey, color = NeonYellow, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // GRID DINÁMICO
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        repeat(5) { rowIndex ->
                            val pastAttempt = history.getOrNull(rowIndex)
                            val isCurrentRow = rowIndex == currentRow

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth().clickable(enabled = isCurrentRow) {
                                    focusRequester.requestFocus()
                                    keyboardController?.show()
                                }
                            ) {
                                // Aquí usamos el largo de la palabra objetivo para las celdas
                                repeat(targetWord.length) { charIndex ->
                                    val char = when {
                                        pastAttempt != null -> pastAttempt.first[charIndex].toString()
                                        isCurrentRow && charIndex < inputText.length -> inputText[charIndex].toString()
                                        else -> ""
                                    }

                                    val scale by animateFloatAsState(
                                        targetValue = if (isCurrentRow && char.isNotEmpty()) 1.1f else 1f,
                                        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
                                    )

                                    val rotationY by animateFloatAsState(
                                        targetValue = if (pastAttempt != null) 180f else 0f,
                                        animationSpec = tween(600, delayMillis = charIndex * 100)
                                    )

                                    val bgColor = if (pastAttempt != null) pastAttempt.second[charIndex] else Color.Black.copy(alpha = 0.3f)

                                    Box(
                                        modifier = Modifier.padding(horizontal = 4.dp).size(55.dp)
                                            .graphicsLayer { this.rotationY = rotationY; this.scaleX = scale; this.scaleY = scale; cameraDistance = 12f * density }
                                            .clip(RoundedCornerShape(10.dp)).background(bgColor)
                                            .border(1.5.dp, if (isCurrentRow && charIndex == inputText.length) NeonCyan else Color.White.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = char, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black,
                                            modifier = Modifier.graphicsLayer { if (rotationY > 90f) this.rotationY = 180f }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Invisible TextField corregido
                    TextField(
                        value = inputText,
                        onValueChange = {
                            if (it.length <= targetWord.length) {
                                inputText = it.uppercase().filter { c -> c.isLetter() }
                            }
                        },
                        modifier = Modifier.size(1.dp).focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters, autoCorrect = false),
                        colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, cursorColor = Color.Transparent)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Botón de Pista
                    IconButton(onClick = { showHint = true }, modifier = Modifier.size(60.dp).background(Color.Black.copy(alpha = 0.5f), CircleShape).border(1.dp, NeonCyan, CircleShape)) {
                        Icon(painterResource(id = android.R.drawable.ic_menu_help), null, tint = NeonCyan)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // BOTÓN ENVIAR CORREGIDO
                    val isReady = inputText.length == targetWord.length
                    Box(
                        modifier = Modifier.fillMaxWidth(0.85f).height(65.dp).clip(RoundedCornerShape(18.dp))
                            .background(if (isReady) ButtonBlue else Color.White.copy(alpha = 0.05f))
                            .clickable(enabled = isReady) {
                                val colors = checkWord(inputText, targetWord)
                                history.add(inputText to colors)

                                val hasWon = (inputText == targetWord)
                                if (hasWon || currentRow >= 4) {
                                    won = hasWon
                                    isGameOver = true
                                    prefs.edit().putBoolean("is_game_over", true).putBoolean("user_won", hasWon).apply()
                                } else {
                                    currentRow++
                                    inputText = ""
                                }
                                keyboardController?.hide()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(painterResource(id = android.R.drawable.ic_media_play), null, tint = if(isReady) NeonYellow else Color.DarkGray)
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
    if (showHint) { HintModal(hintText = hintText) { showHint = false } }
}

@Composable
fun ResultScreen(won: Boolean, history: List<Pair<String, List<Color>>>, targetWord: String, onCloseApp: () -> Unit) {
    var timeLeft by remember { mutableStateOf(calculateTimeUntil6AM()) }
    LaunchedEffect(Unit) { while (true) { delay(1000); timeLeft = calculateTimeUntil6AM() } }

    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (won) "¡¡EXCELENTE!!" else "GAME OVER",
            color = if (won) NeonCyan else NeonRed,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        Text("La palabra era: $targetWord", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(vertical = 16.dp))

        // GRID DE RESULTADOS DINÁMICO
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(history.size) { rowIndex ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val rowData = history[rowIndex]
                    repeat(targetWord.length) { charIndex ->
                        val color = rowData.second.getOrNull(charIndex) ?: Color.DarkGray
                        Box(modifier = Modifier.size(35.dp).clip(RoundedCornerShape(4.dp)).background(color))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        Text("Próximo reto en:", color = TextGray, fontSize = 14.sp)
        Text(timeLeft, color = NeonYellow, fontSize = 38.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = onCloseApp,
            modifier = Modifier.fillMaxWidth(0.7f).height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("SALIR", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

// --- FUNCIONES AUXILIARES ---
fun checkWord(guess: String, target: String): List<Color> {
    val result = MutableList(target.length) { NeonRed }
    val targetChars = target.toMutableList()
    val guessChars = guess.toMutableList()

    // Primera pasada: Verdes (Correctos)
    for (i in target.indices) {
        if (guessChars[i] == targetChars[i]) {
            result[i] = ButtonBlue
            targetChars[i] = ' '
            guessChars[i] = '_'
        }
    }

    // Segunda pasada: Amarillos (Lugar equivocado)
    for (i in target.indices) {
        if (guessChars[i] != '_' && targetChars.contains(guessChars[i])) {
            result[i] = NeonYellow
            targetChars[targetChars.indexOf(guessChars[i])] = ' '
        }
    }
    return result
}

fun calculateTimeUntil6AM(): String {
    val now = Calendar.getInstance()
    val target = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 6); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0)
        if (before(now)) add(Calendar.DATE, 1)
    }
    val diff = target.timeInMillis - now.timeInMillis
    val h = diff / 3600000; val m = (diff / 60000) % 60; val s = (diff / 1000) % 60
    return String.format("%02d:%02d:%02d", h, m, s)
}