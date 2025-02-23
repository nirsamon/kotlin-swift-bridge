package com.kotlinswiftbridge.model

/**
 * Custom class converted from a JSON in String format
 *
 * @property className class name of the command
 * @property params any parameters needed for the command
 */
data class InputJson(
    val className: String,
    var params: Any? = null
)
