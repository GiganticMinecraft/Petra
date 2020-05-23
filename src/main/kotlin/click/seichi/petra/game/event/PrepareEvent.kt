package click.seichi.petra.game.event

import click.seichi.petra.event.CustomEvent
import java.util.*

/**
 * @author tar0ss
 */
class PrepareEvent(val players: Set<UUID>) : CustomEvent()