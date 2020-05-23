package click.seichi.petra.stage.section.wave

import click.seichi.message.Message
import click.seichi.message.TitleMessage
import click.seichi.petra.GameSound
import click.seichi.petra.TopBarType
import click.seichi.petra.stage.StageResult
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.attribute.Attribute
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
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

    private lateinit var playerHpBar: BossBar

    override fun onStart() {
        target = players.mapNotNull { Bukkit.getServer().getPlayer(it) }.first { it.isValid }
        target.isGlowing = true
        playerHpBar = topBar.get(TopBarType.ENEMY_COUNT)
        playerHpBar.color = BarColor.YELLOW
        playerHpBar.style = BarStyle.SOLID
        updateHpBar(target.health / (target.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 10.0))
        playerHpBar.isVisible = true
    }

    private fun updateHpBar(remain: Double) {
        val title = "${ChatColor.YELLOW}残りHP"

        playerHpBar.setTitle(title)

        playerHpBar.progress = remain
    }

    override fun onEnd() {
        playerHpBar.isVisible = false
        target.isGlowing = false
        super.onEnd()
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
                GameSound.START_WAVE
        )
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDeath(event: PlayerDeathEvent) {
        if (!isStarted) return
        if (event.entity.uniqueId == target.uniqueId) {
            subject.onNext(StageResult.DEATH_TARGET_ENTITY)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDamage(event: EntityDamageEvent) {
        if (!isStarted) return
        val entity = event.entity
        if (entity !is LivingEntity) return
        if (entity.uniqueId == target.uniqueId) {
            val remainHealth = entity.health - event.finalDamage
            updateHpBar(remainHealth.coerceAtLeast(0.0) / (entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value
                    ?: 10.0))
        }
    }
}