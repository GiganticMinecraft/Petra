package click.seichi.petra.listener

import click.seichi.game.IGameStarter
import click.seichi.game.event.StartGameEvent
import click.seichi.petra.stage.Stage
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

/**
 * @author tar0ss
 */
class GameListener(private val starter: IGameStarter, private val stage: Stage) : Listener {

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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onStartGame(event: StartGameEvent) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "recipe give @a[gamemode=survival] *")
        stage.waveController.start()
    }
}