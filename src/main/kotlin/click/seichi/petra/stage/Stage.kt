package click.seichi.petra.stage

import click.seichi.petra.stage.generator.StageGenerator
import click.seichi.petra.stage.spawn.SpawnProxy
import click.seichi.petra.stage.stages.FirstLayer
import click.seichi.petra.stage.wave.Wave

/**
 * @author tar0ss
 */
enum class Stage(
        val key: String,
        val generator: StageGenerator,
        val spawnProxy: SpawnProxy,
        vararg _waves: Wave
) {
    PETRA_ONE(
            FirstLayer.KEY,
            FirstLayer.GENERATOR,
            FirstLayer.SPAWN_PROXY,
            *FirstLayer.WAVES
    )
    ;

    val waves = _waves

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