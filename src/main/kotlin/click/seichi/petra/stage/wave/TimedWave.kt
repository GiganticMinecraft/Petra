package click.seichi.petra.stage.wave

import click.seichi.game.IGame
import click.seichi.message.Message
import click.seichi.petra.TopBarConstants
import click.seichi.petra.stage.summon.SummonProxy
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
 * @param startMessage
 */
class TimedWave(
        private val raidData: WaveData,
        private val seconds: Int,
        private val startMessage: Message
) : IWave {

    private val subject: Subject<Unit> = PublishSubject.create()

    private lateinit var summonProxy: SummonProxy
    private lateinit var world: World
    private lateinit var players: Set<UUID>
    private lateinit var topBar: TopBar
    private var index: Int = 0
    private var remainNextSpawnSeconds: Int? = 0

    private lateinit var bar: BossBar
    private lateinit var raidBar: BossBar

    private val entitySet = mutableSetOf<UUID>()

    private val timer = Timer(
            seconds,
            onStart = {
            },
            onNext = { remainSeconds ->
                updateBar(remainSeconds)
                val elapsedSeconds = seconds - remainSeconds
                val hasNextSpawn = raidData.hasNextSpawn(elapsedSeconds)
                val _remainNextSpawnSeconds = raidData.calcRemainNextSpawnSeconds(elapsedSeconds)
                if (hasNextSpawn) updateRaidBar(_remainNextSpawnSeconds!!)

                val spawnData = raidData.findSpawnData(elapsedSeconds) ?: return@Timer
                if (hasNextSpawn) remainNextSpawnSeconds = _remainNextSpawnSeconds
                else removeRaidBar()
                summon(spawnData)
            },
            onComplete = {
                removeBar()
                removeAllEntities()
                end()
            }
    )

    private fun summon(summonData: SummonData) {
        entitySet.addAll(summonData.summoner.summon(world, summonProxy, players))
        summonData.message.broadcast()
    }

    override fun start(index: Int, game: IGame, summonProxy: SummonProxy) {
        this.summonProxy = summonProxy
        this.world = game.world
        this.players = game.players
        this.index = index
        this.topBar = game.topBar
        this.bar = topBar.findBar(TopBarConstants.WAVE)!!
        remainNextSpawnSeconds = raidData.calcRemainNextSpawnSeconds(0)
        startMessage.broadcast()
        setupBar()
        if (raidData.hasNextSpawn(0)) {
            this.raidBar = topBar.register(TopBarConstants.RAID_TIME)
            setupRaidBar()
        }

        timer.start()
    }

    private fun setupBar() {
        bar.style = BarStyle.SEGMENTED_20
        bar.color = BarColor.WHITE
        updateBar(seconds)
        bar.isVisible = true
    }

    private fun updateBar(remainSeconds: Int) {
        val title = "${ChatColor.WHITE}Wave${index.plus(1)} 残り時間 ${remainSeconds}秒"
        bar.setTitle(title)
        bar.progress = remainSeconds.toDouble() / seconds.toDouble()
    }

    private fun setupRaidBar() {
        raidBar.style = BarStyle.SEGMENTED_20
        raidBar.color = BarColor.RED
        updateRaidBar(remainNextSpawnSeconds!!)
        raidBar.isVisible = true
    }

    private fun updateRaidBar(remainSeconds: Int) {
        val title = "${ChatColor.RED}次の襲撃まで ${remainSeconds}秒"
        raidBar.setTitle(title)
        raidBar.progress = remainSeconds.toDouble() / remainNextSpawnSeconds!!.toDouble()
    }

    private fun removeRaidBar() {
        topBar.removeBar(TopBarConstants.RAID_TIME)
    }

    private fun removeBar() {
        bar.isVisible = false
    }

    private fun end() {
        subject.onNext(Unit)
    }

    private fun removeAllEntities() {
        entitySet.mapNotNull { world.getEntity(it) }
                .forEach { it.remove() }
    }

    override fun endAsObservable(): Observable<Unit> = subject
}