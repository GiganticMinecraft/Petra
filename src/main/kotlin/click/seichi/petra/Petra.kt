package click.seichi.petra

import click.seichi.Plugin
import click.seichi.config.Config
import click.seichi.game.PlayerLocator
import click.seichi.game.SimpleGameStarter
import click.seichi.game.command.ReadyCommand
import click.seichi.game.listener.PlayerConnectionListener
import click.seichi.petra.listener.DebugListener
import click.seichi.petra.listener.GameActionListener
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
    private lateinit var stage: Stage

    // 参加者
    private val players = mutableSetOf<UUID>()

    // 準備完了
    private val readyPlayers = mutableSetOf<UUID>()

    private val gameStarter = SimpleGameStarter(players, readyPlayers)
    private val playerLocator = PlayerLocator(players, gameStarter)

    override fun loadConfiguration(vararg configurations: Config) {
        super.loadConfiguration(
                *configurations,
                PetraConfig
        )
        stage = Stage.find(PetraConfig.STAGE_NAME)!!
        logger.fine("Load Stage \"${stage.key}\"")
    }

    override fun registerListeners(vararg listeners: Listener) {
        super.registerListeners(
                *listeners,
                PlayerConnectionListener(playerLocator),
                PlayerNavigator(players, readyPlayers),
                GameActionListener(gameStarter, stage),
                WorldListener(),
                DebugListener()
        )
    }

    override fun bindCommands(vararg commands: Pair<String, CommandExecutor>) {
        super.bindCommands(
                *commands,
                "ready" to ReadyCommand(gameStarter)
        )
    }

    override fun getDefaultWorldGenerator(worldName: String, id: String?): ChunkGenerator? {
        return stage.generator
    }
}
