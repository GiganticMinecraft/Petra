package click.seichi.petra.extension

import org.bukkit.Chunk

/**
 * @author tar0ss
 */

fun Chunk.getHighestBlockY(x: Int, z: Int): Int {
    var y = world.maxHeight - 1
    while (getBlock(x, y, z).type.isAir) y--
    return y
}