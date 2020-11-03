package click.seichi.petra.game

import click.seichi.petra.GameSound
import click.seichi.petra.config.PetraConfig
import click.seichi.petra.config.WinnerConfig
import click.seichi.petra.event.StartGameEvent
import click.seichi.petra.game.event.*
import click.seichi.petra.stage.Facilitator
import click.seichi.petra.stage.ResultSender
import click.seichi.petra.stage.Stage
import click.seichi.petra.stage.StageResult
import click.seichi.petra.util.Random
import click.seichi.petra.util.Timer
import click.seichi.petra.util.TopBar
import com.destroystokyo.paper.event.block.BlockDestroyEvent
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent
import io.reactivex.disposables.Disposable
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.world.TimeSkipEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

/**
 * @author tar0ss
 */
class PetraGame(private val stage: Stage) : Listener, Game {

    override var isStarted = false
        private set
    var isfinished = false
        private set

    override val players = mutableSetOf<UUID>()

    override val world: World by lazy { Bukkit.getServer().getWorld("world")!! }

    override val topBar: TopBar = TopBar().apply { this.init() }

    private val count = 5
    private lateinit var disposable: Disposable

    private val timer = Timer(count,
            onNext = { remainSeconds ->
                if (remainSeconds == 0) start()
                else Bukkit.getPluginManager().callEvent(CountDownEvent(remainSeconds, count))
            })


    private var readyPlayers = setOf<UUID>()

    private val resultRecorder = ResultRecorder()

    private fun start() {
        isStarted = true

        players.clear()
        players.addAll(readyPlayers)

        // 参加者以外はspectatorに
        val spectators = Bukkit.getServer().onlinePlayers.mapNotNull { it }
                .filter { !players.contains(it.uniqueId) }
                .toSet()
        spectators.forEach { it.gameMode = GameMode.SPECTATOR }

        ResultRecorder().start(players)

        world.time = stage.startTime

        Bukkit.getPluginManager().callEvent(StartGameEvent(players.toSet()))
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "recipe give @a *")
        disposable = Facilitator().start(this, stage)
                .endAsObservable()
                .take(1)
                .subscribe { result(it) }
    }

    private fun result(result: StageResult) {
        disposable.dispose()
        isfinished = true
        val playerList = players.mapNotNull { Bukkit.getServer().getPlayer(it) }
        playerList.forEach {
            it.teleport(stage.generator.getFixedSpawnLocation(world, Random.generator)!!)
            GameSound.TELEPORT.sendTo(it)
        }
        if (result == StageResult.WIN && PetraConfig.SAVE_WINNER) {
            WinnerConfig.addWinners(playerList)
        }

        resultRecorder.broadcast()

        ResultSender(30).start(result, this)
                .endAsObservable()
                .take(1)
                .subscribe { end() }
    }

    private fun end() {
        Bukkit.getOnlinePlayers().forEach { player ->
            player.kickPlayer("Thank you for playing!!")
        }
        Bukkit.getServer().shutdown()
    }

    private fun shouldChanged(loc: Location): Boolean {
        return when {
            // 準備中
            !isStarted -> false
            // ゲーム終了
            isfinished -> false
            // セーフゾーン以外
            !stage.generator.isSafeZone(loc.blockX, loc.blockY, loc.blockZ) -> false
            else -> true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        event.isCancelled = !shouldChanged(block.location)
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockDestroy(event: BlockDestroyEvent) {
        val block = event.block
        event.isCancelled = !shouldChanged(block.location)
    }

    @EventHandler(ignoreCancelled = true)
    fun onEntityChangeBlock(event: EntityChangeBlockEvent) {
        val block = event.block
        event.isCancelled = !shouldChanged(block.location)
    }

    @EventHandler(ignoreCancelled = true)
    fun onEntityExplode(event: EntityExplodeEvent) {
        val blockList = event.blockList()
        val iterator = blockList.iterator()
        while (iterator.hasNext()) {
            val block = iterator.next()
            if (!shouldChanged(block.location)) {
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
        readyPlayers = event.players
        timer.start()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onCancelPrepare(event: CancelPrepareEvent) {
        if (timer.isStarted) timer.cancel()
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        event.respawnLocation = stage.generator.getFixedSpawnLocation(player.world, Random.generator)!!


    }

    @EventHandler
    fun onPostRespawn(event: PlayerPostRespawnEvent) {
        event.player.addPotionEffects(
                mutableListOf(
                        PotionEffect(PotionEffectType.WEAKNESS, 10 * 20, 1, true, true),
                        PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * 20, 3, true, true),
                        PotionEffect(PotionEffectType.HUNGER, 30 * 20, 1, true, true)
                )
        )
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerJoinGame(event: PlayerJoinGameEvent) {
        val player = event.player
        player.gameMode = GameMode.SURVIVAL
        player.inventory.clear()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onSpectatorJoin(event: SpectatorJoinEvent) {
        val player = event.player
        player.gameMode = GameMode.SPECTATOR
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerQuitInGame(event: PlayerQuitInGameEvent) {
        if (players.mapNotNull { Bukkit.getServer().getPlayer(it) }.isEmpty()) {
            result(StageResult.DEATH_ALL_PLAYERS)
        }
    }
}