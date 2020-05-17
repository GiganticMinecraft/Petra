package click.seichi.petra.stage.wave

import click.seichi.petra.stage.spawn.SpawnProxy
import io.reactivex.Observable
import org.bukkit.World

/**
 * @author tar0ss
 */
interface Wave {
    fun start(spawnProxy: SpawnProxy, world: World)
    fun endAsObservable(): Observable<Unit>
}