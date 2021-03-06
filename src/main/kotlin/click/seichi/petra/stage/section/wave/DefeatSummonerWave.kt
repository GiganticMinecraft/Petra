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
import org.spigotmc.event.entity.EntityMountEvent

/**
 * @author tar0ss
 */
open class DefeatSummonerWave(
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

    protected lateinit var target: Mob
    protected val targetName = (targetSummoner as Named).getName()
    protected var isDefeat = false

    protected var maxHealth = Double.MAX_VALUE

    private lateinit var enemyHpBar: BossBar

    override fun onStart() {
        target = targetSummoner.summonOnly(world, summonProxy, players).first()
                .let { Bukkit.getServer().getEntity(it) as Mob }
        target.isGlowing = true
        onSummoned(target)
        entitySet.add(target.uniqueId)

        enemyHpBar = topBar.get(TopBarType.ENEMY_COUNT)
        enemyHpBar.color = BarColor.PINK
        enemyHpBar.style = BarStyle.SOLID
        updateHpBar(1.0)
        enemyHpBar.isVisible = true
        maxHealth = target.health
    }

    protected fun updateHpBar(remain: Double) {
        val title = if (remain == 0.0) "${ChatColor.WHITE}クリア"
        else "${ChatColor.LIGHT_PURPLE}残りHP"
        enemyHpBar.setTitle(title)

        enemyHpBar.progress = remain.coerceAtMost(1.0)
        enemyHpBar.isVisible = true
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
        EntityDeathEvent.getHandlerList().unregister(this)
        EntityDamageEvent.getHandlerList().unregister(this)
        EntityMountEvent.getHandlerList().unregister(this)
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
    open fun onDamage(event: EntityDamageEvent) {
        if (!isStarted) return
        val entity = event.entity
        if (entity !is LivingEntity) return
        if (entity.uniqueId == target.uniqueId) {
            updateHpBar(entity.health.coerceAtLeast(0.0) / maxHealth)
        }
    }

    @EventHandler
    fun onMount(event: EntityMountEvent) {
        event.isCancelled = true
    }
}