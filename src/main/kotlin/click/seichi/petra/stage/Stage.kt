package click.seichi.petra.stage

import click.seichi.petra.stage.generator.FirstStageGenerator

/**
 * @author tar0ss
 */
enum class Stage(
        val key: String,
        val generator: StageGenerator,
        vararg waves: Wave
) {
    PETRA_ONE("1stLayer", FirstStageGenerator())
    ;

    val waveController: WaveController = WaveController(*waves)
    val numWaves = waves.size

    fun isDangerZone(globalX: Int, globalY: Int, globalZ: Int): Boolean {
        return this.generator.isDangerZone(globalX, globalY, globalZ)
    }

    fun isSafeZone(globalX: Int, globalY: Int, globalZ: Int): Boolean {
        return this.generator.isSafeZone(globalX, globalY, globalZ)
    }

    companion object {
        val keyMap = values().map { it.key to it }.toMap()

        fun find(key: String) = keyMap[key]
    }
}