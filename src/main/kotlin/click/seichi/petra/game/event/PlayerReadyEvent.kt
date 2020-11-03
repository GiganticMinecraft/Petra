package click.seichi.petra.game.event

import click.seichi.petra.event.CustomEvent
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

/**
 * @author tar0ss
 */
class PlayerReadyEvent(
        val player: Player,
        val ready: Int,
        val startableCount: Int
) : CustomEvent() {
    companion object {
        @JvmStatic
        private val handler = HandlerList()

        @JvmStatic
        fun getHandlerList() = handler
    }

    override fun getHandlers() = handler
}