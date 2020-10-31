package click.seichi.petra.stage.section.wave

import click.seichi.petra.GameSound
import click.seichi.petra.TopBarType
import click.seichi.petra.game.Game
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
import java.util.*

/**
 * @author tar0ss
 */
class BonusWave(
        private val summonData: SummonData,
        private val seconds: Int,
        private val startMessage: Message? = GameSound.START_BREAK_SECTION.add(
                TitleMessage("", "ボーナスタイム")
        )
) : Section {

    private val subject: Subject<StageResult> = PublishSubject.create()

    override fun endAsObservable(): Observable<StageResult> = subject.doOnDispose {
        timer.cancel()
    }

    protected lateinit var summonProxy: SummonProxy
    protected lateinit var world: World
    protected lateinit var players: Set<UUID>
    private lateinit var topBar: TopBar
    private lateinit var bar: BossBar

    private val timer = Timer(
            seconds,
            onStart = {
                summon(summonData)
            },
            onNext = { remainSeconds ->
                updateBar(remainSeconds)
            },
            onComplete = {
                bar.isVisible = false
                subject.onNext(StageResult.WIN)
            },
            onCancelled = {
                bar.isVisible = false
            }
    )

    private fun summon(summonData: SummonData) {
        val summonedSet = summonData.summoner.summon(world, summonProxy, players)
        summonedSet.mapNotNull { Bukkit.getServer().getEntity(it) }
        summonData.message.broadcast()
    }

    private fun setupBar() {
        bar.style = BarStyle.SEGMENTED_20
        bar.color = BarColor.WHITE
        updateBar(seconds)
        bar.isVisible = true
    }

    private fun updateBar(remainSeconds: Int) {
        val title = "${ChatColor.WHITE}次のウェーブまで 残り${remainSeconds}秒"
        bar.setTitle(title)
        bar.progress = remainSeconds.toDouble() / seconds.toDouble()
    }

    override fun start(game: Game, summonProxy: SummonProxy): Section {
        this.summonProxy = summonProxy
        this.world = game.world
        this.players = game.players
        this.topBar = game.topBar
        this.bar = topBar.get(TopBarType.WAVE)
        startMessage?.broadcast()
        setupBar()
        timer.start()
        return this
    }
}