package click.seichi

import click.seichi.config.Config
import click.seichi.config.DatabaseConfig
import click.seichi.config.ServerConfig
import click.seichi.extension.bind
import click.seichi.extension.register
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author tar0ss
 */
abstract class Plugin : JavaPlugin() {

    private lateinit var serverDefinition: ServerDefinition

    companion object {
        lateinit var PLUGIN: Plugin
            private set
    }

    abstract val configurations: Array<Config>
    abstract val listeners: Array<Listener>
    abstract val commands: Array<Pair<String, CommandExecutor>>
    abstract val tables: Array<Table>

    override fun onEnable() {
        PLUGIN = this
        Bukkit.getServer().messenger.registerOutgoingPluginChannel(this, "BungeeCord")

        loadConfiguration(
                ServerConfig,
                DatabaseConfig,
                *configurations
        )

        loadServerDefinition(ServerConfig.BUNGEE_NAME).let {
            if (it == null) {
                // 存在しない場合はプラグインを終了する
                logger.warning("${ServerConfig.BUNGEE_NAME} is not available.")
                logger.warning("available server name list:${ServerDefinition.bungeeNameArray}")
                pluginLoader.disablePlugin(this)
                return
            }
            serverDefinition = it
        }

        registerListeners(*listeners)

        bindCommands(*commands)

        if (tables.isNotEmpty()) {
            // データベース作成
            runCatching {
                prepareDatabase(*tables
                )
            }.onFailure { exception ->
                // 例外時はプラグインを終了する
                exception.printStackTrace()
                pluginLoader.disablePlugin(this)
                return
            }
        }

        logger.info("Enabled")
    }

    override fun onDisable() {

        Bukkit.getOnlinePlayers().filterNotNull().forEach { player ->
            player.kickPlayer("Restarting...Please wait a few seconds.")
        }

        server.scheduler.cancelTasks(this)
        logger.info("Disabled")
    }

    private fun loadConfiguration(vararg configurations: Config) = configurations.forEach { it.init(this) }

    private fun loadServerDefinition(bungeeName: String) = ServerDefinition.findByBungeeName(bungeeName)

    private fun registerListeners(vararg listeners: Listener) = listeners.forEach { it.register() }

    private fun bindCommands(vararg commands: Pair<String, CommandExecutor>) = commands.toMap().forEach { id, executor -> executor.bind(id) }

    private fun prepareDatabase(vararg tables: Table) {
        //connect MySQL
        Database.connect("jdbc:mysql://${DatabaseConfig.HOST}/${DatabaseConfig.DATABASE}",
                "com.mysql.jdbc.Driver", DatabaseConfig.USER, DatabaseConfig.PASSWORD)

        // create Tables
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                    *tables
            )
        }
    }
}