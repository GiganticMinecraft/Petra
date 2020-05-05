package click.seichi.petra

/**
 * @author tar0ss
 */
enum class Stage(
        val key: String,
        // セーフゾーンの半径
        val radius: Int
) {
    PETRA_ONE("1stLayer", 64)
    ;

    companion object {
        val keyMap = values().map { it.key to it }.toMap()

        fun find(key: String) = keyMap[key]

        val DANGER_ZONE_LENGTH = 10
    }
}