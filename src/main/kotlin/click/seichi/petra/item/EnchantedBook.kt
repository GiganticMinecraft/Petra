package click.seichi.petra.item

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

/**
 * @author tar0ss
 */
class EnchantedBook(
        enchants: Map<Enchantment, Int>,
        amount: Int
) : ItemStack(Material.ENCHANTED_BOOK, amount) {
    init {
        itemMeta = (itemMeta as EnchantmentStorageMeta).apply {
            enchants.forEach { (e, l) -> addStoredEnchant(e, l, true) }
        }
    }

    constructor(enchant: Enchantment, level: Int) : this(mapOf(enchant to level), 1)
}