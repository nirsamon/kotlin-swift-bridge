package com.kotlinswiftbridge.util

import com.kotlinswiftbridge.model.InputJson
import org.json.JSONObject

/**
 * Converts a JSON in String format into a custom class
 *
 * @return converted String
 */
fun String.toInputJson(): InputJson {

    val jsonObject = JSONObject(this)

    return InputJson(
        className = jsonObject.getString(BridgeConstants.COMMAND_CLASS_NAME)
    ).apply {
        BridgeConstants.COMMAND_PARAMS.let { if (jsonObject.has(it)) params = jsonObject.get(it) }
    }
}
