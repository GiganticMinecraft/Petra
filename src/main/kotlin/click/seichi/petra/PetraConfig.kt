package click.seichi.petra

import click.seichi.petra.config.Config

/**
 * @author tar0ss
 */
object PetraConfig : Config("petra") {
    val STAGE_NAME by lazy { getString("stage")!! }
}