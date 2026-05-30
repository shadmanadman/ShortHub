package org.kmp.playground.shorthub.shared.observation

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.logging.Level
import java.util.logging.Logger

class JvmInputObserver : InputObserver, NativeKeyListener {
    private val _keyEvents = MutableSharedFlow<KeyEvent>(extraBufferCapacity = 64)
    override val keyEvents = _keyEvents.asSharedFlow()

    init {
        // Disable JNativeHook logging
        val logger = Logger.getLogger(GlobalScreen::class.java.getPackage().name)
        logger.level = Level.OFF
        logger.useParentHandlers = false
    }

    override fun start() {
        try {
            GlobalScreen.registerNativeHook()
            GlobalScreen.addNativeKeyListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun stop() {
        try {
            GlobalScreen.removeNativeKeyListener(this)
            GlobalScreen.unregisterNativeHook()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun nativeKeyPressed(e: NativeKeyEvent) {
        emitEvent(e, true)
    }

    override fun nativeKeyReleased(e: NativeKeyEvent) {
        emitEvent(e, false)
    }

    override fun nativeKeyTyped(e: NativeKeyEvent) {
        // Not used
    }

    private fun emitEvent(e: NativeKeyEvent, isPressed: Boolean) {
        val mods = e.modifiers
        val event = KeyEvent(
            key = NativeKeyEvent.getKeyText(e.keyCode),
            isPressed = isPressed,
            ctrlPressed = (mods and NativeKeyEvent.CTRL_L_MASK) != 0 || (mods and NativeKeyEvent.CTRL_R_MASK) != 0,
            altPressed = (mods and NativeKeyEvent.ALT_L_MASK) != 0 || (mods and NativeKeyEvent.ALT_R_MASK) != 0,
            shiftPressed = (mods and NativeKeyEvent.SHIFT_L_MASK) != 0 || (mods and NativeKeyEvent.SHIFT_R_MASK) != 0,
            metaPressed = (mods and NativeKeyEvent.META_L_MASK) != 0 || (mods and NativeKeyEvent.META_R_MASK) != 0,
        )
        _keyEvents.tryEmit(event)
    }
}

actual fun createInputObserver(): InputObserver = JvmInputObserver()
