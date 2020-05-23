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

    val START_WAVE = SoundMessage(
            Sound.ENTITY_ILLUSIONER_CAST_SPELL,
            SoundCategory.BLOCKS,
            2.0f,
            0.3f
    )

    val START_BREAK_SECTION = SoundMessage(
            Sound.ENTITY_PLAYER_LEVELUP,
            SoundCategory.BLOCKS,
            pitch = 1.7F
    )

    val GAME_RESULT = SoundMessage(
            Sound.BLOCK_ENDER_CHEST_OPEN,
            SoundCategory.BLOCKS,
            pitch = 1.6F
    )

    val WIN = SoundMessage(
            Sound.UI_TOAST_CHALLENGE_COMPLETE,
            SoundCategory.BLOCKS,
            pitch = 1.4F
    )

    val LOSE = SoundMessage(
            Sound.ENTITY_VILLAGER_NO,
            SoundCategory.BLOCKS,
            pitch = 1.3F
    )

    val TELEPORT = SoundMessage(
            Sound.ITEM_CHORUS_FRUIT_TELEPORT,
            SoundCategory.BLOCKS,
            pitch = 1.4F
    )


}