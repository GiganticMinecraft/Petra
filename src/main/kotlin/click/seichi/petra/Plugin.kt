package click.seichi.petra

import click.seichi.petra.config.Config
import click.seichi.petra.config.DatabaseConfig
import click.seichi.petra.config.ServerConfig
import click.seichi.petra.extension.bind
import click.seichi.petra.extension.register
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
        lateinit var INSTANCE: Plugin
            private set
    }

    override fun onEnable() {
        INSTANCE = this
        Bukkit.getServer().messenger.registerOutgoingPluginChannel(this, "BungeeCord")

        loadConfiguration(
                ServerConfig,
                DatabaseConfig
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

        registerListeners()

        bindCommands()

        // データベース作成
        runCatching {
            prepareDatabase()
        }.onFailure { exception ->
            // 例外時はプラグインを終了する
            exception.printStackTrace()
            pluginLoader.disablePlugin(this)
            return
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

    open fun loadConfiguration(vararg configurations: Config) = configurations.forEach { it.init(this) }

    private fun loadServerDefinition(bungeeName: String) = ServerDefinition.findByBungeeName(bungeeName)

    open fun registerListeners(vararg listeners: Listener) = listeners.forEach { it.register() }

    open fun bindCommands(vararg commands: Pair<String, CommandExecutor>) = commands.toMap().forEach { id, executor -> executor.bind(id) }

    open fun prepareDatabase(vararg tables: Table) {
        if (tables.isEmpty()) return
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