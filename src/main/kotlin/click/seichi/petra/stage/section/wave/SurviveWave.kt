package click.seichi.petra.stage.section.wave

import click.seichi.petra.GameSound
import click.seichi.petra.TopBarType
import click.seichi.petra.game.event.PlayerQuitInGameEvent
import click.seichi.petra.message.Message
import click.seichi.petra.message.TitleMessage
import click.seichi.petra.stage.StageResult
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * @author tar0ss
 */
class SurviveWave(
        waveNum: Int,
        minutes: Int,
        raidData: WaveData,
        rewards: List<ItemStack> = listOf()
) : Wave(
        waveNum,
        minutes,
        raidData,
        rewards
), Listener {
    private val deathPlayers = mutableSetOf<UUID>()

    private lateinit var deathCountBar: BossBar

    override fun getStartMessage(): Message {
        return TitleMessage(
                "${ChatColor.WHITE}Wave$waveNum ${ChatColor.RED}襲撃",
                "${ChatColor.AQUA}${minutes}分間耐え抜け"
        ).add(
                GameSound.START_WAVE
        )
    }

    override fun onStart() {

        val remainedPlayers = players.mapNotNull { Bukkit.getServer().getPlayer(it) }

        remainedPlayers.forEach {
            it.isGlowing = true
        }

        val remainedPlayerUUIDs = remainedPlayers.map { it.uniqueId }.toSet()
        players.toSet().filter { !remainedPlayerUUIDs.contains(it) }.forEach {
            deathPlayers.add(it)
        }

        deathCountBar = topBar.get(TopBarType.DEATH_COUNT)

        deathCountBar.color = BarColor.GREEN
        deathCountBar.style = BarStyle.SEGMENTED_20
        deathCountBar.progress = 1.0
        updateCountBar()

        super.onStart()
    }

    private fun updateCountBar() {
        val survived = players.size - deathPlayers.size
        val title = "${ChatColor.GREEN}生き残り ${survived}人"
        deathCountBar.setTitle(title)
        deathCountBar.isVisible = true

        deathCountBar.progress = 1.0 - (deathPlayers.size.toDouble() / players.size.toDouble())
    }

    override fun onTimeUp() {
        subject.onNext(StageResult.WIN)
    }

    override fun onEnd() {
        deathCountBar.isVisible = false
        PlayerDeathEvent.getHandlerList().unregister(this)
        PlayerRespawnEvent.getHandlerList().unregister(this)
        PlayerQuitInGameEvent.getHandlerList().unregister(this)
        super.onEnd()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDeath(event: PlayerDeathEvent) {
        deathPlayers.add(event.entity.uniqueId)
        updateCountBar()
        if (deathPlayers.size == players.size) {
            subject.onNext(StageResult.DEATH_ALL_PLAYERS)
        }
    }

    @EventHandler
    fun onLeave(event: PlayerQuitInGameEvent) {
        val uniqueId = event.player.uniqueId
        if (!players.contains(uniqueId)) return
        deathPlayers.add(uniqueId)
        updateCountBar()
        if (deathPlayers.size == players.size) {
            subject.onNext(StageResult.DEATH_ALL_PLAYERS)
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPostRespawn(event: PlayerRespawnEvent) {
        val uniqueId = event.player.uniqueId
        if (!deathPlayers.contains(uniqueId)) return
        event.player.isGlowing = false
    }
}