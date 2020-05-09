package click.seichi.petra

import click.seichi.Plugin
import click.seichi.config.Config
import click.seichi.petra.command.ReadyCommand
import click.seichi.petra.listener.PlayerConnectionListener
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.generator.ChunkGenerator
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
            PlayerConnectionListener()
    )
    override val commands: Array<Pair<String, CommandExecutor>> = arrayOf(
            "ready" to ReadyCommand()
    )
    override val tables: Array<Table> = arrayOf()

    override fun onEnable() {
        super.onEnable()
        stage = Stage.find(PetraConfig.STAGE_NAME)!!
        logger.fine("Load Stage \"${stage.key}\"")
    }

    override fun getDefaultWorldGenerator(worldName: String, id: String?): ChunkGenerator? {
        return StageChunkGenerator(stage)
    }
}