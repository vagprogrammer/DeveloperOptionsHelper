package org.example.vag.developer.options.helper.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.example.vag.developer.options.helper.extensions.getLayoutBounds
import org.example.vag.developer.options.helper.extensions.getNotEmptyDevices
import org.example.vag.developer.options.helper.extensions.setLayoutBounds
import org.example.vag.developer.options.helper.extensions.showNotification
import org.example.vag.developer.options.helper.SingleLineReceiver

class ShowLayoutBoundsAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        // get list of connected devices using ADB
        val devices = project?.getNotEmptyDevices()
        // toggle "Show layout bounds" on each connected device
        devices?.forEach { device ->
            device.getLayoutBounds(SingleLineReceiver { firstLine ->
                if (firstLine.isNotEmpty()) {
                    val enable = firstLine.toBoolean().not()
                    device.setLayoutBounds(enable)
                    project.showNotification("Layout bounds is: " + if (enable) "turned on" else "turned off")
                }
            })
        } ?: run {
            project?.showNotification("No device connected")
        }
    }
}