package br.com.luanoliveira.pomodoroclock

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import java.time.Duration

class PomodoroClock (
        private val workingSessionMillis: Long,
        val breakSessionMillis: Long,
        val intervalTick: Long
    ) {

    val currentTime: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    var isClockStarted: Boolean = false
    var isClockPaused: Boolean = false
    var isClockStopped: Boolean = true


    private lateinit var workingSession : CountDownTimer
    private lateinit var breakSession : CountDownTimer
    private var currentSession: CountDownTimer

    init{
        currentTime.value = convertDurationToTime(Duration.ofMillis(workingSessionMillis))

        createWorkingSession(workingSessionMillis)
        currentSession = workingSession

    }

    fun start() {
        currentSession.start()
    }

    fun pause() {

    }

    fun restart() {
        currentSession.cancel()
        createWorkingSession(workingSessionMillis)
        currentSession.start()
    }

    fun stop() {
        currentSession.cancel()
        currentTime.value = convertDurationToTime(Duration.ofMillis(workingSessionMillis))
    }

    private fun createWorkingSession(remainingTime: Long) {
        workingSession = object : CountDownTimer(remainingTime, intervalTick) {

            override fun onTick(millisUntilFinished: Long) {
                val duration = Duration.ofMillis(millisUntilFinished)
                currentTime.value = convertDurationToTime(duration)
            }

            override fun onFinish() {
                createBreakSession(breakSessionMillis)
                startSession(breakSession)
            }
        }
    }

    private fun createBreakSession(remainingTime: Long){
        breakSession = object : CountDownTimer(remainingTime, intervalTick) {

            override fun onTick(millisUntilFinished: Long) {
                val duration = Duration.ofMillis(millisUntilFinished)
                currentTime.value = convertDurationToTime(duration)
            }

            override fun onFinish() {
            }
        }
    }

    private fun startSession(timer: CountDownTimer){
        timer.start();
    }

    private fun convertDurationToTime(duration: Duration) : String {
        return String.format("%02d:%02d",
            duration.toMinutes(),
            duration.seconds % (duration.toMinutes() * 60))
    }
}