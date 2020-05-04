package click.seichi.petra

/**
 * @author tar0ss
 */
enum class Stage(
        val key: String
) {
    PETRA_ONE("1stLayer")
    ;

    companion object {
        val keyMap = values().map { it.key to it }.toMap()

        fun find(key: String) = keyMap[key]
    }
}