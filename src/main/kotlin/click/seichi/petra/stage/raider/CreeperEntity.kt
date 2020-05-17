package click.seichi.petra.stage.raider

import click.seichi.petra.stage.spawn.SpawnProxy
import org.bukkit.World
import org.bukkit.entity.Creeper
import org.bukkit.entity.EntityType
import org.bukkit.util.Consumer

/**
 * @author tar0ss
 */
class CreeperEntity : StageEntity {

    private val entityType = EntityType.CREEPER

    override fun spawn(world: World, spawnProxy: SpawnProxy) {
        spawnProxy.spawn(world, entityType, Consumer {
            val creeper = it as Creeper
            creeper.removeWhenFarAway = false
        })
    }

    override fun calcNumSpawn(playerCount: Int): Int {
        return playerCount
    }
}