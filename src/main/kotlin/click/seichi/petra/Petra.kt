package click.seichi.petra

import click.seichi.config.Config
import click.seichi.game.GamePlugin
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.generator.ChunkGenerator
import org.jetbrains.exposed.sql.Table

/**
 * @author tar0ss
 */
class Petra : GamePlugin() {
    private lateinit var stage: Stage

    override val configurations: Array<Config> = arrayOf(
            PetraConfig,
            *super.configurations
    )
    override val listeners: Array<Listener> = arrayOf(
            *super.listeners
    )
    override val commands: Array<Pair<String, CommandExecutor>> = arrayOf(
            *super.commands
    )
    override val tables: Array<Table> = arrayOf(
            *super.tables
    )

    override fun onEnable() {
        super.onEnable()
        stage = Stage.find(PetraConfig.STAGE_NAME)!!
        logger.fine("Load Stage \"${stage.key}\"")
    }

    override fun getDefaultWorldGenerator(worldName: String, id: String?): ChunkGenerator? {
        return StageChunkGenerator(stage)
    }
}