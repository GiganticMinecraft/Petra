package click.seichi.petra.stage.wave

import click.seichi.message.Message
import click.seichi.petra.stage.raider.Spawnable

/**
 * @author tar0ss
 */
data class SpawnData(
        val entity: Spawnable,
        val message: Message
)