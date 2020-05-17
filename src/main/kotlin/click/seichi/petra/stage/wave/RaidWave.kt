package click.seichi.petra.stage.wave

import click.seichi.game.IGame
import click.seichi.petra.stage.raider.StageEntity
import click.seichi.petra.stage.spawn.SpawnProxy
import click.seichi.util.Timer
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.bukkit.World
import java.util.*

/**
 * @author tar0ss
 */
class RaidWave(private val raidData: WaveData, seconds: Int) : Wave {

    private val subject: Subject<Unit> = PublishSubject.create()

    private lateinit var spawnProxy: SpawnProxy
    private lateinit var world: World
    private lateinit var players: Set<UUID>

    private val timer = Timer(
            seconds,
            onNext = { remainSeconds ->
                val elapsedSeconds = seconds - remainSeconds
                val stageEntity = raidData.findEntity(elapsedSeconds) ?: return@Timer
                val count = stageEntity.calcNumSpawn(players.size)
                spawn(stageEntity, count)
            }
            ,
            onComplete = {
                subject.onNext(Unit)
            }
    )

    override fun start(game: IGame, spawnProxy: SpawnProxy) {
        this.spawnProxy = spawnProxy
        this.world = game.world
        this.players = game.players
        timer.start()
    }

    private fun spawn(stageEntity: StageEntity, n: Int) {
        (1..n).forEach { _ -> stageEntity.spawn(world, spawnProxy) }
    }

    override fun endAsObservable(): Observable<Unit> = subject
}