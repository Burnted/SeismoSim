package main.presets

import main.geometry.Circle
import main.geometry.Vec2

class PresetReader(private val fileName: String) {
    private fun loadFileFromResources(): String?{
        return PresetReader::class.java.getResource("/$fileName")?.readText()
    }

    fun readContentIntoCircles(): MutableList<Circle> {
        val fileContent = loadFileFromResources()
        if (fileContent == null){
            println("Could not find preset $fileName, loaded simple instead")
            return EarthPreset.createEarth()
        }

        return mutableListOf(Circle(Vec2(0.0, 0.0), 20.0f, 1.0))
    }
}