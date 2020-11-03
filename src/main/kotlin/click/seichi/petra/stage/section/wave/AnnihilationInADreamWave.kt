package click.seichi.petra.stage.section.wave

import click.seichi.petra.message.ChatMessage
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

/**
 * @author tar0ss
 */
class AnnihilationInADreamWave(
        waveNum: Int,
        minutes: Int,
        raidData: WaveData,
        rewards: List<ItemStack> = listOf()
) : AnnihilationWave(
        waveNum,
        minutes,
        raidData,
        rewards
) {
    val wakeUpPlayers = mutableSetOf<UUID>()

    override fun onStartRaid(summonData: SummonData) {
        super.onStartRaid(summonData)
        wakeUpPlayers.clear()
        players.mapNotNull { Bukkit.getServer().getPlayer(it) }
                .forEach {
                    it.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 10000, 5, true, true))
                }
        ChatMessage("${ChatColor.YELLOW}幸せな夢を見ている...").broadcast()
    }

    override fun onEnd() {
        PlayerBedEnterEvent.getHandlerList().unregister(this)
        PlayerPostRespawnEvent.getHandlerList().unregister(this)
        super.onEnd()
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onBedIn(event: PlayerBedEnterEvent) {
        val player = event.player
        val bed = event.bed
        event.setUseBed(Event.Result.DENY)
        val world = bed.world
        world.createExplosion(bed.location, 8F, false, true)
        player.health = 0.0
        wakeUpPlayers.add(player.uniqueId)
        ChatMessage("${ChatColor.AQUA}${player.name}が夢から醒めた").broadcast()
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPostRespawn(event: PlayerPostRespawnEvent) {
        val player = event.player
        if (wakeUpPlayers.contains(player.uniqueId)) return
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 10000, 5, true, true))
    }
}