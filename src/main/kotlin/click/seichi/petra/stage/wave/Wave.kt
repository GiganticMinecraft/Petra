package click.seichi.petra.stage.wave

import click.seichi.game.IGame
import click.seichi.petra.stage.spawn.SpawnProxy
import io.reactivex.Observable

/**
 * @author tar0ss
 */
interface Wave {
    fun start(index: Int, game: IGame, spawnProxy: SpawnProxy)
    fun endAsObservable(): Observable<Unit>
}