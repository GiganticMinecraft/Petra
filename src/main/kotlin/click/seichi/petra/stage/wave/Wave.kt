package click.seichi.petra.stage.wave

import click.seichi.game.IGame
import click.seichi.petra.stage.summon.SummonProxy
import io.reactivex.Observable

/**
 * @author tar0ss
 */
interface Wave {
    fun start(index: Int, game: IGame, summonProxy: SummonProxy)
    fun endAsObservable(): Observable<Unit>
}