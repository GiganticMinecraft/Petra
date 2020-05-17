package click.seichi.petra.listener

import org.bukkit.Difficulty
import org.bukkit.GameRule
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldInitEvent

/**
 * @author tar0ss
 */
class WorldListener : Listener {
    @EventHandler
    fun onWorldInit(event: WorldInitEvent) {
        val world = event.world
        world.setSpawnFlags(true, true)
        world.pvp = false
        world.difficulty = Difficulty.NORMAL
        world.isAutoSave = true
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true)
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
        world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false)
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
        world.setGameRule(GameRule.DISABLE_ELYTRA_MOVEMENT_CHECK, true)
        world.setGameRule(GameRule.DO_FIRE_TICK, false)
        world.setGameRule(GameRule.DO_LIMITED_CRAFTING, false)
        world.setGameRule(GameRule.SPAWN_RADIUS, 0)
        world.setGameRule(GameRule.DO_PATROL_SPAWNING, false)
        world.setGameRule(GameRule.DO_TRADER_SPAWNING, false)
        world.setGameRule(GameRule.DISABLE_RAIDS, true)
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)
        world.setGameRule(GameRule.DO_INSOMNIA, false)
        world.setGameRule(GameRule.NATURAL_REGENERATION, true)
        world.setGameRule(GameRule.MOB_GRIEFING, false)
        world.keepSpawnInMemory = true
        world.fullTime = 0
        world.isAutoSave = false
        world.monsterSpawnLimit = 100
        world.worldBorder.setCenter(0.0, 0.0)
        world.worldBorder.size = 1000.0
        world.worldBorder.warningDistance = 0
        world.worldBorder.warningTime = 0
    }
}