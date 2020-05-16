package click.seichi.game

import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
interface Preparator {
    fun ready(player: Player)
    fun cancelReady(player: Player)
    fun isReady(player: Player): Boolean
}