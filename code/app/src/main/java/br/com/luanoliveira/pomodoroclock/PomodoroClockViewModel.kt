package br.com.luanoliveira.pomodoroclock

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.Duration

class PomodoroClockViewModel : ViewModel() {

    val currentTime: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val clockState: MutableLiveData<ClockState> by lazy {
        MutableLiveData<ClockState>()
    }

    val sessionState: MutableLiveData<SessionState> by lazy {
        MutableLiveData<SessionState>()
    }

    private lateinit var workingSession : CountDownTimer
    private lateinit var breakSession : CountDownTimer

    private var workingSessionMillis: Long
    private val breakSessionMillis: Long
    private val intervalTick: Long

    init{
        clockState.value = ClockState.STOPPED
        sessionState.value = SessionState.NONE
        workingSessionMillis = 1500000
        breakSessionMillis = 300000
        intervalTick = 1000
        currentTime.value = convertDurationToTime(Duration.ofMillis(workingSessionMillis))
    }

    fun onPlay(){
        clockState.value = ClockState.STARTED
        startWorkingSession(workingSessionMillis)
    }

    fun onPause(){
        clockState.value = ClockState.PAUSED
    }

    fun onStop(){
        clockState.value = ClockState.STOPPED

        val session:CountDownTimer = if (sessionState == SessionState.WORKING){
            workingSession
        } else {
            breakSession
        }

        stopSession(session);

        currentTime.value = convertDurationToTime(Duration.ofMillis(workingSessionMillis))
    }

    fun onRestart(){
        onStop()
        onPlay()
    }

    private fun startWorkingSession(remainingTime: Long){
        sessionState.value = SessionState.WORKING

        workingSession = object : CountDownTimer(remainingTime, intervalTick) {

            override fun onTick(millisUntilFinished: Long) {
                val duration = Duration.ofMillis(millisUntilFinished)
                currentTime.value = convertDurationToTime(duration)
            }

            override fun onFinish() {
                startBreakSession(breakSessionMillis)
            }
        }

        workingSession.start();
    }

    private fun startBreakSession(remainingTime: Long){
        sessionState.value = SessionState.BREAK

        breakSession = object : CountDownTimer(remainingTime, intervalTick) {

            override fun onTick(millisUntilFinished: Long) {
                val duration = Duration.ofMillis(millisUntilFinished)
                currentTime.value = convertDurationToTime(duration)
            }

            override fun onFinish() {
                sessionState.value = SessionState.NONE
            }
        }

        breakSession.start();
    }

    private fun stopSession(session: CountDownTimer){
        session.cancel()
    }

    private fun convertDurationToTime(duration: Duration) : String {
        return String.format("%02d:%02d",
            duration.toMinutes(),
            duration.seconds % (duration.toMinutes() * 60))
    }
}