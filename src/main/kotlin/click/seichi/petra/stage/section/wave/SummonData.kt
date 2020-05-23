package click.seichi.petra.stage.section.wave

import click.seichi.petra.GameSound
import click.seichi.petra.message.Message
import click.seichi.petra.stage.summoner.ISummoner

/**
 * @author tar0ss
 */
data class SummonData(
        val summoner: ISummoner,
        val message: Message = GameSound.SUMMON_WAVE
)