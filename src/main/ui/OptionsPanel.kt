package main.ui

import java.awt.*
import javax.swing.*

class OptionsPanel : JPanel() {


    private val presetSelectorLabel = JLabel(" Select Preset:").apply {
        alignmentX = LEFT_ALIGNMENT
        font = font.deriveFont(16f)
    }
    val presetSelector = JComboBox(arrayOf("earth", "mars", "moon", "custom")).apply {
        maximumSize = Dimension(100, 30)
        alignmentX = LEFT_ALIGNMENT
        selectedItem = 0
    }
    private val rayCountLabel = JLabel(" Ray Count:").apply {
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

    val layerLabels = listOf(
        JLabel(" Layer 0:").apply {
            font = font.deriveFont(16f)
            alignmentX = LEFT_ALIGNMENT
        },
        JLabel(" Layer 1:").apply {
            font = font.deriveFont(16f)
            alignmentX = LEFT_ALIGNMENT
        },
        JLabel(" Layer 2:").apply {
            font = font.deriveFont(16f)
            alignmentX = LEFT_ALIGNMENT
        },
        JLabel(" Layer 3:").apply {
            font = font.deriveFont(16f)
            alignmentX = LEFT_ALIGNMENT
        }
    )

    val waveVelocityLabels = listOf(
        JLabel("P-Wave v_i").apply {
            font = font.deriveFont(14f)
            alignmentX = LEFT_ALIGNMENT
        },
        JLabel("P-Wave v_f").apply {
            font = font.deriveFont(14f)
            alignmentX = LEFT_ALIGNMENT
        },
        JLabel("S-Wave v_i").apply {
            font = font.deriveFont(14f)
            alignmentX = LEFT_ALIGNMENT
        },
        JLabel("S-Wave v_f").apply {
            font = font.deriveFont(14f)
            alignmentX = LEFT_ALIGNMENT
        }
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
        c.gridwidth = 2
        add(rayCountLabel, c)

        c.gridx = 0
        c.gridy = 2
        c.ipadx = 250
        c.gridwidth = 2
        add(rayCountSlider, c)



        this.background = Color(220, 220, 220)
    }

}