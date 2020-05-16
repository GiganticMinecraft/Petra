package click.seichi.petra.stage.spawn

import click.seichi.util.Random
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.util.Consumer

/**
 * @author tar0ss
 */
class RoundStageSpawner(
        private val radius: Int,
        private val dangerZoneLength: Int
) : SpawnProxy {
    private val dangerZoneRadius = radius + dangerZoneLength

    override fun <T : Entity> spawn(world: World, clazz: Class<T>, function: Consumer<T>?) {
        val xz = Random.nextDoughnutPoint(radius.toDouble(), dangerZoneLength.toDouble())
        val x = xz.first
        val z = xz.second
        val highestHeight = world.getHighestBlockYAt(x.toInt(), z.toInt())
        val loc = Location(world, x, (highestHeight + 2).toDouble(), z)
        if (function == null) world.spawn(loc, clazz)
        else world.spawn(loc, clazz, function)
    }
}