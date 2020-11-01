package click.seichi.petra.config

/**
 * @author tar0ss
 */
object PetraConfig : Config("petra") {
    val STAGE_NAME by lazy { getString("stage")!! }
    val SAVE_WINNER by lazy { getBoolean("save_winner") }
}