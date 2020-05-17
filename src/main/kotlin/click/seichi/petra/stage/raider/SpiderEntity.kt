package click.seichi.petra.stage.raider

import click.seichi.petra.stage.spawn.SpawnProxy
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Spider
import org.bukkit.util.Consumer

/**
 * @author tar0ss
 */
class SpiderEntity : StageEntity {

    private val entityType = EntityType.SPIDER

    override fun spawn(world: World, spawnProxy: SpawnProxy) {
        spawnProxy.spawn(world, entityType, Consumer {
            val spider = it as Spider
            spider.removeWhenFarAway = false
        })
    }

    override fun calcNumSpawn(playerCount: Int): Int {
        return playerCount
    }
}