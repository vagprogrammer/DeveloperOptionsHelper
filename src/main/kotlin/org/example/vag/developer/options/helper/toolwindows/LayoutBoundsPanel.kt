package org.example.vag.developer.options.helper.toolwindows

import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.ui.JBUI
import org.example.vag.developer.options.helper.SingleLineReceiver
import org.example.vag.developer.options.helper.extensions.getLayoutBounds
import org.example.vag.developer.options.helper.extensions.setLayoutBounds
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities
import java.awt.FlowLayout
import javax.swing.Timer

object LayoutBoundsPanel {

    // The panel that will be displayed.
    private val panel: JPanel = JPanel(FlowLayout(FlowLayout.LEFT))

    // A static label and the toggle button.
    private val titleLabel = JLabel("Layout Bounds")
    private val toggleButton = JButton("Loading...").apply { isVisible = false }

    init {
        panel.add(titleLabel)
        panel.add(toggleButton)

        // When the toggle button is clicked, toggle layout bounds on the selected device.
        toggleButton.addActionListener {
            val device = DeviceSelectionPanel.getDevice()
            if (device != null) {
                // If the button text contains "Enable", then we want to enable layout bounds.
                val enable = toggleButton.text.contains("Enable", ignoreCase = true)
                device.setLayoutBounds(enable)
                updateState()
            }
        }
    }

    /**
     * Returns the LayoutBoundsPanel as a JPanel.
     */
    fun createPanel(): JPanel {
        updateState() // Force an initial update.
        return panel
    }

    /**
     * Queries the selected device (from DeviceSelectionPanel.getDevice()) and updates
     * the toggle button's text based on the device's layout bounds state.
     * If no device is selected, the panel is hidden.
     *
     * This method should be called whenever the selected device changes or when the refresh button is pressed.
     */
    fun updateState() {
        val device = DeviceSelectionPanel.getDevice()
        if (device == null) {
            SwingUtilities.invokeLater { panel.isVisible = false }
        } else {
            SwingUtilities.invokeLater { panel.isVisible = true }
            // Query the device's layout bounds state using the extension function.
            ApplicationManager.getApplication().executeOnPooledThread {
                try {
                    device.getLayoutBounds(SingleLineReceiver { line ->
                        val trimmed = line.trim()
                        if (trimmed.isEmpty()) return@SingleLineReceiver
                        // If the output equals "true" then layout bounds are enabled.
                        val boundsEnabled = trimmed.equals("true", ignoreCase = true)
                        SwingUtilities.invokeLater {
                            toggleButton.text = if (boundsEnabled) "Disable Layout Bounds" else "Enable Layout Bounds"
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
