package click.seichi.petra.listener

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
class PlayerGameListener(private val players: Set<Player>) : Listener {

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
                "ゲームスタート $ready / $all",
                "/r で じゅんび",
                stay = Int.MAX_VALUE
        ).broadcastTo { players.contains(it) }
    }
}