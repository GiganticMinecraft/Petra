package click.seichi.petra.game.event

import click.seichi.petra.event.CustomEvent
import org.bukkit.event.HandlerList
import java.util.*

/**
 * @author tar0ss
 */
class PrepareEvent(val players: Set<UUID>) : CustomEvent() {
    companion object {
        @JvmStatic
        private val handler = HandlerList()

        @JvmStatic
        fun getHandlerList() = handler
    }

    override fun getHandlers() = handler
}