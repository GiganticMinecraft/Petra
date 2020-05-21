package click.seichi.petra.stage.section

import click.seichi.game.IGame
import click.seichi.message.Message
import click.seichi.petra.TopBarConstants
import click.seichi.petra.stage.StageResult
import click.seichi.petra.stage.summon.SummonProxy
import click.seichi.util.Timer
import click.seichi.util.TopBar
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
        private val startMessage: Message? = null
) : Section {

    private val subject: Subject<StageResult> = PublishSubject.create()

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
                topBar.removeBar(TopBarConstants.WAVE)
                subject.onNext(StageResult.WIN)
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

    override fun start(game: IGame, summonProxy: SummonProxy) {
        this.topBar = game.topBar
        this.bar = topBar.register(TopBarConstants.WAVE)
        setupBar()
        startMessage?.broadcast()
        timer.start()
    }

    override fun endAsObservable(): Observable<StageResult> = subject
}