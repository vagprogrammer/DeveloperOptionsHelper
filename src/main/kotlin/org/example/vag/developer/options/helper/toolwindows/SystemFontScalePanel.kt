package org.example.vag.developer.options.helper.toolwindows

import com.intellij.openapi.application.ApplicationManager
import org.example.vag.developer.options.helper.SingleLineReceiver
import org.example.vag.developer.options.helper.extensions.getSystemFontScale
import org.example.vag.developer.options.helper.extensions.setSystemFontScale
import java.awt.FlowLayout
import java.awt.Color
import javax.swing.*

object SystemFontScalePanel {

    // Preset font scale constants.
    private const val FONT_SCALE_08 = "0.8"
    private const val FONT_SCALE_10 = "1.0"
    private const val FONT_SCALE_12 = "1.2"
    private const val FONT_SCALE_14 = "1.4"
    private const val FONT_SCALE_16 = "1.6"

    // The panel that will be displayed.
    private val panel: JPanel = JPanel(FlowLayout(FlowLayout.LEFT))

    // Static label.
    private val titleLabel = JLabel("Font Scale")

    // Preset buttons.
    private val btn08 = JButton(FONT_SCALE_08)
    private val btn10 = JButton(FONT_SCALE_10)
    private val btn12 = JButton(FONT_SCALE_12)
    private val btn14 = JButton(FONT_SCALE_14)
    private val btn16 = JButton(FONT_SCALE_16)

    // Custom scale: text field and custom button.
    private val customField = JTextField(5)
    private val customButton = JButton("Custom")

    init {
        // Build the UI.
        panel.add(titleLabel)
        panel.add(btn08)
        panel.add(btn10)
        panel.add(btn12)
        panel.add(btn14)
        panel.add(btn16)
        panel.add(customField)
        panel.add(customButton)

        // When a preset button is pressed, update the device.
        btn08.addActionListener { setFontScale(FONT_SCALE_08) }
        btn10.addActionListener { setFontScale(FONT_SCALE_10) }
        btn12.addActionListener { setFontScale(FONT_SCALE_12) }
        btn14.addActionListener { setFontScale(FONT_SCALE_14) }
        btn16.addActionListener { setFontScale(FONT_SCALE_16) }

        // When the custom button is pressed, update the device using the value in the text field.
        customButton.addActionListener { setFontScale(customField.text) }
    }

    /**
     * Sets the system font scale on the selected device using the extension function.
     */
    private fun setFontScale(scale: String) {
        val device = DeviceSelectionPanel.getDevice()
        if (device != null) {
            try {
                device.setSystemFontScale(scale)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            updateState() // Refresh UI after setting.
        }
    }

    /**
     * Creates and returns the SystemFontScalePanel.
     */
    fun createPanel(): JPanel {
        updateState() // Initial update.
        return panel
    }

    /**
     * Queries the selected device (via DeviceSelectionPanel.getDevice()) for its system font scale,
     * and then updates the UI:
     *
     * - If the returned value matches one of the preset values, that preset button is marked (via background color),
     *   and the custom text field is cleared and its text color reverts to default.
     * - Otherwise, the custom text field is updated with the returned value and visually marked as selected
     *   (its background is changed and its text color is set to black).
     *
     * If no device is selected, the panel is hidden.
     *
     * This method should be called whenever the selected device changes or when the DeviceSelectionPanel refreshes.
     */
    fun updateState() {
        val device = DeviceSelectionPanel.getDevice()
        SwingUtilities.invokeLater { panel.isVisible = (device != null) }
        if (device != null) {
            ApplicationManager.getApplication().executeOnPooledThread {
                try {
                    device.getSystemFontScale(SingleLineReceiver { line ->
                        val trimmed = line.trim()
                        if (trimmed.isEmpty()) return@SingleLineReceiver
                        println("VAG- DEBUG: System font scale received: '$trimmed'")
                        // Define the list of preset values.
                        val presets = listOf(FONT_SCALE_08, FONT_SCALE_10, FONT_SCALE_12, FONT_SCALE_14, FONT_SCALE_16)
                        SwingUtilities.invokeLater {
                            // Clear visual selection for all preset buttons and the custom field.
                            setButtonSelected(btn08, false)
                            setButtonSelected(btn10, false)
                            setButtonSelected(btn12, false)
                            setButtonSelected(btn14, false)
                            setButtonSelected(btn16, false)
                            setTextFieldSelected(customField, false)
                            // If the received value is one of the presets, mark that button and clear custom field.
                            if (trimmed in presets) {
                                when (trimmed) {
                                    FONT_SCALE_08 -> setButtonSelected(btn08, true)
                                    FONT_SCALE_10 -> setButtonSelected(btn10, true)
                                    FONT_SCALE_12 -> setButtonSelected(btn12, true)
                                    FONT_SCALE_14 -> setButtonSelected(btn14, true)
                                    FONT_SCALE_16 -> setButtonSelected(btn16, true)
                                }
                                customField.text = ""
                            } else {
                                // Otherwise, update the custom field and mark it as selected.
                                customField.text = trimmed
                                setTextFieldSelected(customField, true)
                            }
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    SwingUtilities.invokeLater {
                        // In case of error, clear all selections.
                        setButtonSelected(btn08, false)
                        setButtonSelected(btn10, false)
                        setButtonSelected(btn12, false)
                        setButtonSelected(btn14, false)
                        setButtonSelected(btn16, false)
                        setTextFieldSelected(customField, false)
                    }
                }
            }
        }
    }

    /**
     * Visually marks or unmarks a button as selected by changing its background color.
     * Adjust the colors as needed.
     */
    private fun setButtonSelected(button: JButton, selected: Boolean) {
        if (selected) {
            // For example, use a light blue background when selected.
            button.background = Color(173, 216, 230)
        } else {
            button.background = null
        }
    }

    /**
     * Visually marks or unmarks a text field as selected.
     * When selected, set the background to light blue and the text color to black.
     * When unselected, reset both to default.
     */
    private fun setTextFieldSelected(field: JTextField, selected: Boolean) {
        if (selected) {
            field.background = Color(173, 216, 230)
            field.foreground = Color.BLACK
        } else {
            field.background = null
            field.foreground = UIManager.getColor("TextField.foreground")
        }
    }
}
