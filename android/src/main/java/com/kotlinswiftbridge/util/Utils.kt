package com.kotlinswiftbridge.util

import androidx.annotation.StringRes
import com.facebook.react.bridge.ReactApplicationContext

object Utils {

    private lateinit var context: ReactApplicationContext

    /**
     * Initializes the context
     *
     * @param context context to work on
     */
    fun init(context: ReactApplicationContext) {
        this.context = context
    }

    /**
     * Gets a string resource
     *
     * @param id identifier of the string resource
     * @return string resource
     */
    fun getStringRes(@StringRes id: Int): String = context.resources.getString(id)
}