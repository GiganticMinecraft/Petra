package click.seichi.petra.stage.raider

import click.seichi.function.getNearestPlayer
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * @author tar0ss
 */
object Raiders {
    val INFLAMMABLE_ZOMBIE: Spawnable = object : StageEntity(EntityType.ZOMBIE), AutoTarget, Named {
        override fun onCreate(entity: Entity) {
            super.onCreate(entity)
            val zombie = entity as Zombie
            zombie.isBaby = false
            zombie.setShouldBurnInDay(false)
        }

        override fun findTarget(self: LivingEntity, players: Set<UUID>): LivingEntity? {
            return self.getNearestPlayer(players)
        }

        override fun getName(): String {
            return "アウトドア派ゾンビ"
        }
    }

    val CAPPED_SKELETON: Spawnable = object : StageEntity(EntityType.SKELETON), AutoTarget, Named {
        override fun onCreate(entity: Entity) {
            super.onCreate(entity)
            val living = entity as Skeleton
            living.equipment?.let {
                it.helmetDropChance = 0F
                it.helmet = ItemStack(Material.LEATHER_HELMET)
            }
        }

        override fun findTarget(self: LivingEntity, players: Set<UUID>): LivingEntity? {
            return self.getNearestPlayer(players)
        }

        override fun getName(): String {
            return "安全第一スケルトン"
        }
    }

}