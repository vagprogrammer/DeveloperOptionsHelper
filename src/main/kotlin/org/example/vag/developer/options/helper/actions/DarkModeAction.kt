package org.example.vag.developer.options.helper.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.example.vag.developer.options.helper.SingleLineReceiver
import org.example.vag.developer.options.helper.extensions.*

class DarkModeAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        // get list of connected devices using ADB
        val devices = project?.getNotEmptyDevices()
        // toggle "Show layout bounds" on each connected device
        devices?.forEach { device ->
            device.getDarkMode(SingleLineReceiver { firstLine ->
                if (firstLine.isNotEmpty()) {
                    val enable = (firstLine == "Night mode: yes").not()
                    device.setDarkMode(enable)
                    project.showNotification("DarkMode is: " + if (enable) "turned on" else "turned off")
                }
            })
        } ?: run {
            project?.showNotification("No device connected")
        }
    }
}