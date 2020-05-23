package click.seichi.petra.message

import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author unicroak
 * @author tar0ss
 *
 */
abstract class Message {

    var messages: MutableList<Message>? = null

    fun add(m: Message): Message {
        if (messages == null) {
            messages = mutableListOf()
        }
        messages!!.add(m)
        return this
    }

    fun sendTo(player: Player) {
        sendToSub(player)
        messages?.forEach { it.sendTo(player) }
    }

    protected abstract fun sendToSub(player: Player)

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