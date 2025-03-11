package org.example.vag.developer.options.helper.toolwindows

import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import org.example.vag.developer.options.helper.extensions.encode
import org.example.vag.developer.options.helper.extensions.runInAdb

object FigsyPanel {

    private const val ADB_COMMAND = "am start -W -a android.intent.action.VIEW -d"

    fun addFigsyPanel(): JPanel {
        val figsyPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        figsyPanel.add(JLabel("Figsy"))

        val textField = JTextField()
        figsyPanel.add(textField)

        val customButton = JButton("Set config on and launch BOE")
        customButton.addActionListener { onButtonClick(textField.text, flagValue = true) }
        figsyPanel.add(customButton)

        val customButton2 = JButton("Set config off and launch BOE")
        customButton2.addActionListener { onButtonClick(textField.text, flagValue = false) }
        figsyPanel.add(customButton2)

        return figsyPanel
    }

    private fun onButtonClick(flagName: String, flagValue: Boolean) {
        val seconds = System.currentTimeMillis() / 1000
        val dataString = "/$seconds?$flagName=$flagValue"
        val signature = dataString.encode()
        val deeplink = "etsy://cfg/$signature$dataString"

        val command = listOf("adb", "shell", "$ADB_COMMAND $deeplink")
        command.runInAdb()
    }
}