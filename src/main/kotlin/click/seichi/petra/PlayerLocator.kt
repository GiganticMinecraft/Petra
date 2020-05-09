package click.seichi.petra

import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
object PlayerLocator {

    val isStarted: Boolean
        get() = GameStarter.isStarted

    // 参加者
    private val players = mutableSetOf<Player>()
    val playerSet: Set<Player> = players

    // 落ち戻り処理用
    private val leftPlayers = mutableSetOf<Player>()

    private val spectators = mutableSetOf<Player>()
    val spectatorSet: Set<Player> = spectators

    /**
     * @return ログインを例外的に許可する時 true
     */
    fun isLeft(player: Player): Boolean {
        // ゲーム参加者なら例外として落ち戻り判定
        // 観戦者なら満員判定
        return leftPlayers.contains(player)
    }

    fun join(player: Player): String? {
        if (isStarted) {
            if (leftPlayers.contains(player)) {
                players.add(player)
                return "${player.name} が再接続しました"
            } else {
                spectators.add(player)
                // 観戦はメッセージなし　
                return null
            }
        } else {
            players.add(player)
            return "${player.name} が入場しました"
        }
    }

    fun leave(player: Player): String? {
        if (isStarted) {
            // 落ち戻り待機
            if (players.contains(player)) {
                leftPlayers.add(player)
                players.remove(player)
                return "${player.name} が退場しました"
            } else {
                spectators.remove(player)
                // 観戦はメッセージなし　
                return null
            }
        } else {
            // キャンセル
            players.remove(player)
            return "${player.name} が退場しました"
        }
    }


}