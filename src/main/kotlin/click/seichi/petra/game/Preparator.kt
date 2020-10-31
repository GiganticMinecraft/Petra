package click.seichi.petra.game

import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
interface Preparator {
    fun prepare()
    fun canPrepare(): Boolean
    fun ready(player: Player)
    fun canReady(): Boolean
    fun cancelReady(player: Player)
    fun isReady(player: Player): Boolean
}