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

    // Cache slider array to avoid repeated allocations
    private val sliderArray = optionsPanel.velSliders

    init {
        splitPane.dividerLocation = SimPanel.WIDTH

        simPanel.minimumSize = Dimension(SimPanel.WIDTH - 300, SimPanel.HEIGHT)
        optionsPanel.minimumSize = Dimension(300, SimPanel.HEIGHT)
        optionsPanel.presetSelector.addActionListener(this)
        optionsPanel.waveTypeSelector.addActionListener(this)
        optionsPanel.rayCountSlider.addChangeListener(this)
        for (slider in sliderArray) {
            slider.addChangeListener(this)
        }
    }

    override fun actionPerformed(e: ActionEvent?) {
        when (e?.source) {
            optionsPanel.presetSelector -> {
                val selectedPreset = optionsPanel.presetSelector.selectedItem as String
                simPanel.loadPreset(selectedPreset)
                optionsPanel.updateVisibility()
            }
            optionsPanel.waveTypeSelector -> {
                val sWavesEnabled = optionsPanel.waveTypeSelector.isSelected
                simPanel.updateWaveType(sWavesEnabled)
            }
        }
    }

    override fun stateChanged(e: ChangeEvent?) {
        if (e == null) return

        val slider = e.source as JSlider
        when (slider) {
            optionsPanel.rayCountSlider -> {
                simPanel.updateRayCount(slider.value)
            }
            in sliderArray -> {
                optionsPanel.updateVelLabel(slider)
                // Direct array indexing instead of map operations
                val pInitialVels = floatArrayOf(
                    sliderArray[0].value / 10f,
                    sliderArray[4].value / 10f,
                    sliderArray[8].value / 10f,
                    sliderArray[12].value / 10f
                )
                val pFinalVels = floatArrayOf(
                    sliderArray[1].value / 10f,
                    sliderArray[5].value / 10f,
                    sliderArray[9].value / 10f,
                    sliderArray[13].value / 10f
                )
                val sInitialVels = floatArrayOf(
                    sliderArray[2].value / 10f,
                    sliderArray[6].value / 10f,
                    sliderArray[10].value / 10f,
                    sliderArray[14].value / 10f
                )
                val sFinalVels = floatArrayOf(
                    sliderArray[3].value / 10f,
                    sliderArray[7].value / 10f,
                    sliderArray[11].value / 10f,
                    sliderArray[15].value / 10f
                )
                simPanel.updateVelocities(pInitialVels, pFinalVels, sInitialVels, sFinalVels)
            }
        }
    }
}
