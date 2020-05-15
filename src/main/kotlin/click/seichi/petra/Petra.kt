package click.seichi.petra

import click.seichi.Plugin
import click.seichi.config.Config
import click.seichi.game.PlayerLocator
import click.seichi.game.Preparator
import click.seichi.game.SimplePreparator
import click.seichi.game.command.ReadyCommand
import click.seichi.game.listener.DebugListener
import click.seichi.game.listener.PlayerConnectionListener
import click.seichi.petra.listener.PetraGame
import click.seichi.petra.listener.PlayerNavigator
import click.seichi.petra.listener.WorldListener
import click.seichi.petra.stage.Stage
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.generator.ChunkGenerator
import java.util.*

/**
 * @author tar0ss
 */
class Petra : Plugin() {
    // 準備完了
    private val readyPlayers = mutableSetOf<UUID>()

    private lateinit var stage: Stage
    private lateinit var game: PetraGame
    private lateinit var preparator: Preparator
    private lateinit var playerLocator: PlayerLocator


    override fun loadConfiguration(vararg configurations: Config) {
        super.loadConfiguration(
                *configurations,
                PetraConfig
        )
        stage = Stage.find(PetraConfig.STAGE_NAME)!!
        logger.fine("Load Stage \"${stage.key}\"")
        game = PetraGame(stage)
        preparator = SimplePreparator(game, readyPlayers)
        playerLocator = PlayerLocator(game, preparator)
    }

    override fun registerListeners(vararg listeners: Listener) {
        super.registerListeners(
                *listeners,
                PlayerConnectionListener(playerLocator),
                PlayerNavigator(game, readyPlayers),
                game,
                WorldListener(),
                DebugListener()
        )
    }

    override fun bindCommands(vararg commands: Pair<String, CommandExecutor>) {
        super.bindCommands(
                *commands,
                "ready" to ReadyCommand(game, preparator)
        )
    }

    override fun getDefaultWorldGenerator(worldName: String, id: String?): ChunkGenerator? {
        return stage.generator
    }
}
