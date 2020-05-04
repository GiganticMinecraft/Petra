package click.seichi.config

/**
 * @author tar0ss
 */
object DatabaseConfig : Config("database") {

    val HOST: String by lazy { getString("host")!! }

    val DATABASE: String by lazy { getString("database")!! }

    val USER: String by lazy { getString("user")!! }

    val PASSWORD: String by lazy { getString("password")!! }
}