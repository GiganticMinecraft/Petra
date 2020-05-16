package click.seichi.petra.stage.generator

import click.seichi.extension.setRegion
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
class FirstStageGenerator(
        radius: Int,
        dangerZoneLength: Int
) : RoundStageGenerator(radius, dangerZoneLength) {

    private val baseHeight = 50
    private val maxHeight = 65

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
                chunk.setRegion(localX, localZ, 1, world.maxHeight, Material.BARRIER)
                chunk.setBlock(localX, 0, localZ, Material.BEDROCK)
            } else if (distance > dangerZoneRadius) {
                // 範囲外最近点
                chunk.setBlock(localX, maxHeight + 1, localZ, Material.GLOWSTONE)
                chunk.setRegion(localX, localZ, 1, maxHeight, Material.QUARTZ_PILLAR)
                chunk.setBlock(localX, 0, localZ, Material.BEDROCK)
            } else if (distance > radius) {
                // デンジャーゾーン
                val currentHeight = (generator.noise(globalX.toDouble(), globalZ.toDouble(), 0.5, 0.5) * 15.0 + 50.0).toInt()
                chunk.setBlock(localX, currentHeight, localZ, Material.NETHER_BRICKS)
                chunk.setRegion(localX, localZ, 1, currentHeight, Material.NETHERRACK)
                chunk.setBlock(localX, 0, localZ, Material.BEDROCK)
            } else {
                // セーフゾーン
                val currentHeight = (generator.noise(globalX.toDouble(), globalZ.toDouble(), 0.5, 0.5) * 15.0 + 50.0).toInt()
                chunk.setBlock(localX, currentHeight, localZ, Material.GRASS_BLOCK)
                chunk.setBlock(localX, currentHeight - 1, localZ, Material.DIRT)
                chunk.setRegion(localX, localZ, 1, currentHeight - 2, Material.STONE)
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