package click.seichi.petra.generator

import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.TreeType
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import java.util.*


/**
 * @author tar0ss
 */
class TreePopulator : BlockPopulator() {
    override fun populate(world: World, random: Random, chunk: Chunk) {
        if (random.nextBoolean()) {
            val amount = random.nextInt(4) + 1 // Amount of trees
            (1..amount).forEach { _ ->
                val x = random.nextInt(15)
                val z = random.nextInt(15)
                var y = world.maxHeight - 1
                while (chunk.getBlock(x, y, z).type.isAir) {
                    y--
                }
                if (y < 0 || 255 <= y) return@forEach
                val surfaceBlock = chunk.getBlock(x, y, z)
                if (surfaceBlock.type != Material.GRASS_BLOCK) return@forEach
                // The tree type can be changed if you want.
                world.generateTree(surfaceBlock.location, TreeType.TREE)
            }
        }
    }
}