package click.seichi.petra.stage.raider

import click.seichi.petra.stage.spawn.SpawnProxy
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Skeleton
import org.bukkit.util.Consumer

/**
 * @author tar0ss
 */
class SkeletonEntity : StageEntity {

    private val entityType = EntityType.SKELETON

    override fun spawn(world: World, spawnProxy: SpawnProxy) {
        spawnProxy.spawn(world, entityType, Consumer {
            val skeleton = it as Skeleton
            skeleton.removeWhenFarAway = false
        })
    }

    override fun calcNumSpawn(playerCount: Int): Int {
        return playerCount
    }
}