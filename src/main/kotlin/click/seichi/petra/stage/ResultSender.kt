package click.seichi.petra.stage

import click.seichi.game.IGame
import click.seichi.message.ActionMessage
import click.seichi.util.Timer
import click.seichi.util.TopBar
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import kotlin.properties.Delegates

/**
 * @author tar0ss
 */
class ResultSender {
    private val subject: Subject<Unit> = PublishSubject.create()

    private var seconds: Int by Delegates.notNull()
    private lateinit var topBar: TopBar
    private lateinit var bar: BossBar
    private lateinit var result: StageResult

    private val timer = Timer(
            seconds,
            onStart = {
            },
            onNext = { remainSeconds ->
                ActionMessage("${ChatColor.RED}強制退出まであと${remainSeconds}秒")
                        .broadcast()
//                updateBar(remainSeconds)
            },
            onComplete = {
//                topBar.removeBar(TopBarConstants.WAVE)
                subject.onNext(Unit)
            }
    )

    private fun setupBar() {
        bar.style = BarStyle.SEGMENTED_20
        bar.color = BarColor.WHITE
//        updateBar(seconds)
        bar.isVisible = true
    }

    private fun updateBar(remainSeconds: Int) {
        val title = "${ChatColor.WHITE} 残り${remainSeconds}秒"
        bar.setTitle(title)
        bar.progress = remainSeconds.toDouble() / seconds.toDouble()
    }

    fun start(result: StageResult, seconds: Int, game: IGame): ResultSender {
//        this.topBar = game.topBar
//        this.bar = topBar.register(TopBarConstants.WAVE)
        this.result = result
        this.seconds = seconds
        timer.start()
        return this
    }

    fun endAsObservable(): Observable<Unit> = subject
}