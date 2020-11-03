package click.seichi.petra.game.event

import click.seichi.petra.event.CustomEvent
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

/**
 * @author tar0ss
 */
class PlayerQuitGameEvent(val player: Player) : CustomEvent() {
    companion object {
        @JvmStatic
        private val handler = HandlerList()

        @JvmStatic
        fun getHandlerList() = handler
    }

    override fun getHandlers() = handler
}