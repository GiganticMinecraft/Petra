package click.seichi.petra.stage.summon

import click.seichi.petra.util.Random
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.util.Consumer
import java.util.*

/**
 * @author tar0ss
 */
class RoundStageSummonProxy(
        private val radius: Int,
        private val dangerZoneLength: Int
) : SummonProxy {
    private val dangerZoneRadius = radius + dangerZoneLength

    override fun summonAtDangerZone(world: World, entityType: EntityType, function: Consumer<Entity>?): Entity {
        val xz = Random.nextDoughnutPoint(radius.toDouble(), (dangerZoneLength - 2).toDouble())
        val x = xz.first
        val z = xz.second
        val highestHeight = world.getHighestBlockYAt(x.toInt(), z.toInt())
        val loc = Location(world, x, (highestHeight + 2).toDouble(), z)
        return if (function == null) world.spawnEntity(loc, entityType)
        else world.spawn(loc, entityType.entityClass as Class<Entity>, function)
    }

    override fun summonAtSafeZone(world: World, entityType: EntityType, function: Consumer<Entity>?): Entity {
        val xz = Random.nextRoundPoint(radius.toDouble())
        val x = xz.first
        val z = xz.second
        val highestHeight = world.getHighestBlockYAt(x.toInt(), z.toInt())
        val loc = Location(world, x, (highestHeight + 2).toDouble(), z)
        return if (function == null) world.spawnEntity(loc, entityType)
        else world.spawn(loc, entityType.entityClass as Class<Entity>, function)
    }

    override fun summonNearPlayer(world: World, entityType: EntityType, players: Set<UUID>, function: Consumer<Entity>?): Entity {
        if (players.isEmpty()) {
            return summonAtDangerZone(world, entityType, function)
        }
        val diffX = Random.nextDouble(5.0, 7.0)
        val diffZ = Random.nextDouble(5.0, 7.0)
        val diffY = Random.nextDouble(1.0, 5.0) * (if (Random.nextBoolean()) -1 else 1)
        val target = players.mapNotNull { Bukkit.getServer().getPlayer(it) }.random()
        val loc = Location(world, target.location.x + diffX, target.location.y + diffY, target.location.z + diffZ)
        val entity = if (function == null) world.spawnEntity(loc, entityType)
        else world.spawn(loc, entityType.entityClass as Class<Entity>, function)

        if (entity is Mob) {
            entity.target = target
        }
        return entity
    }

    override fun summonToCenter(world: World, entityType: EntityType, function: Consumer<Entity>?): Entity {
        val x = 0.0
        val z = 0.0
        val highestHeight = world.getHighestBlockYAt(x.toInt(), z.toInt())
        val loc = Location(world, x, (highestHeight + 2).toDouble(), z)
        return if (function == null) world.spawnEntity(loc, entityType)
        else world.spawn(loc, entityType.entityClass as Class<Entity>, function)
    }
}