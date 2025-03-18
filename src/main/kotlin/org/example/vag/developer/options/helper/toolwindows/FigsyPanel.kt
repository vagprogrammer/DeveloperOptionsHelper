package org.example.vag.developer.options.helper.toolwindows

import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingUtilities
import org.example.vag.developer.options.helper.extensions.encode
import org.example.vag.developer.options.helper.extensions.runInAdb

object FigsyPanel {

    private const val ADB_COMMAND = "am start -W -a android.intent.action.VIEW -d"

    // Map to store the text entered per device (keyed by device serial).
    private val deviceToFigsyText = mutableMapOf<String, String>()

    // We'll hold a reference to the text field so that we can update it when the selected device changes.
    private var figsyTextField: JTextField? = null

    // The panel for Figsy commands.
    private val panel: JPanel = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
        // Initially, the panel is hidden until a device is available.
        isVisible = false
    }

    fun createPanel(): JPanel {
        // Create and add a static label.
        panel.add(JLabel("Figsy"))

        // Create the text field.
        val textField = JTextField(20)
        figsyTextField = textField
        panel.add(textField)

        // Add a document listener to save the text per device.
        textField.document.addDocumentListener(object : javax.swing.event.DocumentListener {
            override fun insertUpdate(e: javax.swing.event.DocumentEvent?) { updateMap() }
            override fun removeUpdate(e: javax.swing.event.DocumentEvent?) { updateMap() }
            override fun changedUpdate(e: javax.swing.event.DocumentEvent?) { updateMap() }
            private fun updateMap() {
                val device = DeviceSelectionPanel.getDevice()
                if (device != null) {
                    deviceToFigsyText[device.serialNumber] = textField.text
                }
            }
        })

        // Create two buttons.
        val buttonOn = JButton("Set config on and launch BOE")
        val buttonOff = JButton("Set config off and launch BOE")
        panel.add(buttonOn)
        panel.add(buttonOff)

        // Set their actions.
        buttonOn.addActionListener { onButtonClick(textField.text, flagValue = true) }
        buttonOff.addActionListener { onButtonClick(textField.text, flagValue = false) }

        // Initially update the text field for the selected device.
        updateState()

        return panel
    }

    /**
     * Updates the text field with the stored value for the selected device.
     * Also, shows or hides the panel based on device availability.
     */
    private fun updateTextFieldForSelectedDevice(textField: JTextField) {
        val device = DeviceSelectionPanel.getDevice()
        if (device != null) {
            val saved = deviceToFigsyText[device.serialNumber]
            SwingUtilities.invokeLater { textField.text = saved ?: "" }
        }
    }

    /**
     * Should be called when the selected device changes (via DeviceSelectionPanel)
     * to update the text field with the stored value for the new device and to show/hide the panel.
     */
    fun updateState() {
        val device = DeviceSelectionPanel.getDevice()
        if (device == null) {
            SwingUtilities.invokeLater { panel.isVisible = false }
        } else {
            SwingUtilities.invokeLater { panel.isVisible = true }
            figsyTextField?.let { updateTextFieldForSelectedDevice(it) }
        }
    }

    /**
     * Sends two commands to the selected device:
     * 1. Kills the app: "adb shell am kill com.etsy.android.beta"
     * 2. Launches a deeplink command to set configuration and launch BOE.
     *
     * The commands are targeted to the selected device using its serial number.
     */
    private fun onButtonClick(flagName: String, flagValue: Boolean) {
        val device = DeviceSelectionPanel.getDevice() ?: return
        val serial = device.serialNumber
        // Save the current text for this device.
        deviceToFigsyText[serial] = flagName

        val seconds = System.currentTimeMillis() / 1000
        val dataString = "/$seconds?$flagName=$flagValue"
        val signature = dataString.encode()
        val deeplink = "etsy://cfg/$signature$dataString"

        // Build the kill command for the specific device.
        val killCommand = listOf("adb", "-s", serial, "shell", "am", "kill", "com.etsy.android.beta")
        // Build the BOE command.
        val boeCommand = listOf("adb", "-s", serial, "shell", "$ADB_COMMAND $deeplink")

        // Run the kill command first.
        killCommand.runInAdb()
        // Wait briefly.
        try { Thread.sleep(500) } catch (e: InterruptedException) { /* ignore */ }
        // Then run the BOE command.
        boeCommand.runInAdb()
    }
}
