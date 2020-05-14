package click.seichi.petra.stage

import click.seichi.function.warning
import click.seichi.petra.event.WaveEvent
import org.bukkit.Bukkit

/**
 * @author tar0ss
 */
class WaveController(
        vararg waves: Wave
) {
    private var currentWaveIndex = -1
    private val waveList = waves.toList()
    private var isStarted = false

    fun start() {
        if (isStarted) {
            warning("wave is already started.")
            return
        }
        isStarted = true
        currentWaveIndex = 0
        start(currentWaveIndex)
    }

    private fun nextWave() {
        if (++currentWaveIndex < waveList.size) start(currentWaveIndex)
        else end()
    }

    private fun start(i: Int) {
        val wave = waveList[i]
        Bukkit.getPluginManager().callEvent(WaveEvent(i))
        wave.start()
        wave.endAsObservable()
                .subscribe {
                    nextWave()
                }
    }

    private fun end() {

    }

}