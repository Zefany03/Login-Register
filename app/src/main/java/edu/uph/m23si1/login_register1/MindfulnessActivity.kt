package edu.uph.m23si1.login_register1

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MindfulnessActivity : AppCompatActivity() {

    private lateinit var breathingCircle: ImageView
    private lateinit var instructionText: TextView
    private lateinit var timerText: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var backButton: Button

    private var breathingTimer: CountDownTimer? = null
    private var breathingAnimation: AnimatorSet? = null

    private var isBreathing = false
    private var currentPhase = 0
    private val phases = arrayOf("Tarik Napas", "Tahan", "Buang Napas", "Tahan")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mindfulness)

        breathingCircle = findViewById(R.id.breathingCircle)
        instructionText = findViewById(R.id.instructionText)
        timerText = findViewById(R.id.timerText)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        backButton = findViewById(R.id.backButton)

        startButton.setOnClickListener { startBreathingExercise() }
        stopButton.setOnClickListener { stopBreathingExercise() }
        backButton.setOnClickListener {
            finish() // atau startActivity(Intent(this, DashboardActivity::class.java))
        }
    }

    private fun startBreathingExercise() {
        isBreathing = true
        currentPhase = 0
        startButton.isEnabled = false
        stopButton.isEnabled = true
        timerText.visibility = View.VISIBLE
        startBreathingCycle()
    }

    private fun startBreathingCycle() {
        if (!isBreathing) return

        instructionText.text = phases[currentPhase]

        breathingTimer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = (millisUntilFinished / 1000 + 1).toString()
            }

            override fun onFinish() {
                currentPhase = (currentPhase + 1) % 4
                if (isBreathing) startBreathingCycle()
            }
        }.start()

        startBreathingAnimation()
    }

    private fun startBreathingAnimation() {
        breathingAnimation?.cancel()

        val (startScale, endScale) = when (currentPhase) {
            0 -> 1.0f to 1.3f
            2 -> 1.3f to 1.0f
            else -> breathingCircle.scaleX to breathingCircle.scaleX
        }

        val scaleX = ObjectAnimator.ofFloat(breathingCircle, "scaleX", startScale, endScale)
        val scaleY = ObjectAnimator.ofFloat(breathingCircle, "scaleY", startScale, endScale)

        scaleX.duration = 4000
        scaleY.duration = 4000
        scaleX.interpolator = AccelerateDecelerateInterpolator()
        scaleY.interpolator = AccelerateDecelerateInterpolator()

        breathingAnimation = AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            start()
        }
    }

    private fun stopBreathingExercise() {
        isBreathing = false
        breathingTimer?.cancel()
        breathingAnimation?.cancel()
        startButton.isEnabled = true
        stopButton.isEnabled = false
        instructionText.text = "Tekan Mulai"
        timerText.visibility = View.GONE
        breathingCircle.scaleX = 1.0f
        breathingCircle.scaleY = 1.0f
    }

    override fun onDestroy() {
        super.onDestroy()
        stopBreathingExercise()
    }
}
