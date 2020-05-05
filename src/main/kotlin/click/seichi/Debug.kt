package click.seichi

import org.bukkit.ChatColor
import java.util.logging.Logger

/**
 * @author tar0ss
 */
object Debug {

    private var isInitialized = false
    private lateinit var logger: Logger

    fun setLogger(logger: Logger) {
        this.logger = logger
        this.isInitialized = true
    }

    fun log(s: String) {
        if (isInitialized) logger.info("${ChatColor.LIGHT_PURPLE}$s")
    }
}