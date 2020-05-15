package click.seichi.petra.listener

import click.seichi.game.IGame
import click.seichi.game.event.CountDownEvent
import click.seichi.game.event.PlayerCancelReadyEvent
import click.seichi.game.event.PrepareEvent
import click.seichi.petra.stage.Stage
import click.seichi.util.Timer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import java.util.*

/**
 * @author tar0ss
 */
class PetraGame(private val stage: Stage) : Listener, IGame {

    override var isStarted = false
        private set

    override val players = mutableSetOf<UUID>()

    private val count = 5

    private val timer = Timer(count,
            onNext = { remainSeconds ->
                if (remainSeconds == 0) start()
                else Bukkit.getPluginManager().callEvent(CountDownEvent(remainSeconds, count))
            })

    private fun start() {
        isStarted = true
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "recipe give @a[gamemode=survival] *")
        stage.waveController.start()
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        event.isCancelled = when {
            // 準備中
            !isStarted -> true
            // セーフゾーン以外
            !stage.isSafeZone(block.x, block.y, block.z) -> true
            else -> false
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPrepare(event: PrepareEvent) {
        timer.start()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onCancelReady(event: PlayerCancelReadyEvent) {
        if (timer.isStarted) timer.cancel()
    }
}