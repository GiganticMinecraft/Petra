package click.seichi.petra.game

import click.seichi.petra.game.event.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
class PlayerLocator(
        private val game: Game,
        private val preparator: Preparator
) : IPlayerLocator {
    private val isStarted
        get() = game.isStarted

    private val participantIds
        get() = game.players

    override fun join(player: Player): String? {
        if (!isStarted) {
            Bukkit.getPluginManager().callEvent(PlayerJoinGameEvent(player))
            return "${player.name} が入場しました"
        }

        return if (player.uniqueId in participantIds) {
            Bukkit.getPluginManager().callEvent(PlayerBackGameEvent(player))

            "${player.name} が再接続しました"
        } else {
            Bukkit.getPluginManager().callEvent(SpectatorJoinEvent(player))
            null
        }
    }

    override fun leave(player: Player): String? {
        if (!isStarted) {
            // キャンセル
            if (preparator.isReady(player)) {
                preparator.cancelReady(player)
            }

            Bukkit.getPluginManager().callEvent(PlayerQuitGameEvent(player))

            return "${player.name} が退場しました"
        }

        return if (player.uniqueId in participantIds) {
            Bukkit.getPluginManager().callEvent(PlayerQuitInGameEvent(player))

            "${player.name} が退場しました"
        } else {
            Bukkit.getPluginManager().callEvent(SpectatorQuitEvent(player))
            null // 観戦はメッセージなし　
        }
    }

}