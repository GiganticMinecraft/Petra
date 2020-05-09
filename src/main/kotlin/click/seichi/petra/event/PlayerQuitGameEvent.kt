package click.seichi.petra.event

import click.seichi.event.CustomEvent
import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
class PlayerQuitGameEvent(val player: Player) : CustomEvent()