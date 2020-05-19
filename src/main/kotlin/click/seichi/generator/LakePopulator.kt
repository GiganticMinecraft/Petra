package click.seichi.generator

import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.generator.BlockPopulator
import java.util.*

/**
 * @author tar0ss
 */
class LakePopulator : BlockPopulator() {
    override fun populate(world: World, random: Random, chunk: Chunk) {
        if (random.nextInt(100) < 10) {  // The chance of spawning a lake
            val block: Block
            val chunkX: Int = chunk.x
            val chunkZ: Int = chunk.z
            val X = chunkX * 16 + random.nextInt(15) - 8
            val Z = chunkZ * 16 + random.nextInt(15) - 8
            var Y = world.getHighestBlockYAt(X, Z)
            val surfaceBlock = world.getBlockAt(X, Y, Z)
            if (surfaceBlock.type != Material.GRASS_BLOCK) return
            if (Y < 0 || 255 <= Y) return
//            Y -= 7
            Y -= 7
            block = world.getBlockAt(X + 8, Y, Z + 8)
            if (random.nextInt(100) < 90) block.type = Material.WATER else block.type = Material.LAVA // The chance of spawing a water or lava lake
            val aboolean = BooleanArray(2048)
            var flag: Boolean
            val i = random.nextInt(4) + 4
            var j: Int
            var j1: Int
            var k1: Int
            j = 0
            while (j < i) {
                val d0 = random.nextDouble() * 6.0 + 3.0
                val d1 = random.nextDouble() * 4.0 + 2.0
                val d2 = random.nextDouble() * 6.0 + 3.0
                val d3 = random.nextDouble() * (16.0 - d0 - 2.0) + 1.0 + d0 / 2.0
                val d4 = random.nextDouble() * (8.0 - d1 - 4.0) + 2.0 + d1 / 2.0
                val d5 = random.nextDouble() * (16.0 - d2 - 2.0) + 1.0 + d2 / 2.0
                for (k in 1..14) {
                    for (l in 1..14) {
                        for (i1 in 1..6) {
                            val d6 = (k.toDouble() - d3) / (d0 / 2.0)
                            val d7 = (i1.toDouble() - d4) / (d1 / 2.0)
                            val d8 = (l.toDouble() - d5) / (d2 / 2.0)
                            val d9 = d6 * d6 + d7 * d7 + d8 * d8
                            if (d9 < 1.0) {
                                aboolean[(k * 16 + l) * 8 + i1] = true
                            }
                        }
                    }
                }
                ++j
            }
            j = 0
            while (j < 16) {
                k1 = 0
                while (k1 < 16) {
                    j1 = 0
                    while (j1 < 8) {
                        if (aboolean[(j * 16 + k1) * 8 + j1]) {
                            val b = world.getBlockAt(X + j, Y + j1, Z + k1)
                            if (b.type == Material.STONE || b.type == Material.DIRT || b.type == Material.GRASS_BLOCK) {
                                b.type = if (j1 > 4) Material.AIR else block.type
                            }
                        }
                        ++j1
                    }
                    ++k1
                }
                ++j
            }
            j = 0
            while (j < 16) {
                k1 = 0
                while (k1 < 16) {
                    j1 = 4
                    while (j1 < 8) {
                        if (aboolean[(j * 16 + k1) * 8 + j1]) {
                            val X1 = X + j
                            val Y1 = Y + j1 - 1
                            val Z1 = Z + k1
                            if (world.getBlockAt(X1, Y1, Z1).type === Material.DIRT) {
                                world.getBlockAt(X1, Y1, Z1).type = Material.GRASS_BLOCK
                            }
                        }
                        ++j1
                    }
                    ++k1
                }
                ++j
            }
        }
    }
}