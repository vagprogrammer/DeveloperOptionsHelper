package org.example.vag.developer.options.helper.toolwindows

import com.android.ddmlib.IDevice
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.util.ui.JBUI
import org.jetbrains.android.sdk.AndroidSdkUtils
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities

object DeviceSelectionPanel {
    // Holds the currently selected device.
    private var selectedDevice: IDevice? = null

    /**
     * Creates and returns the device selection panel.
     */
    fun createPanel(project: Project): JPanel {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
            border = JBUI.Borders.empty()
        }
        // Add a refresh button with a reload icon to the left.
        val refreshButton = JButton(AllIcons.Actions.Refresh).apply {
            toolTipText = "Refresh Devices"
        }
        panel.add(refreshButton)

        // Add a static "Device:" label.
        panel.add(JLabel("Device: "))

        // These UI components will be added dynamically.
        var deviceComboBox: JComboBox<String>? = null
        var deviceLabel: JLabel? = null

        // Helper function to retrieve the current list of devices.
        fun getDevices(): List<IDevice> =
            AndroidSdkUtils.getDebugBridge(project)?.devices?.toList() ?: emptyList()

        // Function to refresh the device list and update the UI.
        fun refreshDevices() {
            val devices = getDevices()
            // Remove any previously added dynamic component(s).
            deviceComboBox?.let { panel.remove(it) }
            deviceLabel?.let { panel.remove(it) }
            deviceComboBox = null
            deviceLabel = null

            when {
                devices.isEmpty() -> {
                    deviceLabel = JLabel("No devices found")
                    selectedDevice = null
                    panel.add(deviceLabel)
                }

                devices.size == 1 -> {
                    deviceLabel = JLabel(devices[0].name)
                    selectedDevice = devices[0]
                    panel.add(deviceLabel)
                }

                else -> {
                    val names = devices.map { it.name }
                    deviceComboBox = JComboBox(names.toTypedArray()).apply {
                        addActionListener {
                            val idx = selectedIndex
                            selectedDevice = if (idx in devices.indices) devices[idx] else null
                            // When a new device is selected, update the dark mode panel.
                            DarkModePanel.updateState()
                            LayoutBoundsPanel.updateState()
                            SystemFontScalePanel.updateState()
                            FigsyPanel.updateState()
                        }
                    }
                    selectedDevice = devices.firstOrNull()
                    panel.add(deviceComboBox)
                }
            }
            panel.revalidate()
            panel.repaint()
        }

        // Set the refresh button action.
        refreshButton.addActionListener {
            SwingUtilities.invokeLater {
                refreshDevices()
                DarkModePanel.updateState()
                LayoutBoundsPanel.updateState()
                SystemFontScalePanel.updateState()
                FigsyPanel.updateState()
            }
        }

        // Initial refresh.
        refreshDevices()

        return panel
    }

    /**
     * Returns the currently selected device.
     */
    fun getDevice(): IDevice? = selectedDevice
}
