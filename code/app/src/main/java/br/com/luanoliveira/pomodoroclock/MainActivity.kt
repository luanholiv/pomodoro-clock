package br.com.luanoliveira.pomodoroclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import br.com.luanoliveira.pomodoroclock.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel: PomodoroClockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = ActivityMainBinding.inflate(layoutInflater)

        val timeObserver = Observer<String> {newTime ->
            binding.timer.text = newTime
        }

        val pauseStateObserver = Observer<Boolean> { isPaused ->
            if (isPaused == true) {
                binding.playButton.visibility = View.VISIBLE
                binding.pauseButton.visibility = View.INVISIBLE
            } else {
                binding.playButton.visibility = View.INVISIBLE
                binding.pauseButton.visibility = View.VISIBLE
            }
        }

        viewModel.currentTime.observe(this, timeObserver)
        viewModel.isPaused.observe(this, pauseStateObserver)

        binding.viewmodel = viewModel

        setContentView(binding.root)
    }
}
