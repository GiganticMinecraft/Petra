package click.seichi.petra.stage.section.wave

import click.seichi.message.Message
import click.seichi.message.SoundMessage
import click.seichi.message.TitleMessage
import click.seichi.petra.stage.StageResult
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

/**
 * @author tar0ss
 */
class AnnihilationWave(
        private val waveNum: Int,
        private val minutes: Int,
        raidData: WaveData
) : Wave(
        waveNum,
        minutes,
        raidData
), Listener {

    private var defeatedNum: Int = 0

    override fun getStartMessage(): Message {
        return TitleMessage(
                "${ChatColor.WHITE}Wave$waveNum ${ChatColor.RED}襲撃",
                "${ChatColor.AQUA}制限時間内に全滅させよ"
        ).add(
                SoundMessage(
                        Sound.ENTITY_ILLUSIONER_CAST_SPELL,
                        SoundCategory.BLOCKS,
                        2.0f,
                        0.3f
                )
        )
    }

    override fun onTimeUp() {
        subject.onNext(StageResult.OVER_THE_TIME_LIMIT)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDeath(event: EntityDeathEvent) {
        if (entitySet.contains(event.entity.uniqueId)) {
            defeatedNum++
        }
        if (!hasNextSpawn && defeatedNum == entitySet.size) {
            subject.onNext(StageResult.WIN)
        }
    }
}