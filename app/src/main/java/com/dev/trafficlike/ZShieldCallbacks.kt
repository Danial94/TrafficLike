package com.dev.trafficlike

object ZShieldCallbacks {
    private val zDefendManager = ZDefendManager.shared

    @JvmStatic
    fun IntegrityCallback() {
        zDefendManager.auditLogs.add("ZShieldCallbacks - IntegrityCallback()")
    }

    @JvmStatic
    fun DebuggerCallback() {
        zDefendManager.auditLogs.add("ZShieldCallbacks - DebuggerCallback()")
    }

    @JvmStatic
    fun RootingCallback() {
        zDefendManager.auditLogs.add("ZShieldCallbacks - RootingCallback()")
    }
}