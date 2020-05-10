package click.seichi.game

import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
interface IPlayerLocator {

    fun isLeft(player: Player): Boolean

    fun join(player: Player): String?

    fun leave(player: Player): String?
}