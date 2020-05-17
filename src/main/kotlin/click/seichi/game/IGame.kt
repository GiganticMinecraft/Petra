package click.seichi.game

import org.bukkit.World
import java.util.*

/**
 * @author tar0ss
 */
interface IGame {
    val isStarted: Boolean
    val players: MutableSet<UUID>
    val readyPlayers: MutableSet<UUID>
    val world: World
}