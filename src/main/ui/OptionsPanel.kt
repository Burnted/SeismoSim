package main.ui

import java.awt.*
import javax.swing.*

class OptionsPanel : JPanel() {
    private val presetSelectorLabel = JLabel("Select Preset:").apply {
        alignmentX = LEFT_ALIGNMENT
        font = font.deriveFont(16f)
    }

    val presetSelector = JComboBox(arrayOf("earth", "mars", "moon", "custom")).apply {
        maximumSize = Dimension(100, 30)
        alignmentX = LEFT_ALIGNMENT
        selectedItem = 0
    }

    private val waveTypeLabel = JLabel("Wave Type:").apply {
        font = font.deriveFont(16f)
        alignmentX = LEFT_ALIGNMENT
    }

    val waveTypeSelector = JCheckBox("S-Waves Enabled").apply {
        maximumSize = Dimension(150, 30)
        alignmentX = LEFT_ALIGNMENT
        isSelected = false
    }

    private val rayCountLabel = JLabel("Ray Count:").apply {
        font = font.deriveFont(16f)
        alignmentX = LEFT_ALIGNMENT
    }

    val rayCountSlider = JSlider(JSlider.HORIZONTAL, 0, 50, 21).apply {
        majorTickSpacing = 10
        minorTickSpacing = 1
        paintTicks = true
        paintLabels = true
        snapToTicks = true
        maximumSize = Dimension(300, 50)
        alignmentX = LEFT_ALIGNMENT
    }

    val layerLabels = arrayOf(JLabel("Layer 0:"), JLabel("Layer 1:"), JLabel("Layer 2:"), JLabel("Layer 3:"))

    val velSliders = arrayOf(
        JSlider(JSlider.HORIZONTAL, 0, 150, 120),
        JSlider(JSlider.HORIZONTAL, 0, 150, 120),
        JSlider(JSlider.HORIZONTAL, 0, 100, 60),
        JSlider(JSlider.HORIZONTAL, 0, 100, 60),
        JSlider(JSlider.HORIZONTAL, 0, 150, 150),
        JSlider(JSlider.HORIZONTAL, 0, 150, 150),
        JSlider(JSlider.HORIZONTAL, 0, 100, 75),
        JSlider(JSlider.HORIZONTAL, 0, 100, 75),
        JSlider(JSlider.HORIZONTAL, 0, 150, 90),
        JSlider(JSlider.HORIZONTAL, 0, 150, 90),
        JSlider(JSlider.HORIZONTAL, 0, 100, 0),
        JSlider(JSlider.HORIZONTAL, 0, 100, 0),
        JSlider(JSlider.HORIZONTAL, 0, 150, 120),
        JSlider(JSlider.HORIZONTAL, 0, 150, 120),
        JSlider(JSlider.HORIZONTAL, 0, 100, 0),
        JSlider(JSlider.HORIZONTAL, 0, 100, 0)
    )

    private val velLabels = arrayOf(
        JLabel("P-Wave v_i:"),
        JLabel("P-Wave v_f:"),
        JLabel("S-Wave v_i:"),
        JLabel("S-Wave v_f:"),
        JLabel("P-Wave v_i:"),
        JLabel("P-Wave v_f:"),
        JLabel("S-Wave v_i:"),
        JLabel("S-Wave v_f:"),
        JLabel("P-Wave v_i:"),
        JLabel("P-Wave v_f:"),
        JLabel("S-Wave v_i:"),
        JLabel("S-Wave v_f:"),
        JLabel("P-Wave v_i:"),
        JLabel("P-Wave v_f:"),
        JLabel("S-Wave v_i:"),
        JLabel("S-Wave v_f:")
    )

    init {
        preferredSize = Dimension(300, 1000)
        layout = GridBagLayout()
        val c = GridBagConstraints()
        c.anchor = GridBagConstraints.FIRST_LINE_START
        c.insets = Insets(5, 5, 5, 0)

        c.gridx = 0
        c.gridy = 0
        add(presetSelectorLabel, c)

        c.gridx = 1
        c.gridy = 0
        add(presetSelector, c)

        c.gridx = 0
        c.gridy = 1
        add(waveTypeLabel, c)

        c.gridx = 1
        c.gridy = 1
        add(waveTypeSelector, c)

        c.gridx = 0
        c.gridy = 2
        c.gridwidth = 2
        add(rayCountLabel, c)

        c.gridx = 0
        c.gridy = 3
        c.ipadx = 250
        c.gridwidth = 2
        add(rayCountSlider, c)

        initVelLabels(c)
        updateVisibility()

        this.background = Color(220, 220, 220)
    }

    private fun initVelLabels(c: GridBagConstraints) {
        var row = 4
        var layer = 0
        for (i in velSliders.indices) {
            c.ipadx = 0
            if (row in intArrayOf(4, 9, 14, 19)) {
                layerLabels[layer].font = layerLabels[layer].font.deriveFont(16f)
                c.gridx = 0
                c.gridy = row
                c.gridwidth = 2
                c.ipadx = 0
                add(layerLabels[layer], c)
                layer++
                row++
            }

            c.gridx = 0
            c.gridy = row
            c.gridwidth = 1
            add(velLabels[i], c)

            c.gridx = 1
            c.gridy = row
            c.gridwidth = 1
            c.ipadx = 120
            add(velSliders[i], c)
            row++
            updateVelLabel(velSliders[i])
        }
    }

    fun updateVisibility() {
        val isCustom = presetSelector.selectedItem == "custom"
        for (label in layerLabels) {
            label.isVisible = isCustom
        }
        for (slider in velSliders) {
            slider.isVisible = isCustom
        }
        for (label in velLabels) {
            label.isVisible = isCustom
        }
    }

    fun updateVelLabel(slider: JSlider) {
        val idx = velSliders.indexOf(slider)
        if (idx < 0) return
        val label = velLabels[idx]
        val value: Float = slider.value / 10f
        val colonIdx = label.text.indexOf(':')
        val text = if (colonIdx > 0) label.text.substring(0, colonIdx) else label.text
        label.text = "$text: $value km/s"
    }
}
