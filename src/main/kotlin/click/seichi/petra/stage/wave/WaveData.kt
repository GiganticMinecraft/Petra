package click.seichi.petra.stage.wave

import click.seichi.petra.stage.raider.StageEntity

/**
 * @author tar0ss
 */
data class WaveData(
        val timingMap: Map<Int, StageEntity>
) {
    fun findEntity(timing: Int): StageEntity? {
        return timingMap[timing]
    }
}