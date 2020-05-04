package click.seichi

/**
 * @author tar0ss
 */
enum class ServerDefinition(
        val id: Int,
        val bungeeName: String
) {
    PETRA_ONE(1, "p1")
    ;

    companion object {
        val idMap = values().map { it.id to it }.toMap()
        val bungeeNameMap = values().map { it.bungeeName to it }.toMap()
        val bungeeNameArray = bungeeNameMap.keys.toTypedArray()

        fun findById(id: Int) = idMap[id]
        fun findByBungeeName(bungeeName: String) = bungeeNameMap[bungeeName]
    }
}