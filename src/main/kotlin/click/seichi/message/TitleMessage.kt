package click.seichi.message

import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
class TitleMessage(
        private val title: String?,
        private val subTitle: String?,
        private val fadeIn: Int = 10,
        private val stay: Int = 70,
        private val fadeOut: Int = 20
) : Message {

    override fun sendTo(player: Player) {
        player.sendTitle(
                title,
                subTitle,
                fadeIn,
                stay,
                fadeOut
        )
    }
}