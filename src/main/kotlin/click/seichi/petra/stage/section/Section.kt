package click.seichi.petra.stage.section

import click.seichi.game.IGame
import click.seichi.petra.stage.StageResult
import click.seichi.petra.stage.summon.SummonProxy
import io.reactivex.Observable

/**
 * @author tar0ss
 */
interface Section {
    fun start(game: IGame, summonProxy: SummonProxy): Section
    fun endAsObservable(): Observable<StageResult>
}