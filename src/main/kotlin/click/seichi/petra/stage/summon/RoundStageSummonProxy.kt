package click.seichi.petra.stage.summon

import click.seichi.util.Random
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.util.Consumer

/**
 * @author tar0ss
 */
class RoundStageSummonProxy(
        private val radius: Int,
        private val dangerZoneLength: Int
) : SummonProxy {
    private val dangerZoneRadius = radius + dangerZoneLength

    override fun summon(world: World, entityType: EntityType, function: Consumer<Entity>?): Entity {
        val xz = Random.nextDoughnutPoint(radius.toDouble(), (dangerZoneLength - 2).toDouble())
        val x = xz.first
        val z = xz.second
        val highestHeight = world.getHighestBlockYAt(x.toInt(), z.toInt())
        val loc = Location(world, x, (highestHeight + 2).toDouble(), z)
        return if (function == null) world.spawnEntity(loc, entityType)
        else world.spawn(loc, entityType.entityClass as Class<Entity>, function)
    }
}