package click.seichi.petra.listener

import click.seichi.game.Preparator
import click.seichi.game.event.CompletePreparationEvent
import click.seichi.petra.stage.Stage
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

/**
 * @author tar0ss
 */
class GameListener(private val preparator: Preparator, private val stage: Stage) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        event.isCancelled = when {
            // 準備中
            !preparator.isCompleted -> true
            // セーフゾーン以外
            !stage.isSafeZone(block.x, block.y, block.z) -> true
            else -> false
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onCompletePreparation(event: CompletePreparationEvent) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "recipe give @a[gamemode=survival] *")
        stage.waveController.start()
    }
}