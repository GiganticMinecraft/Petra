package click.seichi.petra.game

import click.seichi.petra.game.event.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

/**
 * @author tar0ss
 */
class PlayerLocator(
        private val game: Game,
        private val preparator: Preparator
) : IPlayerLocator {
    private val isStarted
        get() = game.isStarted
    private val onlinePlayers = game.players

    private val participantIds = mutableSetOf<UUID>()
    private val spectatorIds = mutableSetOf<UUID>()

    /**
     * ログインを許可するかどうかを返す
     * 参加者であれば落ち戻り判定を行い, それ以外の場合は一切を許可しない.
     *
     * @return ログインを許可するかどうか
     */
    override fun allowsToJoin(player: Player): Boolean {
        return player.uniqueId in participantIds
    }

    override fun join(player: Player): String? {
        if (!isStarted) {
            onlinePlayers.add(player.uniqueId)
            participantIds.add(player.uniqueId)

            Bukkit.getPluginManager().callEvent(PlayerJoinGameEvent(player))

            return "${player.name} が入場しました"
        }

        return if (player.uniqueId in participantIds) {
            onlinePlayers.add(player.uniqueId)
            participantIds.add(player.uniqueId)

            Bukkit.getPluginManager().callEvent(PlayerBackGameEvent(player))

            "${player.name} が再接続しました"
        } else {
            spectatorIds.add(player.uniqueId)

            Bukkit.getPluginManager().callEvent(SpectatorJoinEvent(player))

            null
        }
    }

    override fun leave(player: Player): String? {
        if (!isStarted) {
            // キャンセル
            if (preparator.isReady(player) && !isStarted) {
                preparator.cancelReady(player)
            }
            onlinePlayers.remove(player.uniqueId)

            Bukkit.getPluginManager().callEvent(PlayerQuitGameEvent(player))

            return "${player.name} が退場しました"
        }

        return if (player.uniqueId in onlinePlayers) {
            // 落ち戻り待機
            participantIds.add(player.uniqueId)
            onlinePlayers.remove(player.uniqueId)

            Bukkit.getPluginManager().callEvent(PlayerQuitInGameEvent(player))

            "${player.name} が退場しました"
        } else {
            spectatorIds.remove(player.uniqueId)

            Bukkit.getPluginManager().callEvent(SpectatorQuitEvent(player))

            null // 観戦はメッセージなし　
        }
    }

}