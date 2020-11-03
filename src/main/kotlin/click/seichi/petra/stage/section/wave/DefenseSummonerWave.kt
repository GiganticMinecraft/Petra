package click.seichi.petra.stage.section.wave

import click.seichi.petra.GameSound
import click.seichi.petra.TopBarType
import click.seichi.petra.function.getNearestPlayer
import click.seichi.petra.message.Message
import click.seichi.petra.message.TitleMessage
import click.seichi.petra.stage.StageResult
import click.seichi.petra.stage.summoner.ISummoner
import org.bukkit.Bukkit
import org.bukkit.ChatColor
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
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack

/**
 * @author tar0ss
 */
class DefenseSummonerWave(
        waveNum: Int,
        minutes: Int,
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
    private lateinit var enemyHpBar: BossBar
    private var maxHealth = Double.MAX_VALUE

    override fun onStart() {
        target = targetSummoner.summonOnly(world, summonProxy, players).first().let {
            Bukkit.getServer().getEntity(it) as Mob
        }
        target.target = target.getNearestPlayer(players)
        target.isGlowing = true
        entitySet.add(target.uniqueId)

        enemyHpBar = topBar.get(TopBarType.ENEMY_COUNT)
        enemyHpBar.color = BarColor.YELLOW
        enemyHpBar.style = BarStyle.SOLID
        updateHpBar(1.0)
        enemyHpBar.isVisible = true
        maxHealth = target.health
    }

    private fun updateHpBar(remain: Double) {
        val title = "${ChatColor.YELLOW}残りHP"
        enemyHpBar.setTitle(title)

        enemyHpBar.progress = remain
        enemyHpBar.isVisible = true
    }

    override fun onEnd() {
        enemyHpBar.isVisible = false
        EntityDeathEvent.getHandlerList().unregister(this)
        EntityDamageEvent.getHandlerList().unregister(this)
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
    fun onDeath(event: EntityDeathEvent) {
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
            updateHpBar(entity.health.coerceAtLeast(0.0) / maxHealth)
        }
    }
}

