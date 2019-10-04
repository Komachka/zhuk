package com.evo.evozhuk.chooseTime

data class TimeButton(
    val text: String,
    val milisec: Long,
    var isSelected: Boolean = false,
    val navigation: (() -> Unit)? = null
)