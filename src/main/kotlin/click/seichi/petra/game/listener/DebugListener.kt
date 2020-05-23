package click.seichi.petra.game.listener

import click.seichi.petra.event.CustomEvent
import click.seichi.petra.function.debug
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

/**
 * @author tar0ss
 */
class DebugListener : Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    fun onCustomEvent(event: CustomEvent) {
        debug("call ${event.eventName}")
    }
}