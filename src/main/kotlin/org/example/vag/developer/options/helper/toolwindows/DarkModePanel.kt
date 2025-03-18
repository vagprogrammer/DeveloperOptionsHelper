package org.example.vag.developer.options.helper.toolwindows

import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.ui.JBUI
import org.example.vag.developer.options.helper.SingleLineReceiver
import org.example.vag.developer.options.helper.extensions.getDarkMode
import org.example.vag.developer.options.helper.extensions.setDarkMode
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities
import java.awt.FlowLayout

object DarkModePanel {

    // The panel that will be added to the tool window.
    private val panel: JPanel = JPanel(FlowLayout(FlowLayout.LEFT))

    // A static label and the toggle button.
    private val themeLabel = JLabel("Dark Theme")
    private val toggleButton = JButton("Loading...").apply { isVisible = false }

    init {
        // Build the UI.
        panel.add(themeLabel)
        panel.add(toggleButton)

        // When the toggle button is clicked, toggle dark mode on the selected device.
        toggleButton.addActionListener {
            val device = DeviceSelectionPanel.getDevice()
            if (device != null) {
                val enable = toggleButton.text.contains("Enable", ignoreCase = true)
                device.setDarkMode(enable)
                updateState() // Refresh the UI after toggling.
            }
        }
    }

    /**
     * Creates and returns the DarkModePanel. This method forces an initial update so that if a device
     * is already selected from the DeviceSelectionPanel, the toggle button is updated immediately.
     */
    fun createPanel(): JPanel {
        updateState()
        return panel
    }

    /**
     * Queries the currently selected device (via DeviceSelectionPanel.getDevice()) and updates
     * the toggle button's text based on the device's dark mode state.
     * If no device is selected, the panel is hidden.
     *
     * This method should be called whenever the selected device changes (for example, when the user
     * selects a different device in DeviceSelectionPanel or when that panel's refresh button is pressed).
     */
    fun updateState() {
        val device = DeviceSelectionPanel.getDevice()
        if (device == null) {
            SwingUtilities.invokeLater { panel.isVisible = false }
        } else {
            SwingUtilities.invokeLater { panel.isVisible = true }
            // Query the device's dark mode state using the extension function.
            ApplicationManager.getApplication().executeOnPooledThread {
                try {
                    device.getDarkMode(SingleLineReceiver { line ->
                        val trimmed = line.trim()
                        if (trimmed.isEmpty()) return@SingleLineReceiver
                        // We assume output like "Night mode: yes" or "Night mode: no".
                        val darkModeEnabled = trimmed.startsWith("night mode: yes", ignoreCase = true)
                        SwingUtilities.invokeLater {
                            toggleButton.text = if (darkModeEnabled) "Disable Dark Mode" else "Enable Dark Mode"
                            toggleButton.isVisible = true
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    SwingUtilities.invokeLater {
                        toggleButton.text = "Error"
                        toggleButton.isVisible = true
                    }
                }
            }
        }
    }
}
