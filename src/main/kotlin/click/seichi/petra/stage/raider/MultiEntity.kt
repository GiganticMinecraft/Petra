package click.seichi.petra.stage.raider

import click.seichi.petra.stage.spawn.SpawnProxy
import org.bukkit.World
import java.util.*

/**
 * @author tar0ss
 */
class MultiEntity(vararg pairs: Pair<StageEntity, Int>) : StageEntity {

    private val entityPairList = listOf(*pairs)

    override fun spawn(world: World, spawnProxy: SpawnProxy, players: Set<UUID>) {
        entityPairList.forEach { (entity, n) ->
            (1..n).forEach { _ ->
                entity.spawn(world, spawnProxy, players)
            }
        }
    }

}