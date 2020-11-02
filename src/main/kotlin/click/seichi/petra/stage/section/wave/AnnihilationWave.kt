package click.seichi.petra.stage.section.wave

import click.seichi.petra.GameSound
import click.seichi.petra.TopBarType
import click.seichi.petra.message.Message
import click.seichi.petra.message.TitleMessage
import click.seichi.petra.stage.StageResult
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack

/**
 * @author tar0ss
 */
open class AnnihilationWave(
        private val waveNum: Int,
        minutes: Int,
        raidData: WaveData,
        rewards: List<ItemStack> = listOf()
) : Wave(
        waveNum,
        minutes,
        raidData,
        rewards
), Listener {

    private var defeatedNum: Int = 0

    private lateinit var enemyCountBar: BossBar

    override fun getStartMessage(): Message {
        return TitleMessage(
                "${ChatColor.WHITE}Wave$waveNum ${ChatColor.RED}襲撃",
                "${ChatColor.AQUA}制限時間内に全滅させよ"
        ).add(
                GameSound.START_WAVE
        )
    }

    override fun onStart() {
        enemyCountBar = topBar.get(TopBarType.ENEMY_COUNT)

        enemyCountBar.color = BarColor.GREEN
        enemyCountBar.style = BarStyle.SEGMENTED_20
        enemyCountBar.progress = 1.0
        updateCountBar(0)

        super.onStart()
    }

    private fun updateCountBar(remain: Int) {
        enemyCountBar.isVisible = entitySet.size != 0
        if (entitySet.size == 0) return
        val title = if (remain == 0 && !hasNextSpawn) "${ChatColor.YELLOW}クリア"
        else "${ChatColor.GREEN}残っている敵の数 ${remain}体"
        enemyCountBar.setTitle(title)

        enemyCountBar.progress = remain.toDouble() / entitySet.size.toDouble()
    }

    override fun onTimeUp() {
        if (defeatedNum == entitySet.size) subject.onNext(StageResult.WIN)
        else subject.onNext(StageResult.OVER_THE_TIME_LIMIT)
    }

    override fun onEnd() {
        enemyCountBar.isVisible = false
        EntityDeathEvent.getHandlerList().unregister(this)
        super.onEnd()
    }

    override fun onSummoned(entity: Entity) {
        super.onSummoned(entity)
        entity.isGlowing = true
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