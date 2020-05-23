package click.seichi.petra.generator

import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import java.util.*

/**
 * @author tar0ss
 */
class FlowerPopulator(vararg flowers: Material) : BlockPopulator() {
    val flowers = flowers.toSet()
    override fun populate(world: World, random: Random, chunk: Chunk) {
        if (random.nextFloat() >= 0.20f) return
        val flower = flowers.random()
        val amount = random.nextInt(10) + 6
        (1..amount).forEach { _ ->
            val x = (random.nextFloat() * 7.5 + 7.5).toInt()
            val z = (random.nextFloat() * 7.5 + 7.5).toInt()
            var y = world.maxHeight - 1
            while (chunk.getBlock(x, y, z).type.isAir) {
                y--
            }
            if (y < 0 || 255 <= y) return@forEach
            val surfaceBlock = chunk.getBlock(x, y, z)
            if (surfaceBlock.type != Material.GRASS_BLOCK) return@forEach
            val targetBlock = chunk.getBlock(x, y + 1, z)
            if (!targetBlock.type.isAir) return@forEach
            targetBlock.setType(flower, true)
        }
    }
}