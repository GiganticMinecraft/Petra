package click.seichi.petra

import click.seichi.Plugin
import click.seichi.config.Config
import click.seichi.config.ServerConfig
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.jetbrains.exposed.sql.Table

/**
 * @author tar0ss
 */
class Petra : Plugin() {
    override val configurations : Array<Config> = arrayOf(
                ServerConfig
        )
    override val listeners: Array<Listener> = arrayOf(

    )
    override val commands: Array<Pair<String, CommandExecutor>> = arrayOf(

    )
    override val tables: Array<Table> = arrayOf(

    )
}