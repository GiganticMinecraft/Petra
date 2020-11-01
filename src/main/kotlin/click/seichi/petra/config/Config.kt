package click.seichi.petra.config

import click.seichi.petra.function.async
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * @author tar0ss
 */
abstract class Config(
        private val fileName: String
) : YamlConfiguration() {

    private lateinit var plugin: JavaPlugin

    fun init(plugin: JavaPlugin) {
        this.plugin = plugin
        var file = File(plugin.dataFolder, "$fileName.yml")
        if (!file.exists()) {
            this.makeFile(file, plugin, fileName)
            file = File(plugin.dataFolder, "$fileName.yml")
        }
        this.load(file)
    }

    protected open fun makeFile(file: File, plugin: JavaPlugin, fileName: String) {
        plugin.saveResource(file.name, false)
    }

    fun save() {
        async {
            val file = File(plugin.dataFolder, "$fileName.yml")
            this.save(file)
        }
    }


}