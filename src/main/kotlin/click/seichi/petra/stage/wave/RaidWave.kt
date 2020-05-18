package click.seichi.petra.stage.wave

import click.seichi.game.IGame
import click.seichi.petra.stage.spawn.SpawnProxy
import click.seichi.util.Timer
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import java.util.*

/**
 * @author tar0ss
 */
class RaidWave(private val raidData: WaveData, private val seconds: Int) : Wave {

    private val subject: Subject<Unit> = PublishSubject.create()

    private lateinit var spawnProxy: SpawnProxy
    private lateinit var world: World
    private lateinit var players: Set<UUID>
    private var index: Int = 0

    private lateinit var bar: BossBar

    override fun getBossBar(): BossBar = bar

    private val timer = Timer(
            seconds,
            onNext = { remainSeconds ->
                updateBar(remainSeconds)
                val elapsedSeconds = seconds - remainSeconds
                val stageEntity = raidData.findEntity(elapsedSeconds) ?: return@Timer
                stageEntity.spawn(world, spawnProxy, players)
            },
            onComplete = {
                end()
            }
    )

    override fun start(index: Int, game: IGame, spawnProxy: SpawnProxy) {
        this.spawnProxy = spawnProxy
        this.world = game.world
        this.players = game.players
        this.index = index
        this.bar = game.waveBossBar
        setupBar()

        timer.start()
    }

    private fun setupBar() {
        val waveName = "${ChatColor.RED}Wave${index.plus(1)} 襲撃"
        bar.setTitle(waveName)
        bar.style = BarStyle.SEGMENTED_20
        bar.color = BarColor.RED
        bar.isVisible = true
    }

    private fun updateBar(remainSeconds: Int) {
        bar.progress = remainSeconds.toDouble() / seconds.toDouble()
    }

    private fun end() {
        bar.isVisible = false
        subject.onNext(Unit)
    }

    override fun endAsObservable(): Observable<Unit> = subject
}