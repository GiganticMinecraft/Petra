package click.seichi.petra

import click.seichi.petra.event.PlayerReadyEvent
import click.seichi.petra.event.StartGameEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object GameStarter {
    var isStarted = false
        private set

    private val readyPlayerSet = mutableSetOf<Player>()

    private fun start() {
        isStarted = true
        Bukkit.getPluginManager().callEvent(StartGameEvent())
    }

    private val playerSet = PlayerLocator.playerSet
    private val canStart
        get() = playerSet.size == readyPlayerSet.size

    fun ready(player: Player) {
        if (!playerSet.contains(player)) return
        readyPlayerSet.add(player)
        // TODO move to Listener
//        player.setPlayerListName("${ChatColor.GREEN}${player.name}")
        Bukkit.getPluginManager().callEvent(PlayerReadyEvent(player))

        if (canStart) start()
    }

    fun isReady(player: Player): Boolean {
        return readyPlayerSet.contains(player)
    }
}
