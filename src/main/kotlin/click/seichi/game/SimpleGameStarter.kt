package click.seichi.game

import click.seichi.game.event.GameStartCountEvent
import click.seichi.game.event.PlayerCancelReadyEvent
import click.seichi.game.event.PlayerReadyEvent
import click.seichi.game.event.StartGameEvent
import click.seichi.util.Timer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class SimpleGameStarter(
        private val players: Set<UUID>,
        private val readyPlayerSet: MutableSet<UUID>,
        startCount: Int = 5
) : IGameStarter {

    override var isStarted = false
        private set

    private val timer = Timer(startCount,
            onNext = { remainSeconds ->
                if (remainSeconds == 0) start()
                else Bukkit.getPluginManager().callEvent(GameStartCountEvent(remainSeconds, startCount))
            })

    override fun start() {
        isStarted = true
        Bukkit.getPluginManager().callEvent(StartGameEvent())
    }

    private val canStart: Boolean
        get() = players.count() == readyPlayerSet.count()

    override fun ready(player: Player) {
        if (!players.contains(player.uniqueId)) return
        readyPlayerSet.add(player.uniqueId)

        Bukkit.getPluginManager().callEvent(PlayerReadyEvent(
                player,
                readyPlayerSet.count(),
                players.count()
        ))

        if (canStart) timer.start()
    }

    override fun cancelReady(player: Player) {
        readyPlayerSet.remove(player.uniqueId)
        if (timer.isStarted) {
            timer.cancel()
        }
        Bukkit.getPluginManager().callEvent(PlayerCancelReadyEvent(
                player,
                readyPlayerSet.count(),
                players.count()))
    }

    override fun isReady(player: Player): Boolean {
        return readyPlayerSet.contains(player.uniqueId)
    }

}
