package click.seichi.petra.config

/**
 * @author tar0ss
 */
object ServerConfig : Config("server") {

    val BUNGEE_NAME by lazy { getString("bungee_name")!! }
}