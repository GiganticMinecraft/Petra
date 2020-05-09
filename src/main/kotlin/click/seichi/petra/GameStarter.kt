package click.seichi.petra

import org.bukkit.entity.Player

object GameStarter {
    var isStarted = false
        private set

    private val readyPlayerSet = mutableSetOf<Player>()

    private fun start() {
        isStarted = true
    }

    private val playerSet = PlayerLocator.playerSet
    private val canStart
        get() = playerSet.size == readyPlayerSet.size

    fun ready(player: Player) {
        if (!playerSet.contains(player)) return
        readyPlayerSet.add(player)

        if (canStart) start()
    }

    fun isReady(player: Player): Boolean {
        return readyPlayerSet.contains(player)
    }
}
