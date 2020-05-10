package click.seichi.petra

import click.seichi.sound.SoundPlayer
import org.bukkit.Sound
import org.bukkit.SoundCategory

/**
 * @author tar0ss
 */
object GameSound {
    val COUNT_START = SoundPlayer(
            Sound.ENTITY_PLAYER_LEVELUP,
            SoundCategory.BLOCKS,
            pitch = 1.5F
    )

    val COUNT = SoundPlayer(
            Sound.BLOCK_NOTE_BLOCK_CHIME,
            SoundCategory.BLOCKS,
            pitch = 1.2F
    )

    val GAME_START = SoundPlayer(
            Sound.BLOCK_NOTE_BLOCK_CHIME,
            SoundCategory.BLOCKS,
            pitch = 1.6F
    )

    val GAME_END = SoundPlayer(
            Sound.ENTITY_PLAYER_LEVELUP,
            SoundCategory.BLOCKS,
            pitch = 0.3F
    )

    val GAME_RESULT = SoundPlayer(
            Sound.BLOCK_ENDER_CHEST_OPEN,
            SoundCategory.BLOCKS,
            pitch = 1.6F
    )
}