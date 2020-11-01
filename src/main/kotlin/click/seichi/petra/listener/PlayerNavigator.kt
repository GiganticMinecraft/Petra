package click.seichi.petra.listener

import click.seichi.petra.GameMessage
import click.seichi.petra.GameSound
import click.seichi.petra.event.StartGameEvent
import click.seichi.petra.game.event.CountDownEvent
import click.seichi.petra.game.event.PlayerCancelReadyEvent
import click.seichi.petra.game.event.PlayerJoinGameEvent
import click.seichi.petra.game.event.PlayerReadyEvent
import click.seichi.petra.util.TopBar
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * @author tar0ss
 */
class PlayerNavigator(
        private val bar: TopBar
) : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerJoinGame(event: PlayerJoinGameEvent) {
        val player = event.player
        player.setPlayerListName("${ChatColor.WHITE}${player.name}")
        GameMessage.JOIN.broadcast()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerReady(event: PlayerReadyEvent) {
        val player = event.player
        player.setPlayerListName("${ChatColor.GREEN}${player.name}")
        GameMessage.CANCEL_READY(event.ready, event.startableCount).broadcast()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerCancelReady(event: PlayerCancelReadyEvent) {
        val player = event.player
        player.setPlayerListName("${ChatColor.WHITE}${player.name}")
        GameMessage.READY(event.ready, event.all).broadcast()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onCountDown(event: CountDownEvent) {
        val remainSeconds = event.remainSeconds
        val count = event.count
        GameMessage.COUNT(event.remainSeconds).add(
                if (remainSeconds == count) GameSound.START_COUNT else GameSound.COUNT
        ).broadcast()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onBackPlayer(event: PlayerJoinEvent) {
        bar.addPlayer(event.player)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onQuitInGame(event: PlayerQuitEvent) {
        bar.removePlayer(event.player)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onStartGame(event: StartGameEvent) {
        GameMessage.START.add(GameSound.START_GAME).broadcast()
    }
}