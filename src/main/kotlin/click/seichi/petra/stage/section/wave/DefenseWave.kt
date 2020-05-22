package click.seichi.petra.stage.section.wave

import click.seichi.message.Message
import click.seichi.message.SoundMessage
import click.seichi.message.TitleMessage
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob

/**
 * @author tar0ss
 */
class DefenseWave(
        private val waveNum: Int,
        private val minutes: Int,
        private val raidData: WaveData
) : Wave(
        waveNum,
        minutes,
        raidData
) {

    private lateinit var target: LivingEntity

    override fun onStart() {
        target = players.mapNotNull { Bukkit.getServer().getPlayer(it) }.first { it.isValid }
    }

    override fun updateBar(remainSeconds: Int) {
        val title = "${ChatColor.WHITE}Wave${waveNum} 残り時間 ${remainSeconds}秒"
        bar.setTitle(title)
        bar.progress = remainSeconds.toDouble() / seconds.toDouble()
    }

    override fun onSummoned(entity: Entity) {
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
}