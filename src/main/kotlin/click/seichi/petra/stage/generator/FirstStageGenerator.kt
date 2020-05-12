package click.seichi.petra.stage.generator

import click.seichi.generator.TreePopulator
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import org.bukkit.util.noise.PerlinOctaveGenerator
import java.util.*
import kotlin.math.sqrt

/**
 * @author tar0ss
 */
class FirstStageGenerator : RoundStageGenerator(64, 10) {

    override fun generateChunkData(world: World, random: Random, chunkX: Int, chunkZ: Int, biome: BiomeGrid): ChunkData {
        val generator = PerlinOctaveGenerator(Random(world.seed), 8)
        val chunk = createChunkData(world)
        generator.setScale(0.01)
        for (localX in 0..15) for (localZ in 0..15) {
            val globalX = chunkX * 16 + localX
            val globalZ = chunkZ * 16 + localZ
            val distance = sqrt((globalX * globalX + globalZ * globalZ).toDouble())
            if (distance > dangerZoneRadius + 2) {
                //  > 範囲外最近点 + 2
                chunk.setBlock(localX, 0, localZ, Material.BEDROCK)
            } else if (distance > dangerZoneRadius + 1) {
                //　範囲外最近点 + 1
                (1 until world.maxHeight).forEach { y ->
                    chunk.setBlock(localX, y, localZ, Material.BARRIER)
                }
                chunk.setBlock(localX, 0, localZ, Material.BEDROCK)
            } else if (distance > dangerZoneRadius) {
                // 範囲外最近点
                currentHeight = (generator.noise(globalX.toDouble(), globalZ.toDouble(), 0.5, 0.5) * 15.0 + 50.0).toInt()
                chunk.setBlock(localX, currentHeight + 6, localZ, Material.GLOWSTONE)
                (1..currentHeight + 5).forEach { chunk.setBlock(localX, it, localZ, Material.QUARTZ_PILLAR) }
                chunk.setBlock(localX, 0, localZ, Material.BEDROCK)
            } else if (distance > radius) {
                // デンジャーゾーン
                currentHeight = (generator.noise(globalX.toDouble(), globalZ.toDouble(), 0.5, 0.5) * 15.0 + 50.0).toInt()
                chunk.setBlock(localX, currentHeight, localZ, Material.NETHER_BRICKS)
                (1 until currentHeight).forEach { chunk.setBlock(localX, it, localZ, Material.NETHERRACK) }
                for (i in currentHeight - 2 downTo 1) chunk.setBlock(localX, i, localZ, Material.STONE)
                chunk.setBlock(localX, 0, localZ, Material.BEDROCK)
            } else {
                // セーフゾーン
                currentHeight = (generator.noise(globalX.toDouble(), globalZ.toDouble(), 0.5, 0.5) * 15.0 + 50.0).toInt()
                chunk.setBlock(localX, currentHeight, localZ, Material.GRASS_BLOCK)
                chunk.setBlock(localX, currentHeight - 1, localZ, Material.DIRT)
                for (i in currentHeight - 2 downTo 1) chunk.setBlock(localX, i, localZ, Material.STONE)
                chunk.setBlock(localX, 0, localZ, Material.BEDROCK)
            }
        }
        return chunk
    }

    override fun getDefaultPopulators(world: World): MutableList<BlockPopulator> {
        return mutableListOf(
                TreePopulator()
        )
    }
}