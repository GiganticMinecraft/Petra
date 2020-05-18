package click.seichi.util

import click.seichi.function.createInvisibleBossBar
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
class TopBar {

    private val barMap = mutableMapOf<String, BossBar>()

    private val playerSet = mutableSetOf<Player>()

    fun register(key: String): BossBar {
        val bar = createInvisibleBossBar()
        playerSet.forEach { bar.addPlayer(it) }
        barMap[key] = bar
        return bar
    }

    fun removeBar(key: String) {
        val bar = findBar(key) ?: return
        bar.removeAll()
    }

    fun findBar(key: String) = barMap[key]

    fun addPlayer(player: Player) {
        playerSet.add(player)
        barMap.values.forEach { it.addPlayer(player) }
    }

    fun removePlayer(player: Player) {
        playerSet.remove(player)
        barMap.values.forEach { it.removePlayer(player) }
    }
}