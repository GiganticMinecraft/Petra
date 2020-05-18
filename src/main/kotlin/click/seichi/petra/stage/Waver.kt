package click.seichi.petra.stage

import click.seichi.function.warning
import click.seichi.game.IGame
import click.seichi.petra.event.WaveEvent
import click.seichi.petra.stage.spawn.SpawnProxy
import click.seichi.petra.stage.wave.Wave
import org.bukkit.Bukkit

/**
 * @author tar0ss
 */
class Waver {
    private lateinit var game: IGame
    private lateinit var waveList: List<Wave>
    private lateinit var spawnProxy: SpawnProxy


    private var currentWaveIndex = -1
    private var isStarted = false

    fun start(game: IGame, stage: Stage) {
        if (isStarted) {
            warning("wave is already started.")
            return
        }
        isStarted = true

        this.game = game
        this.waveList = stage.waves.toList()
        this.spawnProxy = stage.spawnProxy

        currentWaveIndex = 0
        startWave(currentWaveIndex)
    }

    private fun nextWave() {
        if (++currentWaveIndex < waveList.size) startWave(currentWaveIndex)
        else end()
    }

    private fun startWave(i: Int) {
        val wave = waveList[i]
        Bukkit.getPluginManager().callEvent(WaveEvent(i))
        wave.start(i, game, spawnProxy)
        wave.endAsObservable()
                .take(1)
                .subscribe {
                    nextWave()
                }
    }

    private fun end() {
    }

}