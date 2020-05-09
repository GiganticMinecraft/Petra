package click.seichi.function

import org.bukkit.Bukkit
import org.bukkit.ChatColor

/**
 * @author tar0ss
 */
fun warning(str: String) {
    Bukkit.getServer().logger.warning(str)
}

fun info(str: String) {
    Bukkit.getServer().logger.info(str)
}

fun fine(str: String) {
    Bukkit.getServer().logger.fine(str)
}

fun debug(str: String) {
    Bukkit.getServer().logger.info("${ChatColor.LIGHT_PURPLE}$str")
}