package click.seichi.petra.listener

import click.seichi.game.IGameStarter
import click.seichi.petra.stage.Stage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

/**
 * @author tar0ss
 */
class GameActionListener(private val starter: IGameStarter, private val stage: Stage) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        event.isCancelled = when {
            // スタート前
            !starter.isStarted -> true
            // セーフゾーン以外
            !stage.isSafeZone(block.x, block.y, block.z) -> true
            else -> false
        }
    }
}