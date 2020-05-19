package click.seichi.petra.stage.generator

import org.bukkit.generator.ChunkGenerator

/**
 * @author tar0ss
 */
abstract class StageGenerator : ChunkGenerator() {

    abstract fun isDangerZone(globalX: Int, globalY: Int, globalZ: Int): Boolean

    abstract fun isSafeZone(globalX: Int, globalY: Int, globalZ: Int): Boolean

    override fun shouldGenerateCaves(): Boolean {
        return false
    }

    override fun isParallelCapable(): Boolean {
        return false
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