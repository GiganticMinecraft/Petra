package click.seichi.game.event

import click.seichi.event.CustomEvent
import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
class PlayerCancelReadyEvent(
        val player: Player,
        val ready: Int,
        val all: Int
) : CustomEvent()