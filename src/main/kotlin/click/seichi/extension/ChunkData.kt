package click.seichi.extension

import org.bukkit.Material
import org.bukkit.generator.ChunkGenerator

/**
 * @author tar0ss
 */

/**
 * Set a region of this chunk to material.
 *
 * Setting blocks outside the chunk's bounds does nothing.
 *
 * @param x x location (inclusive) in the chunk to set
 * @param z z location (inclusive) in the chunk to set
 * @param yMin minimum y location (inclusive) in the chunk to set
 * @param yMax maximum y location (inclusive) in the chunk to set
 * @param material the type to set the blocks to
 */
fun ChunkGenerator.ChunkData.setRegion(x: Int, z: Int, yMin: Int, yMax: Int, material: Material) {
    return this.setRegion(x, yMin, z, x + 1, yMax + 1, z + 1, material)
}