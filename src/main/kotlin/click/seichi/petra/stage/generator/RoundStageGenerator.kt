package click.seichi.petra.stage.generator

import click.seichi.petra.util.Random
import org.bukkit.Location
import org.bukkit.World
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

    override fun isStageZone(globalX: Int, globalY: Int, globalZ: Int): Boolean {
        val distance = sqrt((globalX * globalX + globalZ * globalZ).toDouble())
        return distance <= (dangerZoneRadius.toDouble() + 1.0)
    }

    override fun getFixedSpawnLocation(world: World, random: java.util.Random): Location? {
        val xz = Random.nextRoundPoint(radius.toDouble())
        val x = xz.first
        val z = xz.second
        val highestHeight = world.getHighestBlockYAt(x.toInt(), z.toInt())
        return Location(world, x, (highestHeight + 2).toDouble(), z)
    }
}