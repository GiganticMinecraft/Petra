package click.seichi.petra.stage

import click.seichi.petra.event.WaveEvent
import click.seichi.petra.extension.register
import click.seichi.petra.function.warning
import click.seichi.petra.game.Game
import click.seichi.petra.stage.section.Section
import click.seichi.petra.stage.summon.SummonProxy
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.bukkit.Bukkit
import org.bukkit.event.Listener

/**
 * ゲームの進行役
 * @author tar0ss
 */
class Facilitator {
    private val subject: Subject<StageResult> = PublishSubject.create()

    fun endAsObservable(): Observable<StageResult> = subject.doOnDispose {
        disposable.dispose()
    }

    private lateinit var game: Game
    private lateinit var sectionList: List<Section>
    private lateinit var summonProxy: SummonProxy

    private var current = -1
    private var isStarted = false

    private lateinit var disposable: Disposable

    fun start(game: Game, stage: Stage): Facilitator {
        if (isStarted) {
            warning("wave is already started.")
            return this
        }
        isStarted = true

        this.game = game
        this.sectionList = stage.sections.toList()
        this.summonProxy = stage.summonProxy

        current = 0
        startSection(current)
        return this
    }

    private fun nextSection() {
        if (++current < sectionList.size) startSection(current)
        else subject.onNext(StageResult.WIN)
    }

    private fun startSection(i: Int) {
        val section = sectionList[i]
        Bukkit.getPluginManager().callEvent(WaveEvent(i))
        if (section is Listener) section.register()
        disposable = section.start(game, summonProxy)
                .endAsObservable()
                .take(1)
                .subscribe { result ->
                    if (result == StageResult.WIN) nextSection()
                    else subject.onNext(result)
                }
    }
}