package click.seichi.game.listener

import click.seichi.game.IPlayerLocator
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * @author tar0ss
 */
class PlayerConnectionListener(
        private val locator: IPlayerLocator
) : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerLogin(event: PlayerLoginEvent) {
        if (locator.isLeft(event.player)) {
            event.allow()
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage = locator.join(event.player)
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerLeave(event: PlayerQuitEvent) {
        event.quitMessage = locator.leave(event.player)
    }
}