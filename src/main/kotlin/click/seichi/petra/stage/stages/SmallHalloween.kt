package click.seichi.petra.stage.stages

import click.seichi.petra.extension.setRegion
import click.seichi.petra.generator.*
import click.seichi.petra.stage.generator.RoundStageGenerator
import click.seichi.petra.stage.section.BreakSection
import click.seichi.petra.stage.section.Section
import click.seichi.petra.stage.section.wave.*
import click.seichi.petra.stage.summon.RoundStageSummonProxy
import click.seichi.petra.stage.summoner.MultiEntity
import click.seichi.petra.stage.summoner.Summoner
import click.seichi.petra.stage.summoner.Summoners
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.ChunkGenerator
import org.bukkit.inventory.ItemStack
import org.bukkit.util.noise.PerlinOctaveGenerator
import java.util.*
import kotlin.math.sqrt

/**
 * @author tar0ss
 */
object SmallHalloween {

    const val KEY = "SmallHalloween"

    private const val RADIUS = 32
    private const val DANGER_ZONE_LENGTH = 5

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
                    val currentHeight = (generator.noise(globalX.toDouble(), globalZ.toDouble(), 0.5, 0.5) * 15.0 + baseHeight).toInt()
                    chunk.setBlock(localX, currentHeight, localZ, Material.NETHER_BRICKS)
                    chunk.setRegion(localX, localZ, 1, currentHeight, Material.NETHERRACK)
                    chunk.setBlock(localX, 0, localZ, Material.BEDROCK)
                } else {
                    // セーフゾーン
                    val currentHeight = (generator.noise(globalX.toDouble(), globalZ.toDouble(), 0.5, 0.5) * 15.0 + baseHeight).toInt()
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
                    OrePopulator(Material.COAL_ORE, 35, 0..62, 1.0),
                    OrePopulator(Material.DIAMOND_ORE, 1, 0..15, 0.9),
                    OrePopulator(Material.IRON_ORE, 30, 0..62, 0.95),
                    OrePopulator(Material.GOLD_ORE, 8, 0..50, 0.84),
                    OrePopulator(Material.LAPIS_ORE, 3, 0..40, 0.4),
                    OrePopulator(Material.REDSTONE_ORE, 3, 0..40, 0.4),
                    OrePopulator(Material.EMERALD_ORE, 1, 0..30, 0.1)
            )
        }
    }

    val SPAWN_PROXY = RoundStageSummonProxy(RADIUS, DANGER_ZONE_LENGTH)

    val SECTIONS: Array<Section> = arrayOf(
            BreakSection(120, null),
            AnnihilationWave(1, 2,
                    WaveData(mapOf(
                            0 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.ZOMBIE, { 1 }) to 4,
                                            Summoner(EntityType.SKELETON, { 1 }) to 2
                                    )
                            ),
                            30 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.ZOMBIE, { 1 }) to 3,
                                            Summoner(EntityType.SKELETON, { 1 }) to 2
                                    )
                            ),
                            60 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.ZOMBIE, { 1 }) to 5,
                                            Summoner(EntityType.SKELETON, { 1 }) to 3
                                    )
                            )
                    )
                    ), listOf(ItemStack(Material.COAL_ORE, 10))
            ),
            BonusWave(
                    SummonData(
                            MultiEntity(
                                    Summoner(EntityType.CHICKEN, { 1 }, Summoner.SummonCase.SAFE_ZONE) to 10,
                                    Summoner(EntityType.PIG, { 1 }, Summoner.SummonCase.SAFE_ZONE) to 5
                            )
                    ), 60
            ),
            DefeatSummonerWave(2, 2,
                    WaveData(mapOf(
                            0 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.CREEPER, { 1 }) to 2
                                    )
                            ),
                            10 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.CREEPER, { 1 }) to 2
                                    )
                            ),
                            20 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.CREEPER, { 1 }) to 2
                                    )
                            ),
                            30 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.CREEPER, { 1 }) to 2
                                    )
                            ),
                            40 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.CREEPER, { 1 }) to 2
                                    )
                            ),
                            50 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.CREEPER, { 1 }) to 2
                                    )
                            ),
                            60 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.CREEPER, { 1 }) to 2
                                    )
                            )
                    )), Summoners.KIMETSU_TEONI
                    , listOf(ItemStack(Material.IRON_ORE, 30))
            ), BreakSection(60),
            DefenseSummonerWave(3, 1,
                    WaveData(mapOf(
                            0 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.HUSK, { 3 }) to 1,
                                            Summoner(EntityType.VINDICATOR, { 2 }) to 1,
                                            Summoner(EntityType.EVOKER, { 1 }) to 1
                                    )
                            )
                    )), Summoners.KIMETSU_NEZUKO("unchama")
                    , listOf(
                    ItemStack(Material.GUNPOWDER, 32),
                    ItemStack(Material.SAND, 32),
                    ItemStack(Material.FLINT, 16)
            )
            ), BreakSection(60),
            AnnihilationWave(4, 2,
                    WaveData(mapOf(
                            0 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.SPIDER, { 1 }) to 4,
                                            Summoner(EntityType.CAVE_SPIDER, { 1 }) to 2
                                    )
                            ),
                            30 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.SPIDER, { 1 }) to 4,
                                            Summoner(EntityType.CAVE_SPIDER, { 1 }) to 2
                                    )
                            ),
                            60 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.SPIDER, { 1 }) to 4,
                                            Summoner(EntityType.CAVE_SPIDER, { 1 }) to 2
                                    )
                            )
                    )
                    ), listOf(ItemStack(Material.COAL_ORE, 10))
            ),
            DefensePlayerWave(5, 2,
                    WaveData(mapOf(
                            0 to SummonData(
                                    MultiEntity(
                                            Summoner(EntityType.CREEPER, { 1 }) to 4,
                                            Summoner(EntityType.ZOMBIE, { 1 }) to 4,
                                            Summoner(EntityType.SKELETON, { 1 }) to 2,
                                            Summoner(EntityType.HUSK, { 1 }) to 2,
                                            Summoner(EntityType.STRAY, { 1 }) to 2,
                                            Summoner(EntityType.VINDICATOR, { 1 }) to 1,
                                            Summoner(EntityType.ILLUSIONER, { 1 }) to 1,
                                            Summoner(EntityType.EVOKER, { 1 }) to 1,
                                            Summoner(EntityType.VEX, { 2 }) to 1,
                                            Summoner(EntityType.ENDERMAN, { 1 }) to 1
                                    )
                            )
                    )), "unchama"
            )
    )
}