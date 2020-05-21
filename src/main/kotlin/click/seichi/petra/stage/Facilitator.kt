package click.seichi.petra.stage

import click.seichi.function.warning
import click.seichi.game.IGame
import click.seichi.petra.event.WaveEvent
import click.seichi.petra.stage.section.Section
import click.seichi.petra.stage.summon.SummonProxy
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.bukkit.Bukkit

/**
 * ゲームの進行役
 * @author tar0ss
 */
class Facilitator {
    private val subject: Subject<StageResult> = PublishSubject.create()

    fun endAsObservable(): Observable<StageResult> = subject

    private lateinit var game: IGame
    private lateinit var sectionList: List<Section>
    private lateinit var summonProxy: SummonProxy

    private var current = -1
    private var isStarted = false

    fun start(game: IGame, stage: Stage): Facilitator {
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
        else end(StageResult.WIN)
    }

    private fun startSection(i: Int) {
        val section = sectionList[i]
        Bukkit.getPluginManager().callEvent(WaveEvent(i))
        section.start(game, summonProxy)
                .endAsObservable()
                .take(1)
                .subscribe { result ->
                    when (result) {
                        StageResult.WIN -> nextSection()
                        else -> end(result)
                    }
                }
    }

    private fun end(result: StageResult) {
        subject.onNext(result)
    }

}