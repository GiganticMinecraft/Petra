package click.seichi.generator

import click.seichi.extension.getHighestBlockY
import org.bukkit.Chunk
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
                val y = chunk.getHighestBlockY(x, z)
                // The tree type can be changed if you want.
                world.generateTree(chunk.getBlock(x, y, z).location, TreeType.TREE)
            }
        }
    }
}