package com.dev.trafficlike

import androidx.annotation.Keep

class ZShieldCallbacks {
    companion object {
        private val zDefendManager = ZDefendManager.shared

        @JvmStatic
        @Keep
        fun defaultCallback() {
            zDefendManager.auditLogs.add("ZShieldCallbacks - defaultCallback()")
        }

        @JvmStatic
        @Keep
        fun debugCallback() {
            zDefendManager.auditLogs.add("ZShieldCallbacks - debugCallback()")
        }
    }
}