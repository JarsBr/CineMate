package com.cinemate.app.data.models

data class Suggestion(
    var id: String = "",
    val suggestionText: String = "",
    val timestamp: Long = System.currentTimeMillis()
)