package click.seichi.petra.stage.wave

import click.seichi.game.IGame
import click.seichi.message.Message
import click.seichi.petra.TopBarConstants
import click.seichi.petra.stage.spawn.SpawnProxy
import click.seichi.util.Timer
import click.seichi.util.TopBar
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
 * 時間制ウェーブ
 * @author tar0ss
 *
 * @param raidData
 * @param seconds 終了時間
 */
class TimedWave(
        private val raidData: WaveData,
        private val seconds: Int,
        private val startMessage: Message
) : Wave {

    private val subject: Subject<Unit> = PublishSubject.create()

    private lateinit var spawnProxy: SpawnProxy
    private lateinit var world: World
    private lateinit var players: Set<UUID>
    private lateinit var topBar: TopBar
    private var index: Int = 0
    private var remainedNextRaidSeconds: Int? = 0

    private lateinit var bar: BossBar

    private lateinit var raidBar: BossBar

    private val timer = Timer(
            seconds,
            onStart = {
                remainedNextRaidSeconds = raidData.calcRemainedNextRaidSeconds(0)
            },
            onNext = { remainSeconds ->
                updateBar(remainSeconds)
                val elapsedSeconds = seconds - remainSeconds
                if (raidData.hasNextRaid(elapsedSeconds)) {
                    updateRaidBar(raidData.calcRemainedNextRaidSeconds(elapsedSeconds)!!, remainedNextRaidSeconds!!)
                }
                val stageEntity = raidData.findEntity(elapsedSeconds) ?: return@Timer
                remainedNextRaidSeconds = raidData.calcRemainedNextRaidSeconds(elapsedSeconds)
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
        this.topBar = game.topBar
        this.bar = topBar.findBar(TopBarConstants.WAVE)!!
        startMessage.broadcastTo { players.contains(it.uniqueId) }
        setupBar()
        if (raidData.hasNextRaid(0)) {
            this.raidBar = topBar.register(TopBarConstants.RAID_TIME)
            setupRaidBar()
        }

        timer.start()
    }

    private fun setupBar() {
        val title = "${ChatColor.RED}Wave${index.plus(1)} 残り時間"
        bar.setTitle(title)
        bar.style = BarStyle.SEGMENTED_20
        bar.color = BarColor.WHITE
        bar.isVisible = true
    }

    private fun updateBar(remainSeconds: Int) {
        bar.progress = remainSeconds.toDouble() / seconds.toDouble()
    }

    private fun setupRaidBar() {
        val title = "${ChatColor.WHITE}次の襲撃まで"
        raidBar.setTitle(title)
        raidBar.style = BarStyle.SEGMENTED_20
        raidBar.color = BarColor.RED
        raidBar.isVisible = true
    }

    private fun updateRaidBar(remainSeconds: Int, raidSeconds: Int) {
        raidBar.progress = remainSeconds.toDouble() / raidSeconds.toDouble()
    }

    private fun end() {
        bar.isVisible = false
        topBar.removeBar(TopBarConstants.RAID_TIME)
        subject.onNext(Unit)
    }

    override fun endAsObservable(): Observable<Unit> = subject
}