package click.seichi.generator

import click.seichi.extension.getHighestBlockY
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
            val y = chunk.getHighestBlockY(x, z)
            val surfaceBlock = chunk.getBlock(x, y, z)
            if (surfaceBlock.type != Material.GRASS_BLOCK) return@forEach
            if (y < 0 || 255 <= y) return@forEach
            chunk.getBlock(x, y + 1, z).setType(flower, true)
        }
    }
}