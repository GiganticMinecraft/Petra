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
class GrassPopulator : BlockPopulator() {
    override fun populate(world: World, random: Random, chunk: Chunk) {
        val amount = random.nextInt(16 * 8) + 1
        (1..amount).forEach { _ ->
            val x = random.nextInt(15)
            val z = random.nextInt(15)
            val y = chunk.getHighestBlockY(x, z)
            val surfaceBlock = chunk.getBlock(x, y, z)
            if (surfaceBlock.type != Material.GRASS_BLOCK) return@forEach
            if (y < 0 || 255 <= y) return@forEach
            chunk.getBlock(x, y + 1, z).setType(Material.GRASS, true)
        }
    }
}