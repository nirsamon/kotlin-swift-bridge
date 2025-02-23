package com.kotlinswiftbridge

import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.example.controller.CommandManager
import com.example.common.event.Event
import com.example.common.event.EventListener
import com.example.common.event.EventManager
import com.kotlinswiftbridge.util.BridgeConstants
import com.kotlinswiftbridge.util.Utils
import com.kotlinswiftbridge.util.Utils.getStringRes
import com.kotlinswiftbridge.util.toInputJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class KotlinSwiftBridgeModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private val events = hashMapOf<String, Event>()

    private val eventListener = object : EventListener<Event> {
        override fun handle(event: Event) { 
            handleEvent(event)
        }
    }

    init {
        Utils.init(reactContext)
    }

    override fun getName() = getStringRes(R.string.module_name)
    
    override fun getConstants(): MutableMap<String, Any> = hashMapOf(
        getStringRes(R.string.event_on_ble_scan) to getStringRes(R.string.string_on_ble_scan)
    )

    /**
     * Registers [Event] events
     *
     * @param promise async value to be caught the by React Native side
     */
    @ReactMethod
    fun registerEvents(promise: Promise) {
        try {
            EventManager.INSTANCE.register(eventListener)
            promise.resolve("$PROMISE_SUCCESS ${object {}.javaClass.enclosingMethod?.name}")
        } catch (e: Throwable) {
            promise.reject(e)
        }
    }

    /**
     * Unregisters [Event] events
     *
     * @param promise async value to be caught the by React Native side
     */
    @ReactMethod
    fun unregisterEvents(promise: Promise) {
        try {
            EventManager.INSTANCE.remove(eventListener)
            promise.resolve("$PROMISE_SUCCESS ${object {}.javaClass.enclosingMethod?.name}")
        } catch (e: Throwable) {
            promise.reject(e)
        }
    }

    /**
     * Callback response after receiving an event in React Native side
     *
     * @param params any values to be used by event callback
     * @param promise async value to be caught the by React Native side
     */
    @ReactMethod
    fun eventCallback(params: String, promise: Promise) {
        val obj = JSONObject(params)
        val eventName: String = obj.optString("eventName")
        try {
            events[eventName]?.callback?.onCallback(JSONObject(params))
            promise.resolve("$PROMISE_SUCCESS ${object {}.javaClass.enclosingMethod?.name}")
        } catch (e: Throwable) {
            promise.reject(e)
        }
    }

    /**
     * Executes a command from the libraries
     *
     * @param params any parameters needed to execute a command
     * @param promise async value to be caught the by React Native side
     */
    @ReactMethod
    fun run(params: String, promise: Promise) {
        CoroutineScope(Dispatchers.IO).launch {
            val inputJson = params.toInputJson()
            try {
                val response = CommandManager().apply {
                    setCommand(inputJson.className)
                    setContext(currentActivity ?: reactApplicationContext)
                    inputJson.params?.let { setParameters(it) }
                }.execute()
                val returnObject = JSONObject().apply {
                    put(BridgeConstants.KEY_IS_SUCCESS, response?.isSuccess())
                    put(BridgeConstants.KEY_ERROR_MESSAGE, response?.getErrorMessage())
                    put(BridgeConstants.KEY_ERROR_CODE, response?.getErrorCode())
                    put(BridgeConstants.KEY_RESPONSE, response?.getResponse())
                }
                promise.resolve(returnObject.toString())
            } catch (e: Throwable) {
                promise.reject(e)
            }
        }
    }

    /**
     * Send an event to React Native
     *
     * @param reactContext context from React Native
     * @param eventName name of the event
     * @param params additional parameters for the event
     */
    private fun sendEvent(reactContext: ReactContext, eventName: String, params: WritableMap?) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }

    /**
     * Handle emitted events
     *
     * @param event any event that was emitted
     */
    private fun handleEvent(event: Event) {
        events[event.name] = event
        Log.d(javaClass.name, "$STRING_NAME: ${event.name}")
        Log.d(javaClass.name, "$STRING_DATA: ${event.data}")
        val params: WritableMap = Arguments.createMap().apply {
            putString(STRING_DATA, event.data.toString())
        }
        sendEvent(reactApplicationContext, event.name, params)
    }

    companion object {
        const val NAME = "KotlinSwiftBridge"
        const val PROMISE_SUCCESS: String = "success"
        const val STRING_DATA: String = "data"
        const val STRING_NAME: String = "name"
    }
}