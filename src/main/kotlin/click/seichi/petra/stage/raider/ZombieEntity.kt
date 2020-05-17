package click.seichi.petra.stage.raider

import click.seichi.petra.stage.spawn.SpawnProxy
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Zombie
import org.bukkit.util.Consumer

/**
 * @author tar0ss
 */
class ZombieEntity(private val shouldBurnInDay: Boolean = false) : StageEntity {

    private val entityType = EntityType.ZOMBIE

    override fun spawn(world: World, spawnProxy: SpawnProxy) {
        spawnProxy.spawn(world, entityType, Consumer {
            val zombie = it as Zombie
            zombie.removeWhenFarAway = false
            zombie.setShouldBurnInDay(shouldBurnInDay)
        })
    }

    override fun calcNumSpawn(playerCount: Int): Int {
        return playerCount
    }
}