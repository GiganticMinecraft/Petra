package click.seichi.petra.listener

import click.seichi.game.IGameStarter
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

/**
 * @author tar0ss
 */
class GameActionListener(val starter: IGameStarter) : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        // スタート前の採掘をキャンセル
        if (!starter.isStarted) {
            event.isCancelled = true
        }
    }
}