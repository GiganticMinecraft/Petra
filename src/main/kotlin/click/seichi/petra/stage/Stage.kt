package click.seichi.petra.stage

import click.seichi.petra.stage.generator.StageGenerator
import click.seichi.petra.stage.stages.FirstLayer
import click.seichi.petra.stage.summon.SummonProxy
import click.seichi.petra.stage.wave.Wave

/**
 * @author tar0ss
 */
enum class Stage(
        val key: String,
        val generator: StageGenerator,
        val summonProxy: SummonProxy,
        val startTime: Long,
        vararg _waves: Wave
) {
    PETRA_ONE(
            FirstLayer.KEY,
            FirstLayer.GENERATOR,
            FirstLayer.SPAWN_PROXY,
            6000L,
            *FirstLayer.WAVES
    )
    ;

    val waves = _waves

    companion object {
        val keyMap = values().map { it.key to it }.toMap()

        fun find(key: String) = keyMap[key]
    }
}