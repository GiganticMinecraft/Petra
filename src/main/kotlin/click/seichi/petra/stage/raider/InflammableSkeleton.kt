package click.seichi.petra.stage.raider

import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Skeleton
import org.bukkit.inventory.ItemStack

/**
 * @author tar0ss
 */
class InflammableSkeleton(private val shouldBurnInDay: Boolean = false) : Raider(EntityType.SKELETON) {
    override fun onCreate(entity: Entity) {
        super.onCreate(entity)
        val living = entity as Skeleton
        living.equipment?.let {
            if (!shouldBurnInDay) {
                it.helmetDropChance = 0F
                it.helmet = ItemStack(Material.LEATHER_HELMET)
            }
        }
    }
}