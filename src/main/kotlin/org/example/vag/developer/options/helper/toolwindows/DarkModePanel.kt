package org.example.vag.developer.options.helper.toolwindows

import com.intellij.openapi.project.Project
import org.example.vag.developer.options.helper.extensions.setDarkMode
import org.jetbrains.android.sdk.AndroidSdkUtils
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

object DarkModePanel {

    fun addDarkModePanel(project: Project): JPanel {
        val darkModePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        darkModePanel.add(JLabel("Dark mode"))

        val onButton = JButton("on")
        onButton.addActionListener { onButtonClick(project, true) }
        darkModePanel.add(onButton)

        val offButton = JButton("off")
        offButton.addActionListener { onButtonClick(project, false) }
        darkModePanel.add(offButton)

        return darkModePanel
    }

    private fun onButtonClick(project: Project, show: Boolean) {
        val devices = AndroidSdkUtils.getDebugBridge(project)?.devices

        if (!devices.isNullOrEmpty()) {
            devices.forEach { device ->
                try {
                    device.setDarkMode(show)
                } catch (_: Exception) {
                }
            }
        }
    }
}