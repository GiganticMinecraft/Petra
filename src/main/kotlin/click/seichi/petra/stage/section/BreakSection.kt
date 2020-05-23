package click.seichi.petra.stage.section

import click.seichi.petra.GameSound
import click.seichi.petra.TopBarType
import click.seichi.petra.game.Game
import click.seichi.petra.message.Message
import click.seichi.petra.stage.StageResult
import click.seichi.petra.stage.summon.SummonProxy
import click.seichi.petra.util.Timer
import click.seichi.petra.util.TopBar
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar

/**
 * @author tar0ss
 */
class BreakSection(
        private val seconds: Int,
        private val startMessage: Message? = GameSound.START_BREAK_SECTION
) : Section {

    private val subject: Subject<StageResult> = PublishSubject.create()

    override fun endAsObservable(): Observable<StageResult> = subject.doOnDispose {
        timer.cancel()
    }

    private lateinit var topBar: TopBar
    private lateinit var bar: BossBar

    private val timer = Timer(
            seconds,
            onStart = {
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
        this.topBar = game.topBar
        this.bar = topBar.get(TopBarType.WAVE)
        startMessage?.broadcast()
        setupBar()
        timer.start()
        return this
    }
}