# 🎮 Codele App

<div align="center">

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Material 3](https://img.shields.io/badge/Design-Material%203-795548?style=for-the-badge)

**Un juego diario de adivinanza de palabras estilo *Wordle*, adaptado para desarrolladores y entusiastas del código.**

[Características](#-características) • [Mecánica del Juego](#-mecánica-del-juego) • [Arquitectura y Tecnologías](#-arquitectura-y-tecnologías) • [Instalación y Uso](#-instalación-y-ejecución) • [Estructura del Proyecto](#-estructura-del-proyecto)

</div>

---

## 📌 Descripción

**Codele** es una aplicación móvil nativa para Android diseñada con **Jetpack Compose**. Inspirada en la mecánica clásica de *Wordle*, retará tus conocimientos diarios sobre programación, lenguajes, comandos y conceptos fundamentales de desarrollo de software (Python, HTML5, RPA, Linux, etc.).

Cuenta con una interfaz moderna y atractiva con estilo **Dark Cyberpunk / Neón**, transiciones fluidas, efectos 3D en las celdas, persistencia del reto diario y un temporizador que anuncia la llegada del próximo reto.

---

## ✨ Características

- 📅 **Reto Diario Único:** Cada día se desbloquea una nueva palabra y pista técnica calculada a partir del día del año.
- 💡 **Sistema de Pistas Contextuales:** Modal de consejos que ofrece una definición técnica para orientar al jugador sin desvelar la respuesta.
- 🎨 **Estética Neón Cyberpunk:**
  - Modo oscuro nativo (`#0D1117`).
  - Animaciones de flotación y brillo neón en logotipos de tecnologías (Python, Java, HTML, C).
  - Efectos visuales de desenfoque (*blur*), bordes resplandecientes y sombras volumétricas.
- 🎬 **Animaciones Fluidas:**
  - *Splash Screen* interactivo con animación de salida con escala y rotación anticipada (`AnticipateInterpolator`).
  - Animación de volteo 3D (*flip card*) en las letras de cada intento con perspectiva `cameraDistance`.
- 💾 **Persistencia de Partida:** Guarda el estado del juego y los resultados diarios utilizando `SharedPreferences`.
- ⏳ **Temporizador al Próximo Reto:** Cálculo dinámico en tiempo real que descuenta los segundos exactos hasta las 6:00 AM para el siguiente acertijo.

---

## 🕹️ Mecánica del Juego

1. **Objetivo:** Adivinar la palabra técnica oculta en un máximo de **5 intentos**.
2. **Longitud dinámica:** Dependiendo del término seleccionado, las palabras varían en número de caracteres.
3. **Retroalimentación por colores:**
   - 🟦 **Azul / Cian:** La letra es correcta y se encuentra en la posición exacta.
   - 🟨 **Amarillo Neón:** La letra forma parte de la palabra, pero está en otra posición.
   - 🟥 **Rojo Neón:** La letra no pertenece a la palabra secreta.
4. **Categorías del juego:**
   - 🐍 **PYTHON:** `CLASS`, `WHILE`, `TUPLE`, `YIELD`, `DECOR`...
   - 🌐 **HTML5:** `ARRAY`, `INPUT`, `TABLE`, `ASIDE`, `CANVAS`...
   - 🤖 **RPA:** `BOTS`, `FLOWS`, `ORCH`, `NODES`, `SCRAP`...
   - 🐧 **LINUX:** `CHMOD`, `SUDO`, `SHELL`, `KRNEL`, `ROOTS`...

---

## 🛠️ Arquitectura y Tecnologías

El proyecto sigue las directrices modernas de desarrollo Android recomendadas por Google:

- **Lenguaje:** [Kotlin 2.0+](https://kotlinlang.org/)
- **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) con **Material Design 3**.
- **Splash Screen API:** `androidx.core:core-splashscreen` con animaciones personalizadas de salida mediante `ObjectAnimator` y `AnimatorSet`.
- **Gráficos y Animaciones Compose:**
  - `rememberInfiniteTransition` para bucles continuos de respiración lumínica y flotación.
  - `animateFloatAsState` con efectos `spring` y `tween` para transformaciones 3D.
  - `graphicsLayer` para rotaciones espaciales en el eje Y y control de perspectiva.
- **Manejo de Estado y Ciclo de Vida:**
  - `State`, `mutableStateOf`, `remember`, `LaunchedEffect`.
- **Target SDK:** Android 15 (API 35 / compileSdk 36)
- **Min SDK:** Android 7.0 (API 24)

---

## 📂 Estructura del Proyecto

```text
CodeleApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/codeleapp/
│   │   │   │   ├── MainActivity.kt        # Actividad principal, splash y orquestación
│   │   │   │   ├── data/
│   │   │   │   │   └── CodeleRepository.kt # Diccionario de palabras, categorías y pistas
│   │   │   │   ├── screens/
│   │   │   │   │   ├── HomeScreen.kt      # Pantalla inicial con animaciones de bienvenida
│   │   │   │   │   └── GameScreen.kt      # Tablero interactivo, lógica y pantalla de resultado
│   │   │   │   └── ui/theme/              # Paleta de colores neón, tipografías y tema Compose
│   │   │   └── res/                       # Recursos gráficos (iconos, drawables, splash)
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts                   # Dependencias y configuración de la app
├── gradle/                                # Gradle Wrapper y Version Catalogs
├── build.gradle.kts                       # Configuración de compilación raíz
└── settings.gradle.kts
```

---

## 🚀 Instalación y Ejecución

### Prerrequisitos
- **Android Studio** (versión Ladybug o superior recomendada).
- **JDK 11** o superior.
- Dispositivo físico o emulador con **Android 7.0 (API 24)** o superior.

### Pasos para compilar
1. Clona este repositorio:
   ```bash
   git clone https://github.com/AntonioRM1022/Codele_App.git
   cd Codele_App
   ```
2. Abre el proyecto en Android Studio.
3. Deja que Gradle sincronice las dependencias del proyecto.
4. Conecta un dispositivo físico con depuración USB habilitada o inicia un emulador.
5. Haz clic en **Run 'app'** (`Shift + F10`) o compila desde la terminal:
   ```bash
   # En Windows PowerShell / CMD
   .\gradlew assembleDebug

   # En macOS / Linux
   ./gradlew assembleDebug
   ```

---

## 👨‍💻 Autor

Desarrollado por [AntonioRM1022](https://github.com/AntonioRM1022).
