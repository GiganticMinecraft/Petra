package click.seichi.petra.stage.stages

import click.seichi.extension.setRegion
import click.seichi.generator.FlowerPopulator
import click.seichi.generator.GrassPopulator
import click.seichi.generator.TreePopulator
import click.seichi.message.SoundMessage
import click.seichi.message.TitleMessage
import click.seichi.petra.stage.generator.RoundStageGenerator
import click.seichi.petra.stage.raider.InflammableZombie
import click.seichi.petra.stage.raider.MultiEntity
import click.seichi.petra.stage.spawn.RoundStageSpawner
import click.seichi.petra.stage.wave.SpawnData
import click.seichi.petra.stage.wave.TimedWave
import click.seichi.petra.stage.wave.WaveData
import org.bukkit.*
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.ChunkGenerator
import org.bukkit.util.noise.PerlinOctaveGenerator
import java.util.*
import kotlin.math.sqrt

/**
 * @author tar0ss
 */
object FirstLayer {

    const val KEY = "1stLayer"

    private const val RADIUS = 64
    private const val DANGER_ZONE_LENGTH = 10

    val GENERATOR = object : RoundStageGenerator(RADIUS, DANGER_ZONE_LENGTH) {

        private val baseHeight = 50
        private val maxHeight = 65

        override fun generateChunkData(world: World, random: Random, chunkX: Int, chunkZ: Int, biome: ChunkGenerator.BiomeGrid): ChunkGenerator.ChunkData {
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
                    TreePopulator(),
                    GrassPopulator(),
                    FlowerPopulator(Material.DANDELION, Material.POPPY)
            )
        }
    }

    val SPAWN_PROXY = RoundStageSpawner(RADIUS, DANGER_ZONE_LENGTH)

    val WAVES = arrayOf(
            TimedWave(
                    WaveData(mapOf(
                            10 to SpawnData(
                                    MultiEntity(
                                            InflammableZombie() to 5
                                    ),
                                    SoundMessage(Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 2.0f, 2.0f)
                            )
                    )), 20,
                    TitleMessage("${ChatColor.WHITE}Wave1 ${ChatColor.RED}襲撃", "${ChatColor.AQUA}2分間生き延びろ"
                    ).add(
                            SoundMessage(Sound.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.BLOCKS, 2.0f, 0.3f)
                    )
            )
    )
}