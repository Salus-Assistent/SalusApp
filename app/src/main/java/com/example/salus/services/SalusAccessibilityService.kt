package com.example.salus.services

import android.accessibilityservice.AccessibilityService
import android.os.SystemClock
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import com.example.salus.domain.AlertManager

class SalusAccessibilityService : AccessibilityService() {

    private var pressCount = 0
    private var firstPressTime: Long = 0

    override fun onKeyEvent(event: KeyEvent?): Boolean {

        if (event?.action != KeyEvent.ACTION_DOWN) {
            return super.onKeyEvent(event)
        }

        if (event.keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            val currentTime = SystemClock.uptimeMillis()

            if (pressCount == 0) {
                firstPressTime = currentTime
                pressCount = 1
            } else {
                if (currentTime - firstPressTime < PRESS_TIMEOUT) {
                    pressCount++
                } else {
                    firstPressTime = currentTime
                    pressCount = 1
                }
            }

            if (pressCount == 3) {
                sendEmergencySms()

                pressCount = 0
                firstPressTime = 0
            }

            return true
        }

        return super.onKeyEvent(event)
    }

    private fun sendEmergencySms() {
        AlertManager.sendEmergencySms(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    companion object {
        private const val PRESS_TIMEOUT = 2000
    }
}