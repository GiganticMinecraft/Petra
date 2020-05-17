package click.seichi.game

import org.bukkit.World
import org.bukkit.boss.BossBar
import java.util.*

/**
 * @author tar0ss
 */
interface IGame {
    val isStarted: Boolean
    val players: MutableSet<UUID>
    val readyPlayers: MutableSet<UUID>
    val world: World
    val waveBossBar: BossBar
}