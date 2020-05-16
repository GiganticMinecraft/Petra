package click.seichi.petra.stage

import click.seichi.petra.stage.generator.FirstStageGenerator
import click.seichi.petra.stage.spawn.RoundStageSpawner
import click.seichi.petra.stage.spawn.SpawnProxy
import click.seichi.petra.stage.wave.SimpleWave

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
            "1stLayer",
            FirstStageGenerator(64, 10),
            RoundStageSpawner(64, 10),
            SimpleWave(10, 60)
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