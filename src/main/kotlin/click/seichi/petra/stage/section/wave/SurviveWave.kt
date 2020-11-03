package click.seichi.petra.stage.section.wave

import org.bukkit.inventory.ItemStack

/**
 * @author tar0ss
 */
class SurviveWave(
        waveNum: Int,
        minutes: Int,
        raidData: WaveData,
        rewards: List<ItemStack> = listOf()
) : Wave(
        waveNum,
        minutes,
        raidData,
        rewards
)