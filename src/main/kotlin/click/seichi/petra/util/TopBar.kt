package click.seichi.petra.util

import click.seichi.petra.TopBarType
import click.seichi.petra.function.createInvisibleBossBar
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
class TopBar {

    private val barMap = mutableMapOf<TopBarType, BossBar>()

    private val playerSet = mutableSetOf<Player>()

    fun init() {
        TopBarType.values().toList().sortedBy { it.priority }.forEach {
            barMap[it] = createInvisibleBossBar()
        }
    }

    fun get(type: TopBarType) = barMap[type]!!

    fun addPlayer(player: Player) {
        playerSet.add(player)
        barMap.values.forEach { it.addPlayer(player) }
    }

    fun removePlayer(player: Player) {
        playerSet.remove(player)
        barMap.values.forEach { it.removePlayer(player) }
    }
}