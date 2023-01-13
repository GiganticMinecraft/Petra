package click.seichi.petra.function

import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*

/**
 * @author tar0ss
 */

fun Entity.getNearestPlayer(players: Set<UUID>): Player? {
    return players.mapNotNull { Bukkit.getServer().getPlayer(it) }
        .map { it to it.location.distanceSquared(this.location) }
        .minByOrNull { it.second }?.first
}