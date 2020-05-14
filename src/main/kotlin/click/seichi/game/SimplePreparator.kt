package click.seichi.game

import click.seichi.function.warning
import click.seichi.game.event.CompletePreparationCountDownEvent
import click.seichi.game.event.CompletePreparationEvent
import click.seichi.game.event.PlayerCancelReadyEvent
import click.seichi.game.event.PlayerReadyEvent
import click.seichi.util.Timer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class SimplePreparator(
        private val players: Set<UUID>,
        private val readyPlayerSet: MutableSet<UUID>,
        count: Int = 5
) : Preparator {

    override var isCompleted = false
        private set

    private val timer = Timer(count,
            onNext = { remainSeconds ->
                if (remainSeconds == 0) complete()
                else Bukkit.getPluginManager().callEvent(CompletePreparationCountDownEvent(remainSeconds, count))
            })

    private fun complete() {
        if (isCompleted) {
            warning("Already complete preparation")
            return
        }
        isCompleted = true
        Bukkit.getPluginManager().callEvent(CompletePreparationEvent(players))
    }

    private val canComplete: Boolean
        get() = !isCompleted && players.count() == readyPlayerSet.count()

    override fun ready(player: Player) {
        if (!players.contains(player.uniqueId)) return
        readyPlayerSet.add(player.uniqueId)

        Bukkit.getPluginManager().callEvent(PlayerReadyEvent(
                player,
                readyPlayerSet.count(),
                players.count()
        ))

        if (canComplete) timer.start()
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
