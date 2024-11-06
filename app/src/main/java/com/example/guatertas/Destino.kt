package com.example.guatertas

data class Destino(
    val nombre: String,
    val descripcion: String,
    val imagenResId: Int,
    val latitud: Double,
    val longitud: Double,
    val comoLlegar: String = "",   // Valor por defecto vacío
    val queLlevar: String = "",    // Valor por defecto vacío
    val queEsperar: String = ""

)