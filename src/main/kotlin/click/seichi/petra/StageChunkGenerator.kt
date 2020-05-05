package click.seichi.petra

import click.seichi.generator.TreePopulator
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.ChunkGenerator
import org.bukkit.util.noise.SimplexOctaveGenerator
import java.util.*


/**
 * @author tar0ss
 */
class StageChunkGenerator : ChunkGenerator() {
    var currentHeight = 50

    override fun generateChunkData(world: World, random: Random, chunkX: Int, chunkZ: Int, biome: BiomeGrid): ChunkData {
        val generator = SimplexOctaveGenerator(Random(world.seed), 8)
        val chunk = createChunkData(world)
        generator.setScale(0.005)
        for (X in 0..15) for (Z in 0..15) {
            currentHeight = (generator.noise(chunkX * 16 + X.toDouble(), chunkZ * 16 + Z.toDouble(), 0.5, 0.5) * 15.0 + 50.0).toInt()
            chunk.setBlock(X, currentHeight, Z, Material.GRASS_BLOCK)
            chunk.setBlock(X, currentHeight - 1, Z, Material.DIRT)
            for (i in currentHeight - 2 downTo 1) chunk.setBlock(X, i, Z, Material.STONE)
            chunk.setBlock(X, 0, Z, Material.BEDROCK)
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