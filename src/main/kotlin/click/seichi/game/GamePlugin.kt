package click.seichi.game

import click.seichi.Plugin
import click.seichi.game.command.ReadyCommand
import click.seichi.game.listener.PlayerConnectionListener
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener

/**
 * @author tar0ss
 */
abstract class GamePlugin : Plugin() {
    override val listeners: Array<Listener> = arrayOf(
            PlayerConnectionListener()
    )
    override val commands: Array<Pair<String, CommandExecutor>> = arrayOf(
            "ready" to ReadyCommand()
    )
}