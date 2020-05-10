package click.seichi.petra.listener

import click.seichi.game.event.*
import click.seichi.petra.GameMessage
import click.seichi.petra.GameSound
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

/**
 * @author tar0ss
 */
class GameListener(
        private val players: Set<Player>,
        private val readyPlayers: Set<Player>
) : Listener {

    @EventHandler
    fun onPlayerJoinGame(event: PlayerJoinGameEvent) {
        val player = event.player
        player.setPlayerListName("${ChatColor.WHITE}${player.name}")
        GameMessage.READY(readyPlayers.count(), players.count()).broadcastTo { players.contains(it) }
    }

    @EventHandler
    fun onPlayerReady(event: PlayerReadyEvent) {
        val player = event.player
        player.setPlayerListName("${ChatColor.GREEN}${player.name}")
        GameMessage.CANCEL_READY(event.ready, event.all).broadcastTo { players.contains(it) }
    }

    @EventHandler
    fun onPlayerCancelReady(event: PlayerCancelReadyEvent) {
        val player = event.player
        player.setPlayerListName("${ChatColor.WHITE}${player.name}")
        GameMessage.READY(event.ready, event.all).broadcastTo { players.contains(it) }
    }

    @EventHandler
    fun onGameStartCount(event: GameStartCountEvent) {
        val remainSeconds = event.remainSeconds
        val count = event.count
        GameMessage.START_COUNT(event.remainSeconds).broadcastTo { players.contains(it) }
        val soundPlayer = if (remainSeconds == count) GameSound.START_COUNT else GameSound.COUNT
        soundPlayer.broadcastTo { players.contains(it) }
    }

    @EventHandler
    fun onStartGame(event: StartGameEvent) {
        GameMessage.START_GAME.broadcastTo { players.contains(it) }
        GameSound.START_GAME.broadcastTo { players.contains(it) }
    }
}