package click.seichi.petra.stage.section.wave

import click.seichi.petra.GameSound
import click.seichi.petra.TopBarType
import click.seichi.petra.message.Message
import click.seichi.petra.message.TitleMessage
import click.seichi.petra.stage.StageResult
import click.seichi.petra.stage.summoner.ISummoner
import click.seichi.petra.stage.summoner.Named
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack

/**
 * @author tar0ss
 */
class DefeatSummonerWithTNTWave(
        private val waveNum: Int,
        private val minutes: Int,
        raidData: WaveData,
        private val targetSummoner: ISummoner,
        rewards: List<ItemStack> = listOf()
) : Wave(
        waveNum,
        minutes,
        raidData,
        rewards
), Listener {

    private lateinit var target: Mob
    private val targetName = (targetSummoner as Named).getName()
    private var isDefeat = false

    private var maxHealth = Double.MAX_VALUE

    private lateinit var enemyHpBar: BossBar

    override fun onStart() {
        target = targetSummoner.summonOnly(world, summonProxy, players).first()
                .let { Bukkit.getServer().getEntity(it) as Mob }
        onSummoned(target)
        entitySet.add(target.uniqueId)

        enemyHpBar = topBar.get(TopBarType.ENEMY_COUNT)
        enemyHpBar.color = BarColor.PINK
        enemyHpBar.style = BarStyle.SOLID
        updateHpBar(1.0)
        enemyHpBar.isVisible = true
        maxHealth = target.health
    }

    private fun updateHpBar(remain: Double) {
        val title = if (remain == 0.0) "${ChatColor.WHITE}クリア"
        else "${ChatColor.LIGHT_PURPLE}残りHP"
        enemyHpBar.setTitle(title)

        enemyHpBar.progress = remain.coerceAtMost(1.0)
    }

    override fun getStartMessage(): Message {
        return TitleMessage(
                "${ChatColor.WHITE}Wave$waveNum ${ChatColor.RED}襲撃",
                "${ChatColor.AQUA}制限時間内に${targetName}を倒せ"
        ).add(
                GameSound.START_WAVE
        )
    }

    override fun onTimeUp() {
        if (isDefeat) subject.onNext(StageResult.WIN)
        subject.onNext(StageResult.OVER_THE_TIME_LIMIT)
    }

    override fun onEnd() {
        enemyHpBar.isVisible = false
        super.onEnd()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDeath(event: EntityDeathEvent) {
        if (!isStarted) return
        if (event.entity.uniqueId == target.uniqueId) {
            isDefeat = true
            updateHpBar(0.0)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDamage(event: EntityDamageEvent) {
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