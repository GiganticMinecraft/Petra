package click.seichi.petra.game

import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
interface IPlayerLocator {

    fun allowsToJoin(player: Player): Boolean

    fun join(player: Player): String?

    fun leave(player: Player): String?
}