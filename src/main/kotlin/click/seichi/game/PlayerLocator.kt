package click.seichi.game

import click.seichi.game.event.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

/**
 * @author tar0ss
 */
class PlayerLocator(
        private val game: IGame,
        private val preparator: Preparator
) : IPlayerLocator {

    private val isStarted
        get() = game.isStarted
    private val players = game.players

    // 落ち戻り処理用
    private val leftPlayers = mutableSetOf<UUID>()

    private val spectators = mutableSetOf<UUID>()

    /**
     * @return ログインを例外的に許可する時 true
     */
    override fun isLeft(player: Player): Boolean {
        // ゲーム参加者なら例外として落ち戻り判定
        // 観戦者なら満員判定
        return leftPlayers.contains(player.uniqueId)
    }

    override fun join(player: Player): String? {
        if (isStarted) {
            if (leftPlayers.contains(player.uniqueId)) {
                players.add(player.uniqueId)
                Bukkit.getPluginManager().callEvent(PlayerBackGameEvent(player))
                return "${player.name} が再接続しました"
            } else {
                spectators.add(player.uniqueId)
                // 観戦はメッセージなし　
                Bukkit.getPluginManager().callEvent(SpectatorJoinEvent(player))
                return null
            }
        } else {
            players.add(player.uniqueId)
            Bukkit.getPluginManager().callEvent(PlayerJoinGameEvent(player))
            return "${player.name} が入場しました"
        }
    }

    override fun leave(player: Player): String? {
        if (isStarted) {
            // 落ち戻り待機
            if (players.contains(player.uniqueId)) {
                leftPlayers.add(player.uniqueId)
                players.remove(player.uniqueId)
                if (preparator.isReady(player) && !isStarted) {
                    preparator.cancelReady(player)
                }
                Bukkit.getPluginManager().callEvent(PlayerQuitInGameEvent(player))
                return "${player.name} が退場しました"
            } else {
                spectators.remove(player.uniqueId)
                Bukkit.getPluginManager().callEvent(SpectatorQuitEvent(player))
                // 観戦はメッセージなし　
                return null
            }
        } else {
            // キャンセル
            players.remove(player.uniqueId)
            Bukkit.getPluginManager().callEvent(PlayerQuitGameEvent(player))
            return "${player.name} が退場しました"
        }
    }

}