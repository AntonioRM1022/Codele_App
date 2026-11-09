package com.example.codeleapp.data



import com.example.codeleapp.WordConcept

object CodeleRepository {
    val gameDictionary = mapOf(
        "HTML5" to listOf(
            WordConcept("ARRAY", "Estructura que almacena elementos de forma contigua."),
            WordConcept("INPUT", "Etiqueta para introducir datos en formularios."),
            WordConcept("TABLE", "Organiza datos en filas y columnas."),
            WordConcept("ASIDE", "Contenido tangencial al contenido que lo rodea."),
            WordConcept("CANVAS", "Contenedor para gráficos dibujados mediante scripts.")
        ),
        "PYTHON" to listOf(
            WordConcept("CLASS", "Plantilla para crear objetos con datos y funciones."),
            WordConcept("WHILE", "Bucle que se repite según una condición."),
            WordConcept("TUPLE", "Colección inmutable de elementos."),
            WordConcept("YIELD", "Pausa la ejecución de una función y devuelve un valor."),
            WordConcept("DECOR", "Función que modifica el comportamiento de otra función.")
        ),
        "RPA" to listOf(
            WordConcept("BOTS", "Software que automatiza tareas repetitivas."),
            WordConcept("FLOWS", "Diagrama que define la lógica de la automatización."),
            WordConcept("ORCH", "Plataforma para gestionar y monitorear robots."),
            WordConcept("NODES", "Puntos de conexión en un flujo de trabajo."),
            WordConcept("SCRAP", "Técnica para extraer datos de sitios web.")
        ),
        "LINUX" to listOf(
            WordConcept("CHMOD", "Cambia los permisos de acceso a archivos."),
            WordConcept("SUDO", "Ejecuta comandos con privilegios de superusuario."),
            WordConcept("SHELL", "Interfaz de línea de comandos para el sistema."),
            WordConcept("KRNEL", "Núcleo del sistema operativo que gestiona el hardware."),
            WordConcept("ROOTS", "Usuario con acceso total al sistema operativo.")
        )
    )

    // Función útil para obtener un lenguaje aleatorio
    fun getRandomLanguage(): String = gameDictionary.keys.random()

    // Función para obtener una palabra aleatoria de un lenguaje específico
    fun getRandomWordFor(language: String): WordConcept {
        return gameDictionary[language]?.random() ?: WordConcept("ERROR", "No hay palabras.")
    }
}