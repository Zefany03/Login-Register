package edu.uph.m23si1.login_register1

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.firestore.FirebaseFirestore
import edu.uph.m23si1.login_register1.databinding.ActivityEmotiontrackerBinding

class EmotionTrackerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmotiontrackerBinding
    private lateinit var barChart: BarChart
    private val firestore = FirebaseFirestore.getInstance()

    private val emotionMap = mapOf(
        "Very Happy" to 1,
        "Happy" to 2,
        "Neutral" to 3,
        "Sad" to 4,
        "Very Sad" to 5,
        "Angry" to 6,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmotiontrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        barChart = binding.barchart

        findViewById<LinearLayout>(R.id.btnVeryHappy).setOnClickListener { saveEmotion("Very Happy") }
        findViewById<LinearLayout>(R.id.btnNeutral).setOnClickListener { saveEmotion("Neutral") }
        findViewById<LinearLayout>(R.id.btnSad).setOnClickListener { saveEmotion("Sad") }
        findViewById<LinearLayout>(R.id.btnVerySad).setOnClickListener { saveEmotion("Very Sad") }
        findViewById<LinearLayout>(R.id.btnAngry).setOnClickListener { saveEmotion("Angry") }

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
                loadEmotionData()
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
                var index = 0f

                for ((emotion, value) in counts) {
                    entries.add(BarEntry(index, value.toFloat()))
                    labels.add(emotion)
                    index += 1f
                }

                val dataSet = BarDataSet(entries, "Emotions")
                val barData = BarData(dataSet)
                barChart.data = barData

                val xAxis = barChart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawLabels(true)
                xAxis.granularity = 1f
                xAxis.setLabelCount(labels.size)
                xAxis.valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(labels)

                barChart.invalidate()
            }
    }
}
