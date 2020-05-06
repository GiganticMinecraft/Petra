package click.seichi.extension

import click.seichi.Plugin
import org.bukkit.event.Listener

/**
 * @author tar0ss
 */
fun Listener.register() = Plugin.INSTANCE.let {
    it.server.pluginManager.registerEvents(this, it)
}