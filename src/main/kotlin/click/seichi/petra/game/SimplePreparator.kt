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

    private val players: Set<UUID> = game.players
    private val readyPlayerSet = game.readyPlayers
    private var isPrepared = false

    override fun canPrepare(): Boolean {
        return (readyPlayerSet.count() in playerRange && readyPlayerSet.count() == players.count())
                || readyPlayerSet.count() == playerRange.last
    }

    override fun prepare() {
        if (isPrepared) {
            warning("Already complete preparation")
            return
        }
        isPrepared = true
        Bukkit.getPluginManager().callEvent(PrepareEvent(players))
    }

    override fun canReady(): Boolean {
        return players.count() < playerRange.last
    }

    override fun ready(player: Player) {
        if (!players.contains(player.uniqueId)) return
        readyPlayerSet.add(player.uniqueId)

        Bukkit.getPluginManager().callEvent(PlayerReadyEvent(
                player,
                readyPlayerSet.count(),
                players.count()
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
                players.count()))
    }

    override fun isReady(player: Player): Boolean {
        return readyPlayerSet.contains(player.uniqueId)
    }

}
