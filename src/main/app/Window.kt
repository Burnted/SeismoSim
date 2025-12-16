package main.app

import javax.swing.JFrame

class Window: JFrame() {
    init {
        val simPanel = SimPanel()
        this.add(simPanel)

        this.title = "Game"
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.isResizable = false
        this.isAlwaysOnTop = true
        this.pack()
        this.setLocationRelativeTo(null)
        this.isVisible = true
    }
}