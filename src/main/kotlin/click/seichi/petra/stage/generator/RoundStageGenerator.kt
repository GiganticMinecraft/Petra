package click.seichi.petra.stage.generator

import click.seichi.petra.stage.StageGenerator
import org.bukkit.Location
import org.bukkit.World
import java.util.*
import kotlin.math.sqrt


/**
 * @author tar0ss
 */
abstract class RoundStageGenerator(
        // セーフゾーンの半径
        protected val radius: Int,
        // セーフゾーンからデンジャーゾーン端までの距離
        dangerZoneLength: Int
) : StageGenerator() {
    protected val dangerZoneRadius = radius + dangerZoneLength

    override fun isDangerZone(globalX: Int, globalY: Int, globalZ: Int): Boolean {
        val distance = sqrt((globalX * globalX + globalZ * globalZ).toDouble())
        return distance in (radius + 1).toDouble()..dangerZoneRadius.toDouble()
    }

    override fun isSafeZone(globalX: Int, globalY: Int, globalZ: Int): Boolean {
        val distance = sqrt((globalX * globalX + globalZ * globalZ).toDouble())
        return distance in 0.0..radius.toDouble()
    }

    override fun getFixedSpawnLocation(world: World, random: Random): Location? {
        val radius = random.nextDouble() * radius
        val x = random.nextDouble() * 15
        val z = sqrt(radius * radius + x * x)
        val highestHeight = world.getHighestBlockYAt(x.toInt(), z.toInt())
        return Location(world, x, (highestHeight + 2).toDouble(), z)
    }
}