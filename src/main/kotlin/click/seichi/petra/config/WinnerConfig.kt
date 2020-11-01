package click.seichi.petra.config

import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
object WinnerConfig : Config("winner") {
    val WINNER_NAME_SET by lazy { getStringList("name").toMutableSet() }
    val WINNER_UUID_SET by lazy { getStringList("uuid").toMutableSet() }


    fun addWinners(winners: List<Player>) {
        val winnerNames = winners.map { it.name }.toSet()
        val winnerUUIDs = winners.map { it.uniqueId.toString() }.toList()
        WINNER_NAME_SET.addAll(winnerNames)
        WINNER_UUID_SET.addAll(winnerUUIDs)
        set("name", WINNER_NAME_SET.toList())
        set("uuid", WINNER_UUID_SET.toList())

        save()
    }
}