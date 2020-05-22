package click.seichi.petra.stage.section.wave

import click.seichi.function.getNearestPlayer
import click.seichi.game.IGame
import click.seichi.message.Message
import click.seichi.message.SoundMessage
import click.seichi.message.TitleMessage
import click.seichi.petra.TopBarConstants
import click.seichi.petra.stage.StageResult
import click.seichi.petra.stage.section.Section
import click.seichi.petra.stage.summon.SummonProxy
import click.seichi.util.Timer
import click.seichi.util.TopBar
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.bukkit.*
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Entity
import org.bukkit.entity.Mob
import java.util.*

/**
 * 時間制ウェーブ
 * 〇分間生き延びろ
 * @author tar0ss
 *
 * @param waveNum ウェーブ数
 * @param minutes 終了時間
 * @param raidData
 */
open class Wave(
        private val waveNum: Int,
        private val minutes: Int,
        private val raidData: WaveData
) : Section {

    protected val subject: Subject<StageResult> = PublishSubject.create()
    override fun endAsObservable(): Observable<StageResult> = subject.doOnDispose {
        timer.cancel()
    }

    protected val seconds = minutes * 60

    protected lateinit var summonProxy: SummonProxy
    protected lateinit var world: World
    protected lateinit var players: Set<UUID>
    protected lateinit var topBar: TopBar
    private var remainNextSpawnSeconds: Int? = 0
    protected var hasNextSpawn = true

    protected lateinit var bar: BossBar
    protected lateinit var raidBar: BossBar

    protected val entitySet = mutableSetOf<UUID>()

    private val timer = Timer(
            seconds,
            onStart = {
            },
            onNext = { remainSeconds ->
                updateBar(remainSeconds)
                val elapsedSeconds = seconds - remainSeconds
                hasNextSpawn = raidData.hasNextSpawn(elapsedSeconds)
                val _remainNextSpawnSeconds = raidData.calcRemainNextSpawnSeconds(elapsedSeconds)
                if (hasNextSpawn) updateRaidBar(_remainNextSpawnSeconds!!)

                val spawnData = raidData.findSpawnData(elapsedSeconds) ?: return@Timer
                if (hasNextSpawn) remainNextSpawnSeconds = _remainNextSpawnSeconds
                else topBar.removeBar(TopBarConstants.RAID_TIME)
                summon(spawnData)
            },
            onComplete = {
                topBar.removeBar(TopBarConstants.WAVE)
                removeAllEntities()
                onTimeUp()
            },
            onCancelled = {
                topBar.removeBar(TopBarConstants.WAVE)
                topBar.removeBar(TopBarConstants.RAID_TIME)
                removeAllEntities()
            }
    )

    open fun onTimeUp() {
        subject.onNext(StageResult.WIN)
    }

    private fun summon(summonData: SummonData) {
        val summonedSet = summonData.summoner.summon(world, summonProxy, players)
        summonedSet.mapNotNull { Bukkit.getServer().getEntity(it) }
                .forEach { onSummoned(it) }

        entitySet.addAll(summonedSet)
        summonData.message.broadcast()
    }

    protected open fun onSummoned(entity: Entity) {
        if (entity is Mob) {
            entity.target = entity.getNearestPlayer(players)
        }
    }

    override fun start(game: IGame, summonProxy: SummonProxy): Section {
        this.summonProxy = summonProxy
        this.world = game.world
        this.players = game.players
        this.topBar = game.topBar
        this.bar = topBar.register(TopBarConstants.WAVE)

        onStart()

        remainNextSpawnSeconds = raidData.calcRemainNextSpawnSeconds(0)
        getStartMessage().broadcast()
        setupBar()
        if (raidData.hasNextSpawn(0)) {
            this.raidBar = topBar.register(TopBarConstants.RAID_TIME)
            setupRaidBar()
        }

        timer.start()
        return this
    }

    protected open fun onStart() {
    }

    protected open fun getStartMessage(): Message {
        return TitleMessage(
                "${ChatColor.WHITE}Wave$waveNum ${ChatColor.RED}襲撃",
                "${ChatColor.AQUA}${minutes}分間生き延びろ"
        ).add(
                SoundMessage(
                        Sound.ENTITY_ILLUSIONER_CAST_SPELL,
                        SoundCategory.BLOCKS,
                        2.0f,
                        0.3f
                )
        )
    }

    protected open fun setupBar() {
        bar.style = BarStyle.SEGMENTED_20
        bar.color = BarColor.WHITE
        updateBar(seconds)
        bar.isVisible = true
    }

    protected open fun updateBar(remainSeconds: Int) {
        val title = "${ChatColor.WHITE}Wave${waveNum} 残り時間 ${remainSeconds}秒"
        bar.setTitle(title)
        bar.progress = remainSeconds.toDouble() / seconds.toDouble()
    }

    protected open fun setupRaidBar() {
        raidBar.style = BarStyle.SEGMENTED_20
        raidBar.color = BarColor.RED
        updateRaidBar(remainNextSpawnSeconds!!)
        raidBar.isVisible = true
    }

    protected open fun updateRaidBar(remainSeconds: Int) {
        val title = "${ChatColor.RED}次の襲撃まで ${remainSeconds}秒"
        raidBar.setTitle(title)
        raidBar.progress = remainSeconds.toDouble() / remainNextSpawnSeconds!!.toDouble()
    }

    private fun removeAllEntities() {
        entitySet.mapNotNull { world.getEntity(it) }
                .forEach { it.remove() }
    }

}