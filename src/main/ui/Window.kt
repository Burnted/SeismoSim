package main.ui

import javax.swing.JFrame

class Window: JFrame() {
    init {
        // Enable hardware acceleration
        System.setProperty("sun.java2d.opengl", "true")
        System.setProperty("sun.java2d.accelblit", "true")
        System.setProperty("sun.java2d.noddraw", "true")
        // For macOS
        System.setProperty("apple.awt.graphics.UseQuartz", "false")

        val appPane = AppPanel()
        this.add(appPane.splitPane)

        this.title = "SeismoSim"
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.isResizable = true
        this.pack()
        this.setLocationRelativeTo(null)
        this.isVisible = true
    }
}