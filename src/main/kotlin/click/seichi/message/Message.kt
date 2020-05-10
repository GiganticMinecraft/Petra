package click.seichi.message

import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author unicroak
 * @author tar0ss
 *
 */
interface Message {

    fun sendTo(player: Player)

    fun broadcast() {
        Bukkit.getServer().onlinePlayers
                .filterNotNull()
                .filter { it.isValid }
                .forEach { sendTo(it) }
    }

    fun broadcastTo(filter: (Player) -> Boolean) {
        Bukkit.getServer().onlinePlayers
                .filterNotNull()
                .filter { it.isValid }
                .filter(filter)
                .forEach { sendTo(it) }
    }

}