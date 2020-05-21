package br.com.luanoliveira.pomodoroclock

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PomodoroClockViewModel : ViewModel() {
    private val workingSessionMillis: Long = 15000
    private val breakSessionMillis: Long = 3000
    private val longBreakSessionMillis: Long = 6000
    private val intervalTick: Long= 1000
    private val pomodoroClock: PomodoroClock

    init{
        pomodoroClock = PomodoroClock(workingSessionMillis, breakSessionMillis, longBreakSessionMillis, intervalTick)
    }

    val currentTime: MutableLiveData<String>
        get () = pomodoroClock.currentTime

    val isWorkingSession: MutableLiveData<Boolean>
        get () = pomodoroClock.isWorkingSession

    val isPaused: MutableLiveData<Boolean>
        get () = pomodoroClock.isPaused

    val breakSessionCount: MutableLiveData<Int>
        get () = pomodoroClock.breakSessionCount

    fun onPlay(){
        pomodoroClock.start()
    }

    fun onPause(){
        pomodoroClock.pause()
    }

    fun onStop(){
        pomodoroClock.stop()
    }

    fun onRestart(){
        pomodoroClock.restart()
    }
}