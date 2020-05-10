package click.seichi.game.listener

import click.seichi.game.event.PlayerCancelReadyEvent
import click.seichi.game.event.PlayerReadyEvent
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

/**
 * @author tar0ss
 */
class PlayerGameListener : Listener {
    @EventHandler
    fun onPlayerReady(event: PlayerReadyEvent) {
        val player = event.player
        player.setPlayerListName("${ChatColor.GREEN}${player.name}")

    }

    @EventHandler
    fun onPlayerCancelReady(event: PlayerCancelReadyEvent) {
        val player = event.player
        player.setPlayerListName("${ChatColor.WHITE}${player.name}")
    }
}