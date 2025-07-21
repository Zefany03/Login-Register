package edu.uph.m23si1.login_register1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.uph.m23si1.login_register1.databinding.ActivityEmotiontrackerBinding

class EmotionTrackerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmotiontrackerBinding
    private lateinit var barChart: BarChart
    private val firestore = FirebaseFirestore.getInstance()

    private val emotionMap = mapOf(
        "Very Happy" to 0,
        "Happy" to 1,
        "Neutral" to 2,
        "Sad" to 3,
        "Very Sad" to 4,
        "Angry" to 5
    )

    private val emotionColors = listOf(
        Color.rgb(255, 193, 7),     // Very Happy
        Color.rgb(76, 175, 80),     // Happy
        Color.rgb(158, 158, 158),   // Neutral
        Color.rgb(33, 150, 243),    // Sad
        Color.rgb(63, 81, 181),     // Very Sad
        Color.rgb(244, 67, 54)      // Angry
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        binding = ActivityEmotiontrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        barChart = binding.barchart

        // Emotion button listeners
        binding.btnVeryHappy.setOnClickListener { saveEmotion("Very Happy") }
        binding.btnNeutral.setOnClickListener { saveEmotion("Neutral") }
        binding.btnSad.setOnClickListener { saveEmotion("Sad") }
        binding.btnVerySad.setOnClickListener { saveEmotion("Very Sad") }
        binding.btnAngry.setOnClickListener { saveEmotion("Angry") }

        // Logout button
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            getSharedPreferences("MindCarePrefs", MODE_PRIVATE).edit().clear().apply()
            Toast.makeText(this, "Logout sukses!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Back to Dashboard button
        binding.btnBackToDashboard.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        loadEmotionData()
    }

    private fun saveEmotion(emotion: String) {
        val data = hashMapOf(
            "emotion" to emotion,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("emotions")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(this, "✅ $emotion berhasil disimpan!", Toast.LENGTH_SHORT).show()
                loadEmotionData()
            }
            .addOnFailureListener {
                Toast.makeText(this, "❌ Gagal menyimpan emosi", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadEmotionData() {
        firestore.collection("emotions")
            .get()
            .addOnSuccessListener { result ->
                val counts = mutableMapOf<String, Int>()
                emotionMap.keys.forEach { counts[it] = 0 }

                for (document in result) {
                    val emotion = document.getString("emotion")
                    if (emotion != null && counts.containsKey(emotion)) {
                        counts[emotion] = counts[emotion]!! + 1
                    }
                }

                val entries = ArrayList<BarEntry>()
                val labels = ArrayList<String>()
                val colors = ArrayList<Int>()

                for ((emotion, index) in emotionMap) {
                    val count = counts[emotion] ?: 0
                    entries.add(BarEntry(index.toFloat(), count.toFloat()))
                    labels.add(emotion)
                    colors.add(emotionColors[index])
                }

                val dataSet = BarDataSet(entries, "Emosi Mingguan").apply {
                    setColors(colors)
                    valueTextSize = 12f
                    valueTextColor = Color.BLACK
                }

                val barData = BarData(dataSet).apply {
                    barWidth = 0.9f
                }

                barChart.apply {
                    data = barData
                    setFitBars(true)
                    description.isEnabled = false
                    animateY(1000)
                    legend.isEnabled = false

                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawLabels(true)
                        granularity = 1f
                        setLabelCount(labels.size)
                        valueFormatter = IndexAxisValueFormatter(labels)
                        textSize = 12f
                        textColor = Color.DKGRAY
                    }

                    axisLeft.textColor = Color.DKGRAY
                    axisRight.isEnabled = false
                    invalidate()
                }

                // Update stat tracking
                binding.tvStatTracking.text = "Konsistensi Tracking: ${result.size()}/30 hari"
                binding.tvStatHappyDays.text = "Hari Bahagia: ${counts["Very Happy"] ?: 0}/30 hari"
            }
    }
}
