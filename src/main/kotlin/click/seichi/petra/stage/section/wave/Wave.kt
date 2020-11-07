package click.seichi.petra.stage.section.wave

import click.seichi.petra.GameSound
import click.seichi.petra.TopBarType
import click.seichi.petra.function.getNearestPlayer
import click.seichi.petra.game.Game
import click.seichi.petra.message.ChatMessage
import click.seichi.petra.message.Message
import click.seichi.petra.message.TitleMessage
import click.seichi.petra.stage.StageResult
import click.seichi.petra.stage.section.Section
import click.seichi.petra.stage.summon.SummonProxy
import click.seichi.petra.util.Timer
import click.seichi.petra.util.TopBar
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Entity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * 時間制ウェーブ
 * 〇分間生き延びろ
 * @author tar0ss
 *
 * @param waveNum ウェーブ数
 * @param minutes 終了時間
 * @param raidData
 * @param rewards
 */
open class Wave(
        protected val waveNum: Int,
        protected val minutes: Int,
        private val raidData: WaveData,
        private val rewards: List<ItemStack>
) : Section {

    protected val subject: Subject<StageResult> = PublishSubject.create()
    override fun endAsObservable(): Observable<StageResult> = subject.doOnDispose {
        timer.cancel()
    }.doOnNext {
        if (it != StageResult.WIN) return@doOnNext
        if (rewards.isEmpty()) return@doOnNext
        ChatMessage("${ChatColor.GOLD}報酬獲得!!").broadcast()
        players.mapNotNull { Bukkit.getServer().getPlayer(it) }.forEach { p ->
            p.inventory.addItem(*rewards.toTypedArray()).map { it.value }.forEach {
                p.world.dropItemNaturally(p.location, it)
            }
        }
    }

    protected val seconds = minutes * 60

    protected lateinit var summonProxy: SummonProxy
    protected lateinit var world: World
    protected lateinit var players: Set<UUID>
    protected lateinit var topBar: TopBar
    private var remainNextSpawnSeconds: Int? = 0
    protected var hasNextSpawn = true

    protected lateinit var bar: BossBar
    protected var raidBar: BossBar? = null

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
                else raidBar?.isVisible = false
                summon(spawnData)
            },
            onComplete = {
                bar.isVisible = false
                removeAllEntities()
                onTimeUp()
                onEnd()
            },
            onCancelled = {
                bar.isVisible = false
                raidBar?.isVisible = false
                removeAllEntities()
                onEnd()
            }
    )

    protected val isStarted
        get() = timer.isStarted

    open fun onTimeUp() {
        subject.onNext(StageResult.WIN)
    }

    open fun onEnd() {
    }

    private fun summon(summonData: SummonData) {
        onStartRaid(summonData)
        val summonedSet = summonData.summoner.summon(world, summonProxy, players)
        entitySet.addAll(summonedSet)
        summonedSet.mapNotNull { Bukkit.getServer().getEntity(it) }
                .forEach { onSummoned(it) }
        summonData.message.broadcast()
    }

    protected open fun onStartRaid(summonData: SummonData) {
    }

    protected open fun onSummoned(entity: Entity) {
        if (entity is Mob) {
            if (entity.target == null)
                entity.target = entity.getNearestPlayer(players)
        }
    }

    override fun start(game: Game, summonProxy: SummonProxy): Section {
        this.summonProxy = summonProxy
        this.world = game.world
        this.players = game.players
        this.topBar = game.topBar
        this.bar = topBar.get(TopBarType.WAVE)

        setupBar()
        if (raidData.hasNextSpawn(0)) {
            this.raidBar = topBar.get(TopBarType.RAID_TIME)
            remainNextSpawnSeconds = raidData.calcRemainNextSpawnSeconds(0)
            setupRaidBar()
        }

        onStart()

        getStartMessage().broadcast()
        timer.start()
        return this
    }

    protected open fun onStart() {
    }

    protected open fun getStartMessage(): Message {
        return TitleMessage(
                "${ChatColor.WHITE}Wave$waveNum ${ChatColor.RED}襲撃",
                "${ChatColor.AQUA}${minutes}分間耐えろ"
        ).add(
                GameSound.START_WAVE
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
        raidBar?.style = BarStyle.SEGMENTED_20
        raidBar?.color = BarColor.RED
        updateRaidBar(remainNextSpawnSeconds!!)
        raidBar?.isVisible = true
    }

    protected open fun updateRaidBar(remainSeconds: Int) {
        val title = "${ChatColor.RED}次の襲撃まで ${remainSeconds}秒"
        raidBar?.setTitle(title)
        raidBar?.progress = remainSeconds.toDouble() / remainNextSpawnSeconds!!.toDouble()
    }

    private fun removeAllEntities() {
        world.entities.filterNotNull()
                .filter { it !is Player }
                .filterIsInstance<Mob>()
                .forEach {
                    it.health = 0.0
                }
    }

}