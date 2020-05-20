package click.seichi.petra.stage.wave

/**
 * @author tar0ss
 */
data class WaveData(
        private val timingMap: Map<Int, SummonData>
) {
    fun findSpawnData(timing: Int): SummonData? {
        return timingMap[timing]
    }

    fun hasNextSpawn(current: Int): Boolean {
        return timingMap.keys.firstOrNull { it > current } != null
    }

    fun calcRemainNextSpawnSeconds(current: Int): Int? {
        val nextTiming = findNextTiming(current) ?: return null
        return nextTiming - current
    }

    private fun findNextTiming(timing: Int): Int? {
        return timingMap.keys.firstOrNull { it - timing > 0 }
    }
}