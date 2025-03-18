package org.example.vag.developer.options.helper.toolwindows

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import java.awt.FlowLayout
import javax.swing.JPanel

class PluginToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentPanel = JPanel(VerticalFlowLayout(FlowLayout.LEFT))

        contentPanel.add(DeviceSelectionPanel.createPanel(project))
        contentPanel.add(LayoutBoundsPanel.createPanel())
        contentPanel.add(DarkModePanel.createPanel())
        contentPanel.add(SystemFontScalePanel.createPanel())
        contentPanel.add(FigsyPanel.createPanel())

        val scrollPanel = JBScrollPane(contentPanel)
        scrollPanel.verticalScrollBarPolicy = JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        scrollPanel.horizontalScrollBarPolicy = JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED

        val contentFactory = ContentFactory.getInstance()
        val windowContent = contentFactory.createContent(scrollPanel, "", false)
        toolWindow.contentManager.addContent(windowContent)
    }
}