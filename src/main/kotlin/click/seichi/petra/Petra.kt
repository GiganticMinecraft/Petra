package click.seichi.petra

import click.seichi.Plugin
import click.seichi.config.Config
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.generator.ChunkGenerator
import org.jetbrains.exposed.sql.Table

/**
 * @author tar0ss
 */
class Petra : Plugin() {
    private lateinit var stage: Stage
    private val playerLocator = PlayerLocator()

    override val configurations: Array<Config> = arrayOf(
            PetraConfig
    )

    override val listeners: Array<Listener> = arrayOf(
            PlayerConnectionListener(playerLocator)
    )
    override val commands: Array<Pair<String, CommandExecutor>> = arrayOf(

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