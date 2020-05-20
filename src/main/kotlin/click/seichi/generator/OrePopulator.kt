package click.seichi.generator

import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import java.util.*


/**
 * @author tar0ss
 */
class OrePopulator(val ore: Material, val times: Int, val yRange: IntRange, val conProb: Double) : BlockPopulator() {
    override fun populate(world: World, random: Random, chunk: Chunk) {
        var X: Int
        var Y: Int
        var Z: Int
        var isStone: Boolean
        for (i in 1..times) {  // Number of tries
            // The chance of spawning
            if (random.nextInt(100) >= 60) continue

            X = random.nextInt(15)
            Z = random.nextInt(15)
            Y = yRange.random() // Get randomized coordinates
            if (chunk.getBlock(X, Y, Z).type != Material.STONE) continue
            isStone = true
            while (isStone) {
                chunk.getBlock(X, Y, Z).type = ore
                isStone = if (conProb > random.nextDouble()) {   // The chance of continuing the vein
                    when (random.nextInt(5)) {
                        0 -> X++
                        1 -> Y++
                        2 -> Z++
                        3 -> X--
                        4 -> Y--
                        5 -> Z--
                    }

                    X in 0..15 && Z in 0..15 && Y in 0 until world.maxHeight &&
                            chunk.getBlock(X, Y, Z).type == Material.STONE
                } else false
            }
        }
    }
}