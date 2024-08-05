package com.dev.trafficlike

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateListOf
import com.zimperium.api.v5.ZDefend
import com.zimperium.api.v5.ZDefendThreat
import com.zimperium.api.v5.ZDeviceStatus
import com.zimperium.api.v5.ZDeviceStatusCallback
import com.zimperium.api.v5.ZDeviceStatusRegistration
import com.zimperium.api.v5.ZLoginStatus

@SuppressLint("MutableCollectionMutableState")
class ZDefendManager : ZDeviceStatusCallback {
    val auditLogs = mutableStateListOf<String>()
    val threats = mutableStateListOf<ThreatModel>()

    companion object {
        var shared = ZDefendManager()
    }

    private var lastDeviceStatus: ZDeviceStatus? = null
    private var deviceStatusRegistration: ZDeviceStatusRegistration? = null

    override fun onDeviceStatus(deviceStatus: ZDeviceStatus) {
        val logBuilder = StringBuilder()
        logBuilder.append("OnDeviceStatus: ").append(deviceStatus.loginStatus.name)
        logBuilder.append("\nDatetime: ").append(deviceStatus.statusDate)

        if (deviceStatus.loginStatus == ZLoginStatus.LOGGED_IN) {
            logBuilder.append("\nScan progress %: ").append(deviceStatus.initialScanProgressPercentage)
        } else {
            logBuilder.append("\nLast login error: ").append(deviceStatus.loginLastError.name)
        }

        if (lastDeviceStatus != null) {
            for (threat in deviceStatus.activeNewThreats) {
                addThreat(threat)
            }
            for (threat in deviceStatus.mitigatedNewThreats) {
                addThreat(threat)
            }
        } else {
            for (threat in deviceStatus.allThreats) {
                addThreat(threat)
            }
        }

        lastDeviceStatus = deviceStatus
        auditLogs.add("ZDefendManager - onDeviceStatus: $logBuilder")
    }

    fun initializeZDefendApi() {
        deviceStatusRegistration = ZDefend.addDeviceStatusCallback(this)
        auditLogs.add("ZDefendManager - initializeZDefendApi")
    }

    fun deregisterZDefendApi() {
        deviceStatusRegistration?.deregister()
        auditLogs.add("ZDefendManager - deregisterZDefendApi()")
    }

    fun checkForUpdates() {
        ZDefend.checkForUpdates()
    }

    private fun addThreat(threat: ZDefendThreat) {
        threats.add(ThreatModel(
            threat.uuid,
            threat.localizedName,
            threat.severity.name,
            threat.isMitigated,
            threat.localizedDetails,
            threat.localizedResolution))
    }
}