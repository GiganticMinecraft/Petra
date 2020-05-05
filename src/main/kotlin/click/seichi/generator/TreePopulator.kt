package click.seichi.generator

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
            for (i in 1 until amount) {
                val x = random.nextInt(15)
                val z = random.nextInt(15)
                var y: Int = world.maxHeight - 1
                while (chunk.getBlock(x, y, z).type === Material.AIR) {
                    // Find the highest block of the (X,Z) coordinate chosen.
                    y--
                }
                world.generateTree(chunk.getBlock(x, y, z).location, TreeType.TREE) // The tree type can be changed if you want.
            }
        }
    }
}