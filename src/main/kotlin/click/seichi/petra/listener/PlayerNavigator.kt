package click.seichi.petra.listener

import click.seichi.game.IGame
import click.seichi.game.event.*
import click.seichi.petra.GameMessage
import click.seichi.petra.GameSound
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import java.util.*

/**
 * @author tar0ss
 */
class PlayerNavigator(
        game: IGame
) : Listener {
    private val players: Set<UUID> = game.players
    private val readyPlayers: Set<UUID> = game.readyPlayers
    private val bar = game.waveBossBar

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerJoinGame(event: PlayerJoinGameEvent) {
        val player = event.player
        player.setPlayerListName("${ChatColor.WHITE}${player.name}")
        GameMessage.READY(readyPlayers.count(), players.count()).broadcastTo { players.contains(it.uniqueId) }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerReady(event: PlayerReadyEvent) {
        val player = event.player
        player.setPlayerListName("${ChatColor.GREEN}${player.name}")
        GameMessage.CANCEL_READY(event.ready, event.all).broadcastTo { players.contains(it.uniqueId) }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerCancelReady(event: PlayerCancelReadyEvent) {
        val player = event.player
        player.setPlayerListName("${ChatColor.WHITE}${player.name}")
        GameMessage.READY(event.ready, event.all).broadcastTo { players.contains(it.uniqueId) }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onCountDown(event: CountDownEvent) {
        val remainSeconds = event.remainSeconds
        val count = event.count
        GameMessage.COUNT(event.remainSeconds).add(
                if (remainSeconds == count) GameSound.START_COUNT else GameSound.COUNT
        ).broadcastTo { players.contains(it.uniqueId) }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onCompletePreparation(event: PrepareEvent) {
        GameMessage.START_GAME.add(
                GameSound.START_GAME
        ).broadcastTo { players.contains(it.uniqueId) }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onBackPlayer(event: PlayerBackGameEvent) {
        bar.addPlayer(event.player)
    }
}