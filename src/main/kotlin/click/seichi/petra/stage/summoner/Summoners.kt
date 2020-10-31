package click.seichi.petra.stage.summoner

import click.seichi.petra.function.sync
import click.seichi.petra.message.ChatMessage
import click.seichi.petra.util.Random
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.sqrt

/**
 * @author tar0ss
 */
object Summoners {
    //region zombie
    val INFLAMMABLE_ZOMBIE: ISummoner = object : Summoner(EntityType.ZOMBIE, { sqrt(it.toDouble()).toInt() }), Named {
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

    val ZOMBIE_IMITATED_SKELETON: ISummoner = object : Summoner(EntityType.ZOMBIE, { sqrt(it.toDouble()).toInt() }), Named {
        override fun onCreate(entity: Entity) {
            super.onCreate(entity)
            val zombie = entity as Zombie
            zombie.isBaby = false
            zombie.equipment?.apply {
                this.helmet = ItemStack(Material.SKELETON_SKULL)
                this.helmetDropChance = 0.03F
                this.chestplate = ItemStack(Material.LEATHER_CHESTPLATE).apply {
                    if (this is Damageable) damage = Random.nextInt(40, 70)
                }
                this.chestplateDropChance = 0.1F
                this.leggings = ItemStack(Material.LEATHER_LEGGINGS).apply {
                    if (this is Damageable) damage = Random.nextInt(40, 70)
                }
                this.leggingsDropChance = 0.1F
                this.boots = ItemStack(Material.LEATHER_BOOTS).apply {
                    if (this is Damageable) damage = Random.nextInt(40, 60)
                }
                this.bootsDropChance = 0.1F
                this.setItemInMainHand(ItemStack(Material.BOW).apply {
                    if (this is Damageable) damage = Random.nextInt(200, 350)
                })
                this.itemInMainHandDropChance = 0.1F
            }
        }

        override fun getName(): String {
            return "スケルトン?"
        }
    }

    val ZOMBIE_IMITATED_PLAYER: (String?) -> ISummoner = { target: String? ->
        object : Summoner(EntityType.ZOMBIE, { sqrt(it.toDouble()).toInt() }), Named {
            private lateinit var name: String
            override fun onCreate(entity: Entity) {
                super.onCreate(entity)
                var targetPlayer: Player? = null
                if (target != null) {
                    targetPlayer = Bukkit.getServer().getPlayer(target)
                }
                val player = targetPlayer ?: Bukkit.getServer().onlinePlayers
                        .filterNotNull()
                        .filter { it.gameMode == GameMode.SURVIVAL }
                        .random()
                name = player.name
                val playerEquip = player.equipment
                val zombie = entity as Zombie
                zombie.isBaby = false
                zombie.equipment?.apply {
                    this.helmet = ItemStack(Material.PLAYER_HEAD).apply {
                        val skullMeta = itemMeta as SkullMeta
                        skullMeta.owningPlayer = player
                        this.itemMeta = skullMeta
                    }
                    // TODO 鎧着せたい
                    this.helmetDropChance = 1.0F
                    playerEquip?.chestplate?.type?.let {
                        this.chestplate = ItemStack(it)
                        this.chestplateDropChance = 0.0F
                    }
                    playerEquip?.leggings?.type?.let {
                        this.leggings = ItemStack(it)
                        this.leggingsDropChance = 0.0F
                    }
                    playerEquip?.boots?.type?.let {
                        this.boots = ItemStack(it)
                        this.bootsDropChance = 0.0F
                    }
                    playerEquip?.itemInMainHand?.type?.let {
                        this.setItemInMainHand(ItemStack(it))
                        this.itemInMainHandDropChance = 0.0F
                    }
                }
            }

            override fun getName(): String {
                return name
            }
        }
    }
    //endregion

    //region skeleton
    val HONEBUTO: ISummoner = object : Summoner(EntityType.SKELETON), Named {
        override fun onCreate(entity: Entity) {
            super.onCreate(entity)
            val skeleton = entity as Skeleton
            skeleton.equipment?.let {
                val bow = ItemStack(Material.BOW)
                bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2)
                it.itemInMainHandDropChance = 1.0F
                it.setItemInMainHand(bow)
            }

            skeleton.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10000, 3, false, false, false))
            skeleton.addPotionEffect(PotionEffect(PotionEffectType.HEALTH_BOOST, 10000, 3, false, false, false))
            skeleton.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 10000, 4, false, false, false))
            skeleton.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 10000, 1, false, false, false))
        }

        override fun getName(): String {
            return "ほねぶと"
        }
    }

    val HONEBUTO_KURO: ISummoner = object : Summoner(EntityType.WITHER_SKELETON), Named {
        override fun onCreate(entity: Entity) {
            super.onCreate(entity)
            val wSkeleton = entity as WitherSkeleton
            wSkeleton.equipment?.let {
                val sword = ItemStack(Material.DIAMOND_SWORD)
                sword.addEnchantment(Enchantment.FIRE_ASPECT, 1)
                sword.addEnchantment(Enchantment.KNOCKBACK, 2)
                sword.addEnchantment(Enchantment.DAMAGE_ALL, 4)
                it.itemInMainHandDropChance = 1.0F
                it.setItemInMainHand(sword)
            }

            wSkeleton.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10000, 3, false, false, false))
            wSkeleton.addPotionEffect(PotionEffect(PotionEffectType.HEALTH_BOOST, 10000, 3, false, false, false))
            wSkeleton.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 10000, 4, false, false, false))
            wSkeleton.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 10000, 1, false, false, false))
        }

        override fun getName(): String {
            return "ほねぶと(黒)"
        }
    }

    val KIMETSU_TEONI: ISummoner = object : Summoner(EntityType.WITHER_SKELETON), Named {
        override fun onCreate(entity: Entity) {
            super.onCreate(entity)
            val wSkeleton = entity as WitherSkeleton
            wSkeleton.equipment?.let {
                val sword = ItemStack(Material.DIAMOND_SWORD)
                sword.addEnchantment(Enchantment.FIRE_ASPECT, 1)
                sword.addEnchantment(Enchantment.KNOCKBACK, 2)
                sword.addEnchantment(Enchantment.DAMAGE_ALL, 4)
                it.itemInMainHandDropChance = 1.0F
                it.setItemInMainHand(sword)
            }

            wSkeleton.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10000, 3, false, false, false))
            wSkeleton.addPotionEffect(PotionEffect(PotionEffectType.HEALTH_BOOST, 10000, 3, false, false, false))
            wSkeleton.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 10000, 4, false, false, false))
            wSkeleton.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 10000, 1, false, false, false))
            wSkeleton.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 10000, 2, false, false, false))
        }

        override fun getName(): String {
            return "テオニー"
        }
    }


    val CAPPED_SKELETON: ISummoner = object : Summoner(EntityType.SKELETON, { 1 }), Named {
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

    //endregion

    //region spider
    val RAIN_BRAIN: ISummoner = object : Summoner(EntityType.SPIDER, { 1 }), Named {
        override fun onCreate(entity: Entity) {
            super.onCreate(entity)
            val spider = entity as Spider

            spider.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 10000, 1, true, true))
            spider.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 10000, 4, true, true))
            spider.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10000, 3, true, true))
            val spiderUniqueId = spider.uniqueId
            val world = spider.world
            if (world.hasStorm()) return
            ChatMessage("${ChatColor.BLUE}嵐の予感..").broadcast()
            world.setStorm(true)
            world.isThundering = true
            world.thunderDuration = 2 * 20
            sync(0L, 100L) {
                val e = Bukkit.getServer().getEntity(spiderUniqueId)
                if (e != null && e.isValid && !e.isDead) return@sync true
                else {
                    ChatMessage("${ChatColor.BLUE}嵐が過ぎ去った").broadcast()
                    world.setStorm(false)
                    world.isThundering = false
                    return@sync false
                }
            }
        }

        override fun getName(): String {
            return "アメフラシ"
        }
    }

    val KIMETSU_RUI: ISummoner = object : Summoner(EntityType.SPIDER, { 1 }), Named {
        override fun onCreate(entity: Entity) {
            super.onCreate(entity)
            val spider = entity as Spider
            spider.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 10000, 1, true, true))
            spider.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 10000, 4, true, true))
            spider.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10000, 3, true, true))
            val spiderUniqueId = spider.uniqueId
            ChatMessage("${ChatColor.LIGHT_PURPLE}僕たち家族の静かな暮らしを邪魔するな").broadcast()
            sync(0L, 100L) {
                val e = Bukkit.getServer().getEntity(spiderUniqueId)
                if (e != null && e.isValid && !e.isDead) return@sync true
                else {
                    ChatMessage("${ChatColor.RED}バカな...糸が焼き切れた!?").broadcast()
                    return@sync false
                }
            }
        }

        override fun getName(): String {
            return "ルイ"
        }
    }

    //endregion

    val YOUJO: (String?) -> ISummoner = { target: String? ->
        object : Summoner(EntityType.VILLAGER, { sqrt(it.toDouble()).toInt() }, SummonCase.CENTER), Named {
            private lateinit var name: String
            override fun onCreate(entity: Entity) {
                super.onCreate(entity)
                var targetPlayer: Player? = null
                if (target != null) {
                    targetPlayer = Bukkit.getServer().getPlayer(target)
                }
                val player = targetPlayer ?: Bukkit.getServer().onlinePlayers
                        .filterNotNull()
                        .filter { it.gameMode == GameMode.SURVIVAL }
                        .random()
                name = "ょぅι゛ょ"
                val vi = entity as Villager
                vi.setBaby()
                vi.equipment?.apply {
                    this.helmet = ItemStack(Material.PLAYER_HEAD).apply {
                        val skullMeta = itemMeta as SkullMeta
                        skullMeta.owningPlayer = player
                        this.itemMeta = skullMeta
                    }
                    this.helmetDropChance = 0.0F
                    this.chestplate = ItemStack(Material.IRON_CHESTPLATE)
                    this.chestplateDropChance = 0.0F
                    this.leggings = ItemStack(Material.IRON_LEGGINGS)
                    this.leggingsDropChance = 0.0F
                    this.boots = ItemStack(Material.IRON_BOOTS)
                    this.bootsDropChance = 0.0F
                }

                vi.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10000, 2, false, false, false))
                vi.addPotionEffect(PotionEffect(PotionEffectType.HEALTH_BOOST, 10000, 3, false, false, false))
                vi.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 10000, 3, false, false, false))
                vi.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 10000, 1, false, false, false))
            }

            override fun getName(): String {
                return name
            }
        }
    }

    val KIMETSU_NEZUKO: (String?) -> ISummoner = { target: String? ->
        object : Summoner(EntityType.VILLAGER, { sqrt(it.toDouble()).toInt() }, SummonCase.CENTER), Named {
            private lateinit var name: String
            override fun onCreate(entity: Entity) {
                super.onCreate(entity)
                var targetPlayer: Player? = null
                if (target != null) {
                    targetPlayer = Bukkit.getServer().getPlayer(target)
                }
                val player = targetPlayer ?: Bukkit.getServer().onlinePlayers
                        .filterNotNull()
                        .filter { it.gameMode == GameMode.SURVIVAL }
                        .random()
                name = "ネズーコ"
                val vi = entity as Villager
                vi.setBaby()
                vi.equipment?.apply {
                    this.helmet = ItemStack(Material.PLAYER_HEAD).apply {
                        val skullMeta = itemMeta as SkullMeta
                        skullMeta.owningPlayer = player
                        this.itemMeta = skullMeta
                    }
                    this.helmetDropChance = 0.0F
                    this.chestplate = ItemStack(Material.IRON_CHESTPLATE)
                    this.chestplateDropChance = 0.0F
                    this.leggings = ItemStack(Material.IRON_LEGGINGS)
                    this.leggingsDropChance = 0.0F
                    this.boots = ItemStack(Material.IRON_BOOTS)
                    this.bootsDropChance = 0.0F
                }

                vi.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10000, 2, false, false, false))
                vi.addPotionEffect(PotionEffect(PotionEffectType.HEALTH_BOOST, 10000, 3, false, false, false))
                vi.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 10000, 3, false, false, false))
                vi.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 10000, 1, false, false, false))
            }

            override fun getName(): String {
                return name
            }
        }
    }
}