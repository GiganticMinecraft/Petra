package click.seichi.petra.stage.section.wave

import click.seichi.petra.GameSound
import click.seichi.petra.message.Message
import click.seichi.petra.message.TitleMessage
import click.seichi.petra.stage.summoner.ISummoner
import org.bukkit.ChatColor
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack

/**
 * @author tar0ss
 */
class DefeatSummonerWithTNTWave(
        waveNum: Int,
        minutes: Int,
        raidData: WaveData,
        targetSummoner: ISummoner,
        rewards: List<ItemStack> = listOf()
) : DefeatSummonerWave(
        waveNum,
        minutes,
        raidData,
        targetSummoner,
        rewards
), Listener {

    override fun getStartMessage(): Message {
        return TitleMessage(
                "${ChatColor.WHITE}Wave$waveNum ${ChatColor.RED}襲撃",
                "${ChatColor.AQUA}制限時間内に${targetName}を倒せ"
        ).add(
                GameSound.START_WAVE
        )
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    override fun onDamage(event: EntityDamageEvent) {
        if (!isStarted) return
        val entity = event.entity
        if (entity !is LivingEntity) return
        if (entity.uniqueId != target.uniqueId) return
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            GameSound.KIIN.sendAt(entity.location)
            event.isCancelled = true
            return
        }
        updateHpBar(entity.health.coerceAtLeast(0.0) / maxHealth)
    }
}