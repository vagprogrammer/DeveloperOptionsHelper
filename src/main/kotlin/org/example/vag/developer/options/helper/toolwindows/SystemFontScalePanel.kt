package org.example.vag.developer.options.helper.toolwindows

import com.intellij.openapi.project.Project
import org.example.vag.developer.options.helper.extensions.setSystemFontScale
import org.jetbrains.android.sdk.AndroidSdkUtils
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

object SystemFontScalePanel {

    fun addSystemFontScalePanel(project: Project): JPanel {
        val systemFontScalePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        systemFontScalePanel.add(JLabel("Font scale"))

        val fButton = JButton("0.8")
        fButton.addActionListener { onButtonClick(project, "0.8") }
        systemFontScalePanel.add(fButton)

        val sButton = JButton("1.0")
        sButton.addActionListener { onButtonClick(project, "1.0") }
        systemFontScalePanel.add(sButton)

        val tButton = JButton("1.2")
        tButton.addActionListener { onButtonClick(project, "1.2") }
        systemFontScalePanel.add(tButton)

        val foButton = JButton("1.4")
        foButton.addActionListener { onButtonClick(project, "1.4") }
        systemFontScalePanel.add(foButton)

        val fiButton = JButton("1.6")
        fiButton.addActionListener { onButtonClick(project, "1.6") }
        systemFontScalePanel.add(fiButton)

        val textField = JTextField()
        systemFontScalePanel.add(textField)
        val customButton = JButton("Set custom")
        customButton.addActionListener { onButtonClick(project, textField.text) }
        systemFontScalePanel.add(customButton)

        return systemFontScalePanel
    }

    private fun onButtonClick(project: Project, fontScale: String) {
        val devices = AndroidSdkUtils.getDebugBridge(project)?.devices

        if (!devices.isNullOrEmpty()) {
            devices.forEach { device ->
                try {
                    device.setSystemFontScale(fontScale)
                } catch (_: Exception) {
                }
            }
        }
    }
}