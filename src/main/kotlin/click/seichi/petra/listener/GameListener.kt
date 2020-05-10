package click.seichi.petra.listener

import click.seichi.game.event.GameStartCountEvent
import click.seichi.game.event.PlayerCancelReadyEvent
import click.seichi.game.event.PlayerReadyEvent
import click.seichi.game.event.StartGameEvent
import click.seichi.petra.GameMessage
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

/**
 * @author tar0ss
 */
class GameListener(private val players: Set<Player>) : Listener {

    @EventHandler
    fun onPlayerReady(event: PlayerReadyEvent) {
        val player = event.player
        player.setPlayerListName("${ChatColor.GREEN}${player.name}")
        GameMessage.READY_GAME(event.ready, event.all).broadcastTo { players.contains(it) }
    }

    @EventHandler
    fun onPlayerCancelReady(event: PlayerCancelReadyEvent) {
        val player = event.player
        player.setPlayerListName("${ChatColor.WHITE}${player.name}")
        GameMessage.READY_GAME(event.ready, event.all).broadcastTo { players.contains(it) }
    }

    @EventHandler
    fun onGameStartCount(event: GameStartCountEvent) {
        GameMessage.START_COUNT(event.remainSeconds).broadcastTo { players.contains(it) }
    }

    @EventHandler
    fun onStartGame(event: StartGameEvent) {
        GameMessage.START_GAME.broadcastTo { players.contains(it) }
    }
}