package click.seichi.game.event

import click.seichi.event.CustomEvent
import java.util.*

/**
 * @author tar0ss
 */
class PrepareEvent(val players: Set<UUID>) : CustomEvent()