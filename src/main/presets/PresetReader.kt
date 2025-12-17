package main.presets

import main.geometry.Circle
import main.geometry.Vec2

class PresetReader(var fileName: String) {
    private fun loadFileFromResources(): String? {
        return PresetReader::class.java.getResource("/$fileName")?.readText()
    }

    fun readContentIntoCircles(): Pair<MutableList<Circle>, Float> {
        val fileContent = loadFileFromResources()
        if (fileContent == null) {
            println("Could not find preset $fileName, loaded simple instead")
            return Presets.createSimple()
        }

        val lines = fileContent.lines()
        val circles = mutableListOf<Circle>()
        if (lines.isEmpty() || lines[0] != "# Preset Configuration File") {
            println("Incorrect file format, loaded simple instead")
            return Presets.createSimple()
        }

        for (line in lines) {
            if (line.startsWith("#") || line.isBlank()) continue
            val parts = line.split(",")
            if (parts.size >= 3) {
                val layerNum = parts[0].trim().toIntOrNull()
                val waveVelocity = parts[1].trim().toFloatOrNull()
                val radius = parts[2].trim().toFloatOrNull()

                if (radius != null && waveVelocity != null && layerNum != null) {
                    val circle = Circle(Vec2(0.0, 0.0), radius, waveVelocity, layerNum)
                    circles.add(circle)
                } else {
                    println("Invalid data in line: $line")
                }
            } else {
                println("Invalid format in line: $line")
            }
        }

        return circles to (circles.maxOfOrNull { it.radius } ?: 100f)
    }
}
