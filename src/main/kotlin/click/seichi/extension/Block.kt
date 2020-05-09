package click.seichi.extension

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace

/**
 * @author tar0ss
 */
val AIRS = setOf(
        Material.AIR,
        Material.CAVE_AIR,
        Material.VOID_AIR
)

val Block.isAir
    get() = AIRS.contains(type)

val Block.isSurface
    get() = if (isAir) false
    else (1..3).firstOrNull {
        val block = getRelative(0, it, 0)
        !block.isAir && !block.isPassable
    }?.let { false } ?: true


val Block.surfaceBlock: Block
    get() {
        var block = world.getBlockAt(x, world.maxHeight - 1, z)
        while (!block.isSurface && block.y > 0) {
            block = block.getRelative(BlockFace.DOWN)
        }
        return block
    }