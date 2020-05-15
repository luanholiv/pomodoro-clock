package br.com.luanoliveira.pomodoroclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        viewModel.currentTime.observe(this, timeObserver)

        binding.viewmodel = viewModel

        setContentView(binding.root)
    }
}
