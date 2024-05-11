package com.chopas.weatherapp

import java.util.Locale

/**
 * String Extension to capitalize each word in String
 */
fun String.capitalizeWords(): String {
    return split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }
}