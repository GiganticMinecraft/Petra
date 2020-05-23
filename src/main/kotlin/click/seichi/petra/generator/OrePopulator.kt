package click.seichi.petra.generator

import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import java.util.*

/**
 * @author tar0ss
 */
class OrePopulator(val ore: Material, val tries: Int, val yRange: IntRange, val conProb: Double) : BlockPopulator() {
    override fun populate(world: World, random: Random, chunk: Chunk) {
        for (i in 1..tries) {
            // The chance of spawning
            if (random.nextInt(100) >= 60) continue

            var x = random.nextInt(15)
            var z = random.nextInt(15)
            var y = yRange.random() // Get randomized coordinates
            var isStone = true

            if (chunk.getBlock(x, y, z).type != Material.STONE) continue

            while (isStone) {
                chunk.getBlock(x, y, z).type = ore
                isStone = if (conProb > random.nextDouble()) {   // The chance of continuing the vein
                    listOf({ x++ }, { y++ }, { z++ }, { x-- }, { y-- }, { z-- })
                            .shuffled()
                            .first().invoke()

                    x in 0..15 && z in 0..15 && (y in 0 until world.maxHeight)
                            && chunk.getBlock(x, y, z).type == Material.STONE
                } else false
            }
        }
    }
}