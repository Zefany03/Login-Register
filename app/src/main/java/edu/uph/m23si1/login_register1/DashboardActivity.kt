package edu.uph.m23si1.login_register1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.uph.m23si1.login_register1.databinding.ActivityDashboardBinding
import edu.uph.m23si1.login_register1.databinding.ActivityMindfulnessBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigasi ke masing-masing fitur
        binding.cardMindfulness.setOnClickListener {
            val intent = Intent(this, MindfulnessActivity::class.java)
            startActivity(intent)
        }

        binding.cardEducation.setOnClickListener {
            val intent = Intent(this, MentalEducationActivity::class.java)
            startActivity(intent)
        }

        binding.cardProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.cardEmotionTracker.setOnClickListener {
            val intent = Intent(this, EmotionTrackerActivity::class.java)
            startActivity(intent)
        }
    }
}
