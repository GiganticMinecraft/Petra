package click.seichi.petra.command

import click.seichi.petra.GameStarter
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

/**
 * @author tar0ss
 */
class ReadyCommand : TabExecutor {

    private val gameStarter = GameStarter

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

        if (gameStarter.isStarted) {
            sender.sendMessage("既にゲームが開始されています")
            return true
        }

        if (gameStarter.isReady(sender)) {
            sender.sendMessage("既に準備完了しています")
            return true
        }

        gameStarter.ready(sender)
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