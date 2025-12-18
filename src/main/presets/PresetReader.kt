package main.presets

import main.geometry.Circle
import main.geometry.Vec2

class PresetReader(var fileName: String) {
    private val fileCache = mutableMapOf<String, String>()

    private fun loadFileFromResources(): String? {
        return fileCache.getOrPut(fileName) {
            PresetReader::class.java.getResource("/$fileName")?.readText() ?: return null
        }
    }

    fun readContentIntoCircles(): Pair<MutableList<Circle>, Float> {
        val fileContent = loadFileFromResources()
        if (fileContent == null) {
            println("Could not find preset $fileName, loaded simple instead")
            return Presets.createSimple()
        }

        val circles = mutableListOf<Circle>()
        val lines = fileContent.lines()

        if (lines.isEmpty() || lines[0] != "# Preset Configuration File") {
            println("Incorrect file format, loaded simple instead")
            return Presets.createSimple()
        }

        var maxRadius = 0f
        val sharedCenter = Vec2(0.0, 0.0)

        for (line in lines) {
            if (line.isEmpty() || line[0] == '#') continue

            val commaIdx = line.indexOf(',')
            if (commaIdx < 0) continue

            val comma2Idx = line.indexOf(',', commaIdx + 1)
            if (comma2Idx < 0) continue

            try {
                val layerNum = line.substring(0, commaIdx).trim().toInt()
                val waveVelocity = line.substring(commaIdx + 1, comma2Idx).trim().toFloat()
                val radius = line.substring(comma2Idx + 1).trim().toFloat()

                val circle = Circle(sharedCenter, radius, waveVelocity, 0f, layerNum)
                circles.add(circle)
                if (radius > maxRadius) maxRadius = radius
            } catch (e: Exception) {
                println("Invalid data in line: $line")
            }
        }

        return circles to (if (maxRadius > 0) maxRadius else 100f)
    }
}
