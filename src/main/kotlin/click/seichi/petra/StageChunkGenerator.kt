package click.seichi.petra

import click.seichi.generator.TreePopulator
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.ChunkGenerator
import org.bukkit.util.noise.PerlinOctaveGenerator
import java.util.*


/**
 * @author tar0ss
 */
class StageChunkGenerator : ChunkGenerator() {
    var currentHeight = 50

    override fun generateChunkData(world: World, random: Random, chunkX: Int, chunkZ: Int, biome: BiomeGrid): ChunkData {
        val generator = PerlinOctaveGenerator(Random(world.seed), 8)
        val chunk = createChunkData(world)
        generator.setScale(0.01)
        for (localX in 0..15) for (localZ in 0..15) {
            currentHeight = (generator.noise(chunkX * 16 + localX.toDouble(), chunkZ * 16 + localZ.toDouble(), 0.5, 0.5) * 15.0 + 50.0).toInt()
            chunk.setBlock(localX, currentHeight, localZ, Material.GRASS_BLOCK)
            chunk.setBlock(localX, currentHeight - 1, localZ, Material.DIRT)
            for (i in currentHeight - 2 downTo 1) chunk.setBlock(localX, i, localZ, Material.STONE)
            chunk.setBlock(localX, 0, localZ, Material.BEDROCK)
        }
        return chunk
    }

    override fun getDefaultPopulators(world: World): MutableList<BlockPopulator> {
        return mutableListOf(
                TreePopulator()
        )
    }

    override fun shouldGenerateCaves(): Boolean {
        return false
    }

    override fun isParallelCapable(): Boolean {
        return true
    }

    override fun shouldGenerateDecorations(): Boolean {
        return false
    }

    override fun shouldGenerateMobs(): Boolean {
        return false
    }

    override fun shouldGenerateStructures(): Boolean {
        return false
    }
}