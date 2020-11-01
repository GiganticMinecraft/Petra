package click.seichi.petra.game

import click.seichi.petra.function.warning
import click.seichi.petra.game.event.CancelPrepareEvent
import click.seichi.petra.game.event.PlayerCancelReadyEvent
import click.seichi.petra.game.event.PlayerReadyEvent
import click.seichi.petra.game.event.PrepareEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class SimplePreparator(
        game: Game,
        private val playerRange: IntRange
) : Preparator {

    private val onlinePlayerCount: Int
        get() = Bukkit.getServer().onlinePlayers.count()
    private val readyPlayerSet = mutableSetOf<UUID>()
    private var isPrepared = false

    private val startableCount: Int
        get() {
            val readyCount = readyPlayerSet.count()
            return when {
                readyCount in playerRange -> readyCount
                readyCount < playerRange.first -> playerRange.first
                else -> playerRange.last
            }
        }

    override fun canPrepare(): Boolean {
        return (readyPlayerSet.count() in playerRange && readyPlayerSet.count() == onlinePlayerCount)
                || readyPlayerSet.count() == playerRange.last
    }

    override fun prepare() {
        if (isPrepared) {
            warning("Already complete preparation")
            return
        }
        isPrepared = true
        Bukkit.getPluginManager().callEvent(PrepareEvent(readyPlayerSet.toSet()))
    }

    override fun canReady(): Boolean {
        return readyPlayerSet.count() < playerRange.last
    }

    override fun ready(player: Player) {
        readyPlayerSet.add(player.uniqueId)

        val readyCount = readyPlayerSet.count()

        Bukkit.getPluginManager().callEvent(PlayerReadyEvent(
                player,
                readyCount,
                startableCount
        ))

        if (canPrepare()) prepare()
    }

    override fun cancelReady(player: Player) {
        if (isPrepared) {
            isPrepared = false
            Bukkit.getPluginManager().callEvent(CancelPrepareEvent())
        }
        readyPlayerSet.remove(player.uniqueId)
        Bukkit.getPluginManager().callEvent(PlayerCancelReadyEvent(
                player,
                readyPlayerSet.count(),
                startableCount))
    }

    override fun isReady(player: Player): Boolean {
        return readyPlayerSet.contains(player.uniqueId)
    }

}
