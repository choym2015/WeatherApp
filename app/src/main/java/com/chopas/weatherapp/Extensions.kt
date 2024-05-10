package com.chopas.weatherapp

import java.util.Locale

fun String.capitalizeWords(): String {
    return split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }
}