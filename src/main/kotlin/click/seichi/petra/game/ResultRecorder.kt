package click.seichi.petra.game

import click.seichi.petra.extension.register
import click.seichi.petra.function.debug
import click.seichi.petra.message.ChatMessage
import click.seichi.petra.message.Message
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Monster
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.*

/**
 * @author tar0ss
 */
class ResultRecorder : Listener {

    lateinit var players: Set<UUID>

    private val deathCountMap = mutableMapOf<UUID, Int>()
    private val defeatCountMap = mutableMapOf<UUID, Int>()
    private val breakCountMap = mutableMapOf<UUID, Int>()

    open fun broadcast() {
        broadcastRanking(deathCountMap, ChatMessage("${ChatColor.AQUA}${ChatColor.BOLD}死亡数ランキング")) { rank, count, name ->
            val color = if (rank == 1) ChatColor.YELLOW else ChatColor.WHITE
            ChatMessage("${color}${rank}位 $name ${count}回")
        }
        broadcastRanking(defeatCountMap, ChatMessage("${ChatColor.AQUA}${ChatColor.BOLD}撃退数ランキング")) { rank, count, name ->
            val color = if (rank == 1) ChatColor.YELLOW else ChatColor.WHITE
            ChatMessage("${color}${rank}位 $name ${count}体")
        }
        broadcastRanking(breakCountMap, ChatMessage("${ChatColor.AQUA}${ChatColor.BOLD}破壊数ランキング")) { rank, count, name ->
            val color = if (rank == 1) ChatColor.YELLOW else ChatColor.WHITE
            ChatMessage("${color}${rank}位 $name ${count}ブロック")
        }
    }

    private fun broadcastRanking(map: Map<UUID, Int>, title: Message, message: (Int, Int, String) -> Message) {
        title.broadcast()
        if (map.isEmpty()) debug("map is empty!!")
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
        deathCountMap[player.uniqueId] = deathCountMap.getOrDefault(player.uniqueId, 0) + 1
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        if (entity !is Monster) return
        val killer = entity.killer ?: return
        defeatCountMap[killer.uniqueId] = defeatCountMap.getOrDefault(killer.uniqueId, 0) + 1
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        breakCountMap[player.uniqueId] = breakCountMap.getOrDefault(player.uniqueId, 0) + 1
    }

    fun start(players: Set<UUID>) {
        this.players = players.toSet()
        register()
    }

}