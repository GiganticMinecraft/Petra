package click.seichi.petra

import click.seichi.message.SoundMessage
import org.bukkit.Sound
import org.bukkit.SoundCategory

/**
 * @author tar0ss
 */
object GameSound {
    val START_COUNT = SoundMessage(
            Sound.ENTITY_PLAYER_LEVELUP,
            SoundCategory.BLOCKS,
            pitch = 1.5F
    )

    val COUNT = SoundMessage(
            Sound.BLOCK_NOTE_BLOCK_CHIME,
            SoundCategory.BLOCKS,
            pitch = 1.2F
    )

    val START_GAME = SoundMessage(
            Sound.BLOCK_NOTE_BLOCK_CHIME,
            SoundCategory.BLOCKS,
            pitch = 1.6F
    )

    val GAME_END = SoundMessage(
            Sound.ENTITY_PLAYER_LEVELUP,
            SoundCategory.BLOCKS,
            pitch = 0.3F
    )

    val GAME_RESULT = SoundMessage(
            Sound.BLOCK_ENDER_CHEST_OPEN,
            SoundCategory.BLOCKS,
            pitch = 1.6F
    )
}