package click.seichi.game

import click.seichi.game.event.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
class PlayerLocator(
        private val players: MutableSet<Player>,
        private val gameStarter: IGameStarter
) : IPlayerLocator {

    // 落ち戻り処理用
    private val leftPlayers = mutableSetOf<Player>()

    private val spectators = mutableSetOf<Player>()

    /**
     * @return ログインを例外的に許可する時 true
     */
    override fun isLeft(player: Player): Boolean {
        // ゲーム参加者なら例外として落ち戻り判定
        // 観戦者なら満員判定
        return leftPlayers.contains(player)
    }

    override fun join(player: Player): String? {
        if (gameStarter.isStarted) {
            if (leftPlayers.contains(player)) {
                players.add(player)
                Bukkit.getPluginManager().callEvent(PlayerBackGameEvent(player))
                return "${player.name} が再接続しました"
            } else {
                spectators.add(player)
                // 観戦はメッセージなし　
                Bukkit.getPluginManager().callEvent(SpectatorJoinEvent(player))
                return null
            }
        } else {
            players.add(player)
            Bukkit.getPluginManager().callEvent(PlayerJoinGameEvent(player))
            return "${player.name} が入場しました"
        }
    }

    override fun leave(player: Player): String? {
        if (gameStarter.isStarted) {
            // 落ち戻り待機
            if (players.contains(player)) {
                leftPlayers.add(player)
                players.remove(player)
                if (gameStarter.isReady(player)) {
                    gameStarter.cancelReady(player)
                }
                Bukkit.getPluginManager().callEvent(PlayerQuitInGameEvent(player))
                return "${player.name} が退場しました"
            } else {
                spectators.remove(player)
                Bukkit.getPluginManager().callEvent(SpectatorQuitEvent(player))
                // 観戦はメッセージなし　
                return null
            }
        } else {
            // キャンセル
            players.remove(player)
            Bukkit.getPluginManager().callEvent(PlayerQuitGameEvent(player))
            return "${player.name} が退場しました"
        }
    }

}