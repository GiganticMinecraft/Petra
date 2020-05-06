package click.seichi.extension

import click.seichi.Plugin

/**
 * @author tar0ss
 */

/**
 * Executes the given [f] via the [plugin] asynchronous.
 */
fun Any.async(delayTicks: Long = 0L, repeatingTicks: Long = 0L, f: () -> Unit) {
    if (repeatingTicks > 0) {
        Plugin.INSTANCE.server.scheduler.runTaskTimerAsynchronously(Plugin.INSTANCE, f, delayTicks, repeatingTicks)
    } else {
        Plugin.INSTANCE.server.scheduler.runTaskLaterAsynchronously(Plugin.INSTANCE, f, delayTicks)
    }
}

/**
 * Executes the given [f] via the [plugin] synchronized with the server tick.
 */
fun Any.sync(delayTicks: Long = 0L, repeatingTicks: Long = 0L, f: () -> Unit) {
    if (repeatingTicks > 0) {
        Plugin.INSTANCE.server.scheduler.runTaskTimer(Plugin.INSTANCE, f, delayTicks, repeatingTicks)
    } else {
        Plugin.INSTANCE.server.scheduler.runTaskLater(Plugin.INSTANCE, f, delayTicks)
    }
}