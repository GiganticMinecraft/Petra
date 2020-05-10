package click.seichi.message

import org.bukkit.entity.Player

/**
 * @author unicroak
 * @author tar0ss
 */
class ChatMessage(private val message: String) : Message {

    override fun sendTo(player: Player) {
        player.sendMessage(message)
    }

}