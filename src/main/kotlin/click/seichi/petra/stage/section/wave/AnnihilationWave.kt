package click.seichi.petra.stage.section.wave

import click.seichi.message.Message
import click.seichi.message.SoundMessage
import click.seichi.message.TitleMessage
import click.seichi.petra.TopBarConstants
import click.seichi.petra.stage.StageResult
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

/**
 * @author tar0ss
 */
class AnnihilationWave(
        private val waveNum: Int,
        minutes: Int,
        raidData: WaveData
) : Wave(
        waveNum,
        minutes,
        raidData
), Listener {

    private var defeatedNum: Int = 0

    private lateinit var enemyCountBar: BossBar

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

    override fun onStart() {
        enemyCountBar = topBar.register(TopBarConstants.ENEMY_COUNT)

        enemyCountBar.color = BarColor.GREEN
        enemyCountBar.style = BarStyle.SEGMENTED_20
        enemyCountBar.isVisible = true
        updateCountBar(0)

        super.onStart()
    }

    private fun updateCountBar(remain: Int) {
        val title = "${ChatColor.GREEN}残っている敵の数 ${remain}体"
        enemyCountBar.setTitle(title)
        enemyCountBar.isVisible = entitySet.size != 0
        if (entitySet.size == 0) return

        enemyCountBar.progress = 1.0 - (remain.toDouble() / entitySet.size.toDouble())
    }

    override fun onTimeUp() {
        topBar.removeBar(TopBarConstants.ENEMY_COUNT)
        if (defeatedNum == entitySet.size) subject.onNext(StageResult.WIN)
        else subject.onNext(StageResult.OVER_THE_TIME_LIMIT)
    }

    override fun onSummoned(entity: Entity) {
        super.onSummoned(entity)
        updateCountBar(entitySet.size - defeatedNum)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDeath(event: EntityDeathEvent) {
        if (!isStarted) return
        if (entitySet.contains(event.entity.uniqueId)) {
            defeatedNum++
            updateCountBar(entitySet.size - defeatedNum)
        }
    }
}