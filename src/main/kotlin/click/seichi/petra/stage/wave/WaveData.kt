package click.seichi.petra.stage.wave

import click.seichi.petra.stage.raider.StageEntity

/**
 * @author tar0ss
 */
data class WaveData(
        private val timingMap: Map<Int, StageEntity>
) {
    fun findEntity(timing: Int): StageEntity? {
        return timingMap[timing]
    }

    fun hasNextRaid(current: Int): Boolean {
        return timingMap.keys.firstOrNull { it > current } != null
    }

    fun calcRemainedNextRaidSeconds(current: Int): Int? {
        val nextTiming = findNextTiming(current) ?: return null
        return nextTiming - current
    }

    private fun findNextTiming(timing: Int): Int? {
        return timingMap.keys.firstOrNull { it - timing > 0 }
    }
}