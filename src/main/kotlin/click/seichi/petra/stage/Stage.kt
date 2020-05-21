package click.seichi.petra.stage

import click.seichi.petra.stage.generator.StageGenerator
import click.seichi.petra.stage.section.Section
import click.seichi.petra.stage.stages.FirstLayer
import click.seichi.petra.stage.summon.SummonProxy

/**
 * @author tar0ss
 */
enum class Stage(
        val key: String,
        val generator: StageGenerator,
        val summonProxy: SummonProxy,
        val startTime: Long,
        vararg _sections: Section
) {
    PETRA_ONE(
            FirstLayer.KEY,
            FirstLayer.GENERATOR,
            FirstLayer.SPAWN_PROXY,
            6000L,
            *FirstLayer.SECTIONS
    )
    ;

    val sections = _sections

    companion object {
        val keyMap = values().map { it.key to it }.toMap()

        fun find(key: String) = keyMap[key]
    }
}