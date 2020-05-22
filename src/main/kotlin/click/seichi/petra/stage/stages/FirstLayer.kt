package click.seichi.petra.stage.stages

import click.seichi.extension.setRegion
import click.seichi.generator.*
import click.seichi.message.SoundMessage
import click.seichi.petra.stage.generator.RoundStageGenerator
import click.seichi.petra.stage.section.BreakSection
import click.seichi.petra.stage.section.Section
import click.seichi.petra.stage.section.wave.DefensePlayerWave
import click.seichi.petra.stage.section.wave.DefenseSummonerWave
import click.seichi.petra.stage.section.wave.SummonData
import click.seichi.petra.stage.section.wave.WaveData
import click.seichi.petra.stage.summon.RoundStageSummonProxy
import click.seichi.petra.stage.summoner.MultiEntity
import click.seichi.petra.stage.summoner.Summoner
import click.seichi.petra.stage.summoner.Summoners
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.World
import org.bukkit.entity.EntityType
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
                    LakePopulator(),
                    TreePopulator(),
                    GrassPopulator(),
                    FlowerPopulator(Material.DANDELION, Material.POPPY),
                    OrePopulator(Material.COAL_ORE, 15, 0..62, 0.9),
                    OrePopulator(Material.DIAMOND_ORE, 1, 0..15, 0.9),
                    OrePopulator(Material.IRON_ORE, 18, 0..62, 0.84),
                    OrePopulator(Material.GOLD_ORE, 8, 0..50, 0.84),
                    OrePopulator(Material.LAPIS_ORE, 3, 0..40, 0.4),
                    OrePopulator(Material.REDSTONE_ORE, 3, 0..40, 0.4),
                    OrePopulator(Material.EMERALD_ORE, 1, 0..30, 0.1)
            )
        }
    }

    val SPAWN_PROXY = RoundStageSummonProxy(RADIUS, DANGER_ZONE_LENGTH)

    val SECTIONS: Array<Section> = arrayOf(
            BreakSection(10),
            DefenseSummonerWave(1, 1,
                    WaveData(mapOf(
                            30 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.ZOMBIE) to 10,
                                            Summoners.INFLAMMABLE_ZOMBIE to 5,
                                            Summoner(EntityType.CREEPER) to 2
                                    ),
                                    SoundMessage(Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 2.0f, 2.0f)
                            )
                    )), Summoner(EntityType.VILLAGER, Summoner.SummonCase.CENTER)
            ), BreakSection(10),
            DefensePlayerWave(1, 1,
                    WaveData(mapOf(
                            0 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.ZOMBIE) to 10,
                                            Summoners.INFLAMMABLE_ZOMBIE to 5,
                                            Summoner(EntityType.CREEPER) to 2
                                    ),
                                    SoundMessage(Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 2.0f, 2.0f)
                            ),
                            10 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.SKELETON) to 10,
                                            Summoners.CAPPED_SKELETON to 5,
                                            Summoner(EntityType.CREEPER) to 2
                                    ),
                                    SoundMessage(Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 2.0f, 2.0f)
                            )
                    ))
            )
//            Wave(1, 1,
//                    WaveData(mapOf(
//                            0 to SummonData(
//                                    MultiEntity(
//                                            Summoner(EntityType.ZOMBIE) to 10,
//                                            Summoners.INFLAMMABLE_ZOMBIE to 5,
//                                            Summoner(EntityType.CREEPER) to 2
//                                    ),
//                                    SoundMessage(Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 2.0f, 2.0f)
//                            ),
//                            10 to SummonData(
//                                    MultiEntity(
//                                            Summoner(EntityType.SKELETON) to 10,
//                                            Summoners.CAPPED_SKELETON to 5,
//                                            Summoner(EntityType.CREEPER) to 2
//                                    ),
//                                    SoundMessage(Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 2.0f, 2.0f)
//                            )
//                    ))
//            )
    )
}