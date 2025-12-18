package main.ui

import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JPanel
import javax.swing.JSlider
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
        optionsPanel.presetSelector.addActionListener(this)
        optionsPanel.waveTypeSelector.addActionListener(this)
        optionsPanel.rayCountSlider.addChangeListener(this)
        for (slider in optionsPanel.velSlidersToLabels.keys) {
            slider.addChangeListener(this)
        }
    }

    override fun actionPerformed(e: ActionEvent?) {
        if (e?.source == optionsPanel.presetSelector) {
            val selectedPreset = optionsPanel.presetSelector.selectedItem as String
            // Handle the preset change in simPanel
            simPanel.loadPreset(selectedPreset)
            optionsPanel.updateVisibility()
        }
        if (e?.source == optionsPanel.waveTypeSelector) {
            val sWavesEnabled = optionsPanel.waveTypeSelector.isSelected
            // Handle the wave type change in simPanel
            simPanel.updateWaveType(sWavesEnabled)
        }
    }

    override fun stateChanged(e: ChangeEvent?) {
        if (e == null) return

        val slider = e.source as JSlider
        if (slider == optionsPanel.rayCountSlider) {
            val rayCount = optionsPanel.rayCountSlider.value
            // Handle the ray count change in simPanel
            simPanel.updateRayCount(rayCount)
        }
        if (slider in optionsPanel.velSlidersToLabels.keys) {
            // Handle velocity slider changes
            optionsPanel.updateVelLabel(slider)
            val keys = optionsPanel.velSlidersToLabels.keys.toList()
            val pInitialVels = listOf(
                keys[0].value.toFloat() / 10f,
                keys[4].value.toFloat() / 10f,
                keys[8].value.toFloat() / 10f,
                keys[12].value.toFloat() / 10f
            )
            val pFinalVels = listOf(
                keys[1].value.toFloat() / 10f,
                keys[5].value.toFloat() / 10f,
                keys[9].value.toFloat() / 10f,
                keys[13].value.toFloat() / 10f
            )
            val sInitialVels = listOf(
                keys[2].value.toFloat() / 10f,
                keys[6].value.toFloat() / 10f,
                keys[10].value.toFloat() / 10f,
                keys[14].value.toFloat() / 10f
            )
            val sFinalVels = listOf(
                keys[3].value.toFloat() / 10f,
                keys[7].value.toFloat() / 10f,
                keys[11].value.toFloat() / 10f,
                keys[15].value.toFloat() / 10f
            )
            simPanel.updateVelocities(pInitialVels, pFinalVels, sInitialVels, sFinalVels)
        }
    }
}