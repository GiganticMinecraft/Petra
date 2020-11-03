package click.seichi.petra.extension

import click.seichi.petra.Plugin
import click.seichi.petra.function.debug
import org.bukkit.event.Listener

/**
 * @author tar0ss
 */
fun Listener.register() = Plugin.INSTANCE.let {
    debug("register listener ${this::class.simpleName}")
    it.server.pluginManager.registerEvents(this, it)
}