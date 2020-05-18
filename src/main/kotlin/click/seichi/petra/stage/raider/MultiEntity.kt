package click.seichi.petra.stage.raider

import click.seichi.petra.stage.spawn.SpawnProxy
import org.bukkit.World
import java.util.*

/**
 * @author tar0ss
 */
class MultiEntity(val entity: StageEntity, val n: Int) : StageEntity {
    override fun spawn(world: World, spawnProxy: SpawnProxy) {
        entity.spawn(world, spawnProxy)
    }

    override fun calcNumSpawn(playerCount: Int): Int {
        return entity.calcNumSpawn(playerCount).times(n)
    }

}