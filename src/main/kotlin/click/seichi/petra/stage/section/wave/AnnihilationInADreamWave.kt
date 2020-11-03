package click.seichi.petra.stage.section.wave

import click.seichi.petra.function.sync
import click.seichi.petra.message.ChatMessage
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
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
        private val hintMinutes: Int,
        raidData: WaveData,
        rewards: List<ItemStack> = listOf()
) : AnnihilationWave(
        waveNum,
        minutes,
        raidData,
        rewards
) {
    val wakeUpPlayers = mutableSetOf<UUID>()

    val debufEffect = PotionEffect(PotionEffectType.BLINDNESS, 10000, 1, true, true)

    override fun onStart() {
        sync(hintMinutes * 60 * 20L) {
            players.filter { !wakeUpPlayers.contains(it) }.mapNotNull { Bukkit.getServer().getPlayer(it) }
                    .forEach {
                        ChatMessage("${ChatColor.YELLOW}【ヒント】" +
                                " ${ChatColor.WHITE} ベッドで寝る").sendTo(it)
                    }
        }
        super.onStart()
    }

    override fun onStartRaid(summonData: SummonData) {
        super.onStartRaid(summonData)
        wakeUpPlayers.clear()
        players.mapNotNull { Bukkit.getServer().getPlayer(it) }
                .forEach {
                    it.addPotionEffect(debufEffect)
                }
        ChatMessage("${ChatColor.YELLOW}幸せな夢を見ている...").broadcast()
    }

    override fun onEnd() {
        PlayerBedEnterEvent.getHandlerList().unregister(this)
        PlayerPostRespawnEvent.getHandlerList().unregister(this)
        PlayerItemConsumeEvent.getHandlerList().unregister(this)
        super.onEnd()
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onBedIn(event: PlayerBedEnterEvent) {
        val player = event.player
        val bed = event.bed
        event.setUseBed(Event.Result.DENY)
        val world = bed.world
        ChatMessage("${ChatColor.AQUA}${player.name}が夢から醒めた").broadcast()
        world.createExplosion(bed.location, 8F, false, false)
        player.health = 0.0
        wakeUpPlayers.add(player.uniqueId)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPostRespawn(event: PlayerPostRespawnEvent) {
        val player = event.player
        if (wakeUpPlayers.contains(player.uniqueId)) return
        player.addPotionEffect(debufEffect)
    }

//    @EventHandler
//    fun onDrink(event: PlayerItemConsumeEvent) {
//        val player = event.player
//        val item = event.item
//        if (item.type != Material.MILK_BUCKET) return
//        if (wakeUpPlayers.contains(player.uniqueId)) return
//        event.isCancelled = true
//    }
}