package click.seichi.game.listener

import click.seichi.event.CustomEvent
import click.seichi.function.debug
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