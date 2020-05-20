package click.seichi.petra.stage.raider

import click.seichi.petra.stage.spawn.SpawnProxy
import org.bukkit.World
import java.util.*

/**
 * @author tar0ss
 */
interface Spawnable {
    fun spawn(world: World, spawnProxy: SpawnProxy, players: Set<UUID>): Set<UUID>
}