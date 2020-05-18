package click.seichi.petra.stage.raider

import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Zombie

/**
 * @author tar0ss
 */
class InflammableZombie(private val shouldBurnInDay: Boolean = false) : Raider(EntityType.ZOMBIE) {
    override fun onCreate(entity: Entity) {
        super.onCreate(entity)
        val zombie = entity as Zombie
        zombie.setShouldBurnInDay(shouldBurnInDay)
    }
}