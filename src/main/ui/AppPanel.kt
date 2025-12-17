package main.ui

import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JPanel
import javax.swing.JSplitPane
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

class AppPanel : JPanel(), ChangeListener, ActionListener {
    private val simPanel = SimPanel()
    private val optionsPanel = OptionsPanel()
    val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, simPanel, optionsPanel)

    init {
        splitPane.dividerLocation = SimPanel.WIDTH

        simPanel.minimumSize = Dimension(SimPanel.WIDTH - 300, SimPanel.HEIGHT)
        optionsPanel.minimumSize = Dimension(300, SimPanel.HEIGHT)
        optionsPanel.rayCountSlider.addChangeListener(this)
        optionsPanel.presetSelector.addActionListener(this)
    }

    override fun actionPerformed(e: ActionEvent?) {
        if (e?.source == optionsPanel.presetSelector) {
            val selectedPreset = optionsPanel.presetSelector.selectedItem as String
            // Handle the preset change in simPanel
            simPanel.loadPreset(selectedPreset)
        }
    }

    override fun stateChanged(e: ChangeEvent?) {
        if (e?.source == optionsPanel.rayCountSlider) {
            val rayCount = optionsPanel.rayCountSlider.value
            // Handle the ray count change in simPanel
            simPanel.updateRayCount(rayCount)
        }
    }
}