package click.seichi.util

import click.seichi.function.createInvisibleBossBar
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import java.util.*

/**
 * @author tar0ss
 */
class TopBar {

    private val barMap = mutableMapOf<String, BossBar>()

    private val playerSet = mutableSetOf<Player>()

    private val barStack = Stack<BossBar>()

    fun register(key: String): BossBar {
        return if (barStack.isNotEmpty()) {
            barStack.pop()
        } else {
            createInvisibleBossBar().apply {
                playerSet.forEach { this.addPlayer(it) }
                barMap[key] = this
            }
        }
    }

    fun removeBar(key: String) {
        val bar = findBar(key) ?: return
        bar.isVisible = false
        barStack.push(bar)
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