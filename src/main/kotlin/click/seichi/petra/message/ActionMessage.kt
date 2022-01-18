package click.seichi.petra.message

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
class ActionMessage(private val message: String) : Message() {

    override fun sendToSub(player: Player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(message))
    }

}