package click.seichi.petra.stage.raider

import click.seichi.petra.stage.spawn.SpawnProxy
import org.bukkit.World
import java.util.*

/**
 * @author tar0ss
 */
class MultiEntity(vararg pairs: Pair<Spawnable, Int>) : Spawnable {

    private val entityPairList = listOf(*pairs)

    override fun spawn(world: World, spawnProxy: SpawnProxy, players: Set<UUID>): Set<UUID> {
        return entityPairList.flatMap { (entity, n) ->
            (1..n).flatMap { _ ->
                entity.spawn(world, spawnProxy, players)
            }
        }.toSet()
    }

}