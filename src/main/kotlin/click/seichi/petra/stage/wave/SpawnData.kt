package click.seichi.petra.stage.wave

import click.seichi.message.Message
import click.seichi.petra.stage.raider.StageEntity

/**
 * @author tar0ss
 */
data class SpawnData(
        val entity: StageEntity,
        val message: Message
)