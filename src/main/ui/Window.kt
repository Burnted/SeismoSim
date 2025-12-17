package main.ui

import javax.swing.JFrame

class Window: JFrame() {
    init {
        val appPane = AppPanel()
        this.add(appPane.splitPane)

        this.title = "Game"
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.isResizable = true
        this.isAlwaysOnTop = true
        this.pack()
        this.setLocationRelativeTo(null)
        this.isVisible = true
    }
}