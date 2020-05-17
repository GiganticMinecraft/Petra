package click.seichi.petra.stage.raider

import click.seichi.petra.stage.spawn.SpawnProxy
import org.bukkit.World

/**
 * @author tar0ss
 */
interface StageEntity {
    fun spawn(world: World, spawnProxy: SpawnProxy)
    fun calcNumSpawn(playerCount: Int): Int
}