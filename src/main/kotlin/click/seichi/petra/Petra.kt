package click.seichi.petra

import click.seichi.Plugin
import click.seichi.config.Config
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.jetbrains.exposed.sql.Table

/**
 * @author tar0ss
 */
class Petra : Plugin() {
    private lateinit var stage: Stage

    override val configurations: Array<Config> = arrayOf(
            PetraConfig
    )
    override val listeners: Array<Listener> = arrayOf(

    )
    override val commands: Array<Pair<String, CommandExecutor>> = arrayOf(

    )
    override val tables: Array<Table> = arrayOf(

    )

    override fun onEnable() {
        super.onEnable()
        stage = Stage.find(PetraConfig.STAGE_NAME)!!
    }
}