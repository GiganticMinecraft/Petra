package click.seichi.petra.stage

import click.seichi.function.warning
import click.seichi.game.IGame
import click.seichi.petra.event.WaveEvent
import click.seichi.petra.stage.summon.SummonProxy
import click.seichi.petra.stage.wave.IWave
import org.bukkit.Bukkit

/**
 * @author tar0ss
 */
class Waver {
    private lateinit var game: IGame
    private lateinit var waveList: List<IWave>
    private lateinit var summonProxy: SummonProxy


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
        this.summonProxy = stage.summonProxy

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
        wave.start(i, game, summonProxy)
        wave.endAsObservable()
                .take(1)
                .subscribe {
                    nextWave()
                }
    }

    private fun end() {
    }

}