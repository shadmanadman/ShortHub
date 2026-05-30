package org.kmp.playground.shorthub.shared.observation

object ShortcutMatcher {
    fun matches(event: KeyEvent, shortcutStr: String): Boolean {
        if (!event.isPressed) return false
        
        val parts = shortcutStr.split("+").map { it.trim().lowercase() }
        val key = parts.last()
        val modifiers = parts.dropLast(1)
        
        val eventKey = event.key.lowercase()
        if (eventKey != key) return false
        
        val ctrlNeeded = modifiers.contains("ctrl")
        val altNeeded = modifiers.contains("alt")
        val shiftNeeded = modifiers.contains("shift")
        val metaNeeded = modifiers.contains("meta") || modifiers.contains("cmd") || modifiers.contains("win")
        
        return event.ctrlPressed == ctrlNeeded &&
               event.altPressed == altNeeded &&
               event.shiftPressed == shiftNeeded &&
               event.metaPressed == metaNeeded
    }
}
