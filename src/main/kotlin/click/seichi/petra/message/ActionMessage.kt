package click.seichi.petra.message

import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
class ActionMessage(private val message: String) : Message() {

    override fun sendToSub(player: Player) {
        player.sendActionBar(message)
    }

}