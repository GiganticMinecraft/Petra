package click.seichi.petra.message

import org.bukkit.entity.Player

/**
 * @author unicroak
 * @author tar0ss
 */
class ChatMessage(private val message: String) : Message() {

    override fun sendToSub(player: Player) {
        player.sendMessage(message)
    }

}