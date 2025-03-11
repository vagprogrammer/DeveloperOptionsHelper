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

        contentPanel.add(LayoutBoundsPanel.addLayoutBoundsPanel(project))
        contentPanel.add(DarkModePanel.addDarkModePanel(project))
        contentPanel.add(SystemFontScalePanel.addSystemFontScalePanel(project))
        contentPanel.add(FigsyPanel.addFigsyPanel())

        val scrollPanel = JBScrollPane(contentPanel)
        scrollPanel.verticalScrollBarPolicy = JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        scrollPanel.horizontalScrollBarPolicy = JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED

        val contentFactory = ContentFactory.getInstance()
        val windowContent = contentFactory.createContent(scrollPanel, "", false)
        toolWindow.contentManager.addContent(windowContent)
    }
}