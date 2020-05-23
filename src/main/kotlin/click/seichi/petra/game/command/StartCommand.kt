package click.seichi.petra.game.command

import click.seichi.petra.game.Game
import click.seichi.petra.game.Preparator
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
class StartCommand(
        private val game: Game,
        private val preparator: Preparator
) : TabExecutor {

    private val isStarted: Boolean
        get() = game.isStarted

    override fun onCommand(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<out String>
    ): Boolean {

        // 引数あれば除外
        if (args.isNotEmpty()) return false

        // consoleなら除外
        if (sender !is Player) {
            sender.sendMessage("You should execute command in game.")
            return true
        }

        if (isStarted) {
            sender.sendMessage("既にゲームが開始されています")
            return true
        }

        if (!sender.isOp) {
            sender.sendMessage("You are not op.")
            return true
        }

        preparator.prepare()
        return true
    }

    override fun onTabComplete(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<out String>
    ): MutableList<String> {
        return mutableListOf()
    }
}