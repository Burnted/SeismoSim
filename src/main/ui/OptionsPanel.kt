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

    val layerLabels = listOf(JLabel("Layer 0:"), JLabel("Layer 1:"), JLabel("Layer 2:"), JLabel("Layer 3:"))


    val velSlidersToLabels = mapOf(
                                    JSlider(JSlider.HORIZONTAL, 0, 150, 120) to JLabel("P-Wave v_i:"),
                                    JSlider(JSlider.HORIZONTAL, 0, 150, 120) to JLabel("P-Wave v_f:"),

                                    JSlider(JSlider.HORIZONTAL, 0, 100, 50) to JLabel("S-Wave v_i:"),
                                    JSlider(JSlider.HORIZONTAL, 0, 100, 50) to JLabel("S-Wave v_f:"),

                                    JSlider(JSlider.HORIZONTAL, 0, 150, 150) to JLabel("P-Wave v_i:"),
                                    JSlider(JSlider.HORIZONTAL, 0, 150, 150) to JLabel("P-Wave v_f:"),

                                    JSlider(JSlider.HORIZONTAL, 0, 100, 50) to JLabel("S-Wave v_i:"),
                                    JSlider(JSlider.HORIZONTAL, 0, 100, 50) to JLabel("S-Wave v_f:"),

                                    JSlider(JSlider.HORIZONTAL, 0, 150, 90) to JLabel("P-Wave v_i:"),
                                    JSlider(JSlider.HORIZONTAL, 0, 150, 90) to JLabel("P-Wave v_f:"),

                                    JSlider(JSlider.HORIZONTAL, 0, 100, 50) to JLabel("S-Wave v_i:"),
                                    JSlider(JSlider.HORIZONTAL, 0, 100, 50) to JLabel("S-Wave v_f:"),

                                    JSlider(JSlider.HORIZONTAL, 0, 150, 120) to JLabel("P-Wave v_i:"),
                                    JSlider(JSlider.HORIZONTAL, 0, 150, 120) to JLabel("P-Wave v_f:"),

                                    JSlider(JSlider.HORIZONTAL, 0, 100, 50) to JLabel("S-Wave v_i:"),
                                    JSlider(JSlider.HORIZONTAL, 0, 100, 50)to JLabel("S-Wave v_f:"))

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
        c.gridwidth = 2
        add(rayCountLabel, c)

        c.gridx = 0
        c.gridy = 2
        c.ipadx = 250
        c.gridwidth = 2
        add(rayCountSlider, c)

        initVelLabels(c)
        updateVisibility()

        this.background = Color(220, 220, 220)
    }

    private fun initVelLabels(c: GridBagConstraints) {
        var row = 3
        var layer = 0
        for ((slider, label) in velSlidersToLabels) {
            c.ipadx = 0
            if (row in listOf(3, 8, 13, 18)){
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
            add(label, c)


            c.gridx = 1
            c.gridy = row
            c.gridwidth = 1
            c.ipadx = 120
            add(slider, c)
            row++
            updateVelLabel(slider)
        }
    }

    fun updateVisibility(){
        for (label in layerLabels){
            label.isVisible = presetSelector.selectedItem == "custom"
        }

        for ((slider, label) in velSlidersToLabels){
            slider.isVisible = presetSelector.selectedItem == "custom"
            label.isVisible = presetSelector.selectedItem == "custom"
        }
    }

    fun updateVelLabel(slider: JSlider){
        val label = velSlidersToLabels[slider] ?: return
        val value: Float = slider.value / 10f
        val text = label.text.split(":")[0]
        label.text = "$text: $value km/s"
    }
}