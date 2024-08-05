package com.dev.trafficlike

import android.util.Log
import androidx.annotation.Keep

class ZShieldCallbacks {
    companion object {
        @JvmStatic
        @Keep
        fun debugCallback() {
            Log.i("TechTitan", "debugCallback() called")
        }
    }
}