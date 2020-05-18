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

    private lateinit var currentSession: CountDownTimer

    init{
        currentTime.value = convertDurationToTime(Duration.ofMillis(workingSessionMillis))
        createWorkingSession(workingSessionMillis)
    }

    fun start() {
        startCurrentSession()
    }

    fun pause() {

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

    private fun createWorkingSession(remainingTime: Long) {
        currentSession = object : CountDownTimer(remainingTime, intervalTick) {

            override fun onTick(millisUntilFinished: Long) {
                val duration = Duration.ofMillis(millisUntilFinished)
                currentTime.value = convertDurationToTime(duration)
            }

            override fun onFinish() {
                stopCurrentSession()
                createBreakSession(breakSessionMillis)
                startCurrentSession()
            }
        }
    }

    private fun createBreakSession(remainingTime: Long){
        currentSession = object : CountDownTimer(remainingTime, intervalTick) {

            override fun onTick(millisUntilFinished: Long) {
                val duration = Duration.ofMillis(millisUntilFinished)
                currentTime.value = convertDurationToTime(duration)
            }

            override fun onFinish() {
                stopCurrentSession()
                createWorkingSession(workingSessionMillis)
            }
        }
    }

    private fun startCurrentSession(){
        currentSession.start()
    }

    private fun stopCurrentSession(){
        currentSession.cancel()
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