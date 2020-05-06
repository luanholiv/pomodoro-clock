package br.com.luanoliveira.pomodoroclock

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.Duration

class PomodoroClockViewModel : ViewModel() {

    val currentTime: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private lateinit var workingSession : CountDownTimer
    private lateinit var breakSession : CountDownTimer

    private var workingSessionMillis: Long
    private val breakSessionMillis: Long
    private val intervalTick: Long

    init{
        currentTime.value = ""
        workingSessionMillis = 1800000
        breakSessionMillis = 300000
        intervalTick = 1000
    }

    fun onPlay(){
        startWorkingSession(workingSessionMillis)
    }

    fun onStop(){
        TODO()
    }

    fun onRestart(){
        TODO()
    }

    private fun startWorkingSession(remainingTime: Long){
        workingSession = object : CountDownTimer(remainingTime, intervalTick) {

            override fun onTick(millisUntilFinished: Long) {
                val duration = Duration.ofMillis(millisUntilFinished)
                currentTime.setValue(convertDurationToTime(duration))
            }

            override fun onFinish() {
                startBreakSession(breakSessionMillis)
            }
        }

        workingSession.start();
    }

    private fun startBreakSession(remainingTime: Long){
        breakSession = object : CountDownTimer(remainingTime, intervalTick) {

            override fun onTick(millisUntilFinished: Long) {
                val duration = Duration.ofMillis(millisUntilFinished)
                currentTime.setValue(convertDurationToTime(duration))
            }

            override fun onFinish() {
            }
        }

        breakSession.start();
    }

    private fun convertDurationToTime(duration: Duration) : String {
        return String.format("%02d:%02d",
            duration.toMinutes(),
            duration.seconds % (duration.toMinutes() * 60))
    }
}