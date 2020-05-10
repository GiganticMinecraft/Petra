package click.seichi.petra.listener

import click.seichi.game.event.GameStartCountEvent
import click.seichi.game.event.PlayerCancelReadyEvent
import click.seichi.game.event.PlayerReadyEvent
import click.seichi.message.TitleMessage
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
        updateStartCount(event.remainSeconds)
    }

    private fun updateStartCount(remainSeconds: Int) {
        TitleMessage(
                "${ChatColor.YELLOW}$remainSeconds",
                "${ChatColor.RED}/r で キャンセル"
        ).broadcastTo { players.contains(it) }
    }
}