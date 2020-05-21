package br.com.luanoliveira.pomodoroclock

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import java.time.Duration

class PomodoroClock (
        private val workingSessionMillis: Long,
        private val breakSessionMillis: Long,
        private val longBreakSessionMillis: Long,
        private val intervalTick: Long
    ) {

    val currentTime: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val isWorkingSession: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isPaused = MutableLiveData<Boolean>().apply{
        value = true
    }

    val breakSessionCount = MutableLiveData<Int>().apply{
        value = 0
    }

    private lateinit var currentSession: CountDownTimer
    private var remainingTime: Long

    init {
        currentTime.value = convertDurationToTime(Duration.ofMillis(workingSessionMillis))
        remainingTime = workingSessionMillis
        createWorkingSession(workingSessionMillis)
    }

    fun start() {
        startCurrentSession()
    }

    fun pause() {
        pauseCurrentSession()
    }

    fun restart() {
        stopCurrentSession()
        createWorkingSession(workingSessionMillis)
        startCurrentSession()
    }

    fun stop() {
        stopCurrentSession()
        currentTime.value = convertDurationToTime(Duration.ofMillis(workingSessionMillis))
    }

    private fun createWorkingSession(totalTime: Long) {
        currentSession = object : CountDownTimer(totalTime, intervalTick) {

            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                val duration = Duration.ofMillis(millisUntilFinished)
                currentTime.value = convertDurationToTime(duration)
            }

            override fun onFinish() {
                stopCurrentSession()

                if (breakSessionCount.value == 4) {
                    createBreakSession(longBreakSessionMillis)
                }
                else {
                    createBreakSession(breakSessionMillis)
                }

                startCurrentSession()
            }
        }

        isWorkingSession.value = true;
    }

    private fun createBreakSession(totalTime: Long) {
        currentSession = object : CountDownTimer(totalTime, intervalTick) {

            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                val duration = Duration.ofMillis(millisUntilFinished)
                currentTime.value = convertDurationToTime(duration)
            }

            override fun onFinish() {
                stopCurrentSession()
                increaseBreakSessionCount()
                createWorkingSession(workingSessionMillis)

                if (breakSessionCount.value == 5) {
                    resetBreakSessionCount()
                }
            }
        }

        isWorkingSession.value = false;
    }

    private fun startCurrentSession() {
        isPaused.value = false
        currentSession.start()
    }

    private fun stopCurrentSession() {
        isPaused.value = true
        currentSession.cancel()
    }

    private fun pauseCurrentSession() {
        isPaused.value = true
        currentSession.cancel()

        if (isWorkingSession.value == true) {
            createWorkingSession(remainingTime)
        } else {
            createBreakSession((remainingTime))
        }
    }

    private fun increaseBreakSessionCount() {
        breakSessionCount.value?.let { a -> breakSessionCount.value = a + 1 }
    }

    private fun resetBreakSessionCount() {
        breakSessionCount.value = 0
    }

    private fun convertDurationToTime(duration: Duration) : String {
        val minutes = duration.toMinutes()
        val seconds = if (minutes > 0)
        {
            duration.seconds % (duration.toMinutes() * 60)
        } else {
            duration.seconds
        }

        return String.format("%02d:%02d", minutes, seconds)
    }
}