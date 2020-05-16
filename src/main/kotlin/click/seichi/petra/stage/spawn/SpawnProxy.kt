package click.seichi.petra.stage.spawn

import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.util.Consumer

/**
 * @author tar0ss
 */
interface SpawnProxy {
    fun <T : Entity> spawn(world: World, clazz: Class<T>, function: Consumer<T>? = null)
}