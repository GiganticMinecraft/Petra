package click.seichi.game.listener

import click.seichi.game.PlayerLocator
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * @author tar0ss
 */
class PlayerConnectionListener : Listener {

    private val locator = PlayerLocator

    @EventHandler
    fun onPlayerLogin(event: PlayerLoginEvent) {
        if (locator.isLeft(event.player)) {
            event.allow()
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage = locator.join(event.player)
    }

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        event.quitMessage = locator.leave(event.player)
    }
}