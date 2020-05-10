package click.seichi.game

import click.seichi.game.event.GameStartCountEvent
import click.seichi.game.event.PlayerCancelReadyEvent
import click.seichi.game.event.PlayerReadyEvent
import click.seichi.game.event.StartGameEvent
import click.seichi.util.Timer
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class SimpleGameStarter(
        private val players: Set<Player>,
        startCount: Int = 5
) : IGameStarter {

    private val readyPlayerSet = mutableSetOf<Player>()

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
        if (!players.contains(player)) return
        readyPlayerSet.add(player)

        Bukkit.getPluginManager().callEvent(PlayerReadyEvent(
                player,
                readyPlayerSet.count(),
                players.count()
        ))

        if (canStart) timer.start()
    }

    override fun cancelReady(player: Player) {
        readyPlayerSet.remove(player)
        if (timer.isStarted) {
            timer.cancel()
        }
        Bukkit.getPluginManager().callEvent(PlayerCancelReadyEvent(
                player,
                readyPlayerSet.count(),
                players.count()))
    }

    override fun isReady(player: Player): Boolean {
        return readyPlayerSet.contains(player)
    }

}
