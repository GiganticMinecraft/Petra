package click.seichi.petra.game

import click.seichi.petra.extension.register
import click.seichi.petra.message.ChatMessage
import click.seichi.petra.message.Message
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Monster
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.*

/**
 * @author tar0ss
 */
class ResultRecorder : Listener {

    lateinit var players: Set<UUID>

    val deathCountMap = mutableMapOf<UUID, Int>()
    val defeatCountMap = mutableMapOf<UUID, Int>()

    open fun broadcast() {
        broadcastRanking(deathCountMap, ChatMessage("${ChatColor.AQUA}${ChatColor.BOLD}死亡数ランキング")) { rank, count, name ->
            val color = if (rank == 1) ChatColor.YELLOW else ChatColor.WHITE
            ChatMessage("${color}${rank}位 $name ${count}回")
        }
        broadcastRanking(defeatCountMap, ChatMessage("${ChatColor.AQUA}${ChatColor.BOLD}撃退数ランキング")) { rank, count, name ->
            val color = if (rank == 1) ChatColor.YELLOW else ChatColor.WHITE
            ChatMessage("${color}${rank}位 $name ${count}体")
        }
    }

    private fun broadcastRanking(map: Map<UUID, Int>, title: Message, message: (Int, Int, String) -> Message) {
        title.broadcast()
        map.values.sortedByDescending { it }.toSet().forEachIndexed { index, count ->
            val rank = index + 1
            map.filter { it.value == count }.forEach { (uuid, count) ->
                val name = Bukkit.getServer().getPlayer(uuid)?.name ?: "no name"
                message(rank, count, name).broadcast()
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        if (!players.contains(player.uniqueId)) return
        deathCountMap[player.uniqueId] = deathCountMap.getOrDefault(player.uniqueId, 0) + 1
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        if (entity !is Monster) return
        val killer = entity.killer ?: return
        if (!players.contains(killer.uniqueId)) return
        defeatCountMap[killer.uniqueId] = deathCountMap.getOrDefault(killer.uniqueId, 0) + 1
    }


    fun start(players: Set<UUID>) {
        this.players = players.toSet()
        register()
    }

}