package br.com.luanoliveira.pomodoroclock

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PomodoroClockViewModel : ViewModel() {
    private val workingSessionMillis: Long = 1500000
    private val breakSessionMillis: Long= 300000
    private val intervalTick: Long= 1000
    private val pomodoroClock: PomodoroClock

    init{
        pomodoroClock = PomodoroClock(workingSessionMillis, breakSessionMillis, intervalTick)
    }

    val currentTime: MutableLiveData<String>
        get () = pomodoroClock.currentTime

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