package click.seichi.petra.listener

import click.seichi.game.event.GameStartCountEvent
import click.seichi.game.event.PlayerCancelReadyEvent
import click.seichi.game.event.PlayerReadyEvent
import click.seichi.game.event.StartGameEvent
import click.seichi.message.TitleMessage
import click.seichi.util.Random
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
        updateMessage(event.ready, event.all)
    }

    @EventHandler
    fun onPlayerCancelReady(event: PlayerCancelReadyEvent) {
        val player = event.player
        player.setPlayerListName("${ChatColor.WHITE}${player.name}")
        updateMessage(event.ready, event.all)
    }

    private fun updateMessage(ready: Int, all: Int) {
        TitleMessage(
                "${ChatColor.WHITE}ゲームスタート $ready / $all",
                "${ChatColor.BLUE}/r で じゅんび",
                stay = Int.MAX_VALUE
        ).broadcastTo { players.contains(it) }
    }

    @EventHandler
    fun onGameStartCount(event: GameStartCountEvent) {
        val remainSeconds = event.remainSeconds
        TitleMessage(
                "${Random.nextChatColor()}$remainSeconds",
                "${ChatColor.RED}/r で キャンセル",
                fadeIn = 5,
                stay = 10,
                fadeOut = 10
        ).broadcastTo { players.contains(it) }
    }

    @EventHandler
    fun onStartGame(event: StartGameEvent) {
        TitleMessage(
                "${ChatColor.YELLOW}スタート",
                ""
        ).broadcastTo { players.contains(it) }
    }
}