package click.seichi.petra.stage.section

import click.seichi.petra.game.Game
import click.seichi.petra.stage.StageResult
import click.seichi.petra.stage.summon.SummonProxy
import io.reactivex.Observable

/**
 * @author tar0ss
 */
interface Section {
    fun start(game: Game, summonProxy: SummonProxy): Section
    fun endAsObservable(): Observable<StageResult>
}