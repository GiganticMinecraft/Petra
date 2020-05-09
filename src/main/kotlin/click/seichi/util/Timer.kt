package click.seichi.util

import click.seichi.function.sync
import click.seichi.function.warning

/**
 * @author tar0ss
 */
open class Timer(
        private val finishSeconds: Long,
        private val onStart: () -> Unit,
        /**
         * ex) [finishSeconds] = 5の時
         * i = 5,4,3,2,1,0
         */
        private val onNext: (Long) -> Unit,
        private val onComplete: () -> Unit,
        private val onCancelled: () -> Unit
) {

    private var isStarted = false
    private var isCancelled = false

    fun start() {
        if (isStarted) {
            warning("Timer is already started.")
            return
        }
        isStarted = true
        onStart()

        sync(0L, 20L) { elapsedSeconds ->
            if (isCancelled) {
                onCancelled()
                return@sync true
            }

            val remainSeconds = finishSeconds - elapsedSeconds

            onNext(remainSeconds)

            if (remainSeconds <= 0) {
                onComplete()
                return@sync true
            }
            return@sync false
        }
    }

    fun cancel() {
        isCancelled = true
    }

}