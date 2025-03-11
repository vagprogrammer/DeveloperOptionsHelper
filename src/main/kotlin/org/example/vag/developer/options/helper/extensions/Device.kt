package org.example.vag.developer.options.helper.extensions

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import org.example.vag.developer.options.helper.SingleLineReceiver

fun IDevice.getDarkMode(outputReceiver: SingleLineReceiver) {
    executeShellCommand("cmd uimode night", outputReceiver)
}

fun IDevice.setDarkMode(enable: Boolean) {
    val uiMode = if (enable) "yes" else "no"
    executeShellCommand("cmd uimode night $uiMode", NullOutputReceiver())
}

fun IDevice.getLayoutBounds(outputReceiver: SingleLineReceiver) {
    executeShellCommand("getprop debug.layout", outputReceiver)
}

fun IDevice.setLayoutBounds(enable: Boolean) {
    executeShellCommand(
        "setprop debug.layout $enable ; service call activity 1599295570",
        NullOutputReceiver()
    )
}


fun IDevice.setSystemFontScale(scale: String) {
    executeShellCommand("settings put system font_scale $scale", NullOutputReceiver())
}