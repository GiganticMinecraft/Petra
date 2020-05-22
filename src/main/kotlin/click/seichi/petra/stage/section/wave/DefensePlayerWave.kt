package click.seichi.petra.stage.section.wave

import click.seichi.message.Message
import click.seichi.message.SoundMessage
import click.seichi.message.TitleMessage
import click.seichi.petra.stage.StageResult
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

/**
 * @author tar0ss
 */
class DefensePlayerWave(
        private val waveNum: Int,
        private val minutes: Int,
        raidData: WaveData
) : Wave(
        waveNum,
        minutes,
        raidData
), Listener {

    private lateinit var target: LivingEntity

    override fun onStart() {
        target = players.mapNotNull { Bukkit.getServer().getPlayer(it) }.first { it.isValid }
    }

    override fun onSummoned(entity: Entity) {
        if (!target.isValid) return
        if (entity is Mob) {
            entity.target = target
        }
    }

    override fun getStartMessage(): Message {
        return TitleMessage(
                "${ChatColor.WHITE}Wave$waveNum ${ChatColor.RED}襲撃",
                "${ChatColor.AQUA}${minutes}分間${target.name.take(8)}を守れ"
        ).add(
                SoundMessage(
                        Sound.ENTITY_ILLUSIONER_CAST_SPELL,
                        SoundCategory.BLOCKS,
                        2.0f,
                        0.3f
                )
        )
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDeath(event: PlayerDeathEvent) {
        if (!isStarted) return
        if (event.entity.uniqueId == target.uniqueId) {
            subject.onNext(StageResult.DEATH_TARGET_ENTITY)
        }
    }
}