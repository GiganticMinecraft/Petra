package click.seichi.petra.stage.summoner

import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Skeleton
import org.bukkit.entity.Zombie
import org.bukkit.inventory.ItemStack

/**
 * @author tar0ss
 */
object Summoners {
    val INFLAMMABLE_ZOMBIE: ISummoner = object : Summoner(EntityType.ZOMBIE), Named {
        override fun onCreate(entity: Entity) {
            super.onCreate(entity)
            val zombie = entity as Zombie
            zombie.isBaby = false
            zombie.setShouldBurnInDay(false)
        }

        override fun getName(): String {
            return "アウトドア派ゾンビ"
        }
    }

    val CAPPED_SKELETON: ISummoner = object : Summoner(EntityType.SKELETON), Named {
        override fun onCreate(entity: Entity) {
            super.onCreate(entity)
            val living = entity as Skeleton
            living.equipment?.let {
                it.helmetDropChance = 0F
                it.helmet = ItemStack(Material.LEATHER_HELMET)
            }
        }

        override fun getName(): String {
            return "安全第一スケルトン"
        }
    }

}