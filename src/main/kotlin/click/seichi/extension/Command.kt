package click.seichi.extension

import click.seichi.Plugin
import org.bukkit.command.CommandExecutor

/**
 * @author tar0ss
 */

/**
 * plugin.ymlで登録したコマンドをexecutorに紐づけする
 */
fun CommandExecutor.bind(id: String) {
    Plugin.PLUGIN.run {
        getCommand(id).also { pluginCommand ->
            if (pluginCommand == null) {
                logger.warning("${this@bind::class.simpleName}を登録できませんでした")
                logger.warning("plugin.ymlに$id を登録してください")
            } else {
                pluginCommand.setExecutor(this@bind)
            }
        }
    }

}