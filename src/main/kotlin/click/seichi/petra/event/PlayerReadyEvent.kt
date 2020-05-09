package click.seichi.petra.event

import click.seichi.event.CustomEvent
import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
class PlayerReadyEvent(val player: Player) : CustomEvent()