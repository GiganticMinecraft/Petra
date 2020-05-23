package click.seichi.petra.game.event

import click.seichi.petra.event.CustomEvent

/**
 * @author tar0ss
 */
class CountDownEvent(val remainSeconds: Int, val count: Int) : CustomEvent()