package click.seichi.petra.stage.section.wave

import click.seichi.message.Message
import click.seichi.message.SoundMessage
import click.seichi.message.TitleMessage
import click.seichi.petra.stage.StageResult
import click.seichi.petra.stage.summoner.ISummoner
import click.seichi.petra.stage.summoner.Named
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import java.util.*

/**
 * @author tar0ss
 */
class DefeatSummonerWave(
        private val waveNum: Int,
        private val minutes: Int,
        raidData: WaveData,
        private val targetSummoner: ISummoner
) : Wave(
        waveNum,
        minutes,
        raidData
), Listener {

    private lateinit var targetSet: Set<UUID>
    private val targetName = (targetSummoner as Named).getName()
    private var defeatedNum = 0

    override fun onStart() {
        targetSet = targetSummoner.summon(world, summonProxy, players)
        entitySet.addAll(targetSet)
    }

    override fun getStartMessage(): Message {
        return TitleMessage(
                "${ChatColor.WHITE}Wave$waveNum ${ChatColor.RED}襲撃",
                "${ChatColor.AQUA}制限時間内に${targetName}を倒せ"
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
        if (defeatedNum == targetSet.size) subject.onNext(StageResult.WIN)
        subject.onNext(StageResult.OVER_THE_TIME_LIMIT)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDeath(event: EntityDeathEvent) {
        if (!isStarted) return
        if (targetSet.contains(event.entity.uniqueId)) {
            defeatedNum++
        }
    }
}