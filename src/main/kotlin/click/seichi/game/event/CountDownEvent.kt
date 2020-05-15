package click.seichi.game.event

import click.seichi.event.CustomEvent

/**
 * @author tar0ss
 */
class CountDownEvent(val remainSeconds: Int, val count: Int) : CustomEvent()