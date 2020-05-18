package click.seichi.petra.listener

import click.seichi.function.createInvisibleBossBar
import click.seichi.game.IGame
import click.seichi.game.event.CountDownEvent
import click.seichi.game.event.PlayerCancelReadyEvent
import click.seichi.game.event.PrepareEvent
import click.seichi.petra.stage.Stage
import click.seichi.petra.stage.Waver
import click.seichi.util.Random
import click.seichi.util.Timer
import com.destroystokyo.paper.event.block.BlockDestroyEvent
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.World
import org.bukkit.boss.BossBar
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.world.TimeSkipEvent
import java.util.*

/**
 * @author tar0ss
 */
class PetraGame(private val stage: Stage) : Listener, IGame {

    override var isStarted = false
        private set

    override val players = mutableSetOf<UUID>()

    override val readyPlayers = mutableSetOf<UUID>()

    override val world: World by lazy { Bukkit.getServer().getWorld("world")!! }

    override val waveBossBar: BossBar by lazy { createInvisibleBossBar() }

    private val count = 5

    private val timer = Timer(count,
            onNext = { remainSeconds ->
                if (remainSeconds == 0) start()
                else Bukkit.getPluginManager().callEvent(CountDownEvent(remainSeconds, count))
            })

    private fun start() {
        players.mapNotNull { Bukkit.getServer().getPlayer(it) }
                .forEach { waveBossBar.addPlayer(it) }
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true)
        isStarted = true
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "recipe give @a[gamemode=survival] *")
        Waver().start(this, stage)
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        event.isCancelled = when {
            // 準備中
            !isStarted -> true
            // セーフゾーン以外
            !stage.generator.isSafeZone(block.x, block.y, block.z) -> true
            else -> false
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockDestroy(event: BlockDestroyEvent) {
        val block = event.block
        event.isCancelled = !stage.generator.isSafeZone(block.x, block.y, block.z)
    }

    @EventHandler(ignoreCancelled = true)
    fun onEntityChangeBlock(event: EntityChangeBlockEvent) {
        val block = event.block
        event.isCancelled = !stage.generator.isSafeZone(block.x, block.y, block.z)
    }

    @EventHandler(ignoreCancelled = true)
    fun onEntityExplode(event: EntityExplodeEvent) {
        val blockList = event.blockList()
        val iterator = blockList.iterator()
        while (iterator.hasNext()) {
            val block = iterator.next()
            if (!stage.generator.isSafeZone(block.x, block.y, block.z)) {
                iterator.remove()
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onBed(event: TimeSkipEvent) {
        if (event.skipReason == TimeSkipEvent.SkipReason.NIGHT_SKIP) {
            event.isCancelled = true
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

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        event.respawnLocation = stage.generator.getFixedSpawnLocation(player.world, Random.generator)!!
    }
}