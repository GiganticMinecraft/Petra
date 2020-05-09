package click.seichi.petra

/**
 * @author tar0ss
 */
class VoteCollector {
    private var agree: Int = 0
    private var disagree: Int = 0

    val voteNum
        get() = agree + disagree

    fun agree() {
        agree++
    }

    fun disagree() {
        disagree++
    }

    fun reset() {
        agree = 0
        disagree = 0
    }
}