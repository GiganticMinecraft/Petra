package click.seichi.game.event

import click.seichi.event.CustomEvent
import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
class SpectatorJoinEvent(
        val player: Player
) : CustomEvent()