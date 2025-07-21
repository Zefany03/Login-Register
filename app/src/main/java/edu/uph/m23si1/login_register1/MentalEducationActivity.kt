package edu.uph.m23si1.login_register1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MentalEducationActivity : AppCompatActivity() {

    private val journalTitles = arrayOf(
        "🧠 Mental Health Awareness - WHO",
        "💆 Stress Management Techniques",
        "🧘 Mindfulness for Wellness",
        "😔 Understanding Depression",
        "😰 Coping with Anxiety"
    )

    private val journalLinks = arrayOf(
        "https://www.who.int/news-room/fact-sheets/detail/mental-health-strengthening-our-response",
        "https://www.apa.org/topics/stress",
        "https://www.ncbi.nlm.nih.gov/pmc/articles/PMC3679190/",
        "https://www.nimh.nih.gov/health/topics/depression",
        "https://www.nimh.nih.gov/health/topics/anxiety-disorders"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mental_education)

        val listView = findViewById<ListView>(R.id.listViewJournals)
        val backButton = findViewById<Button>(R.id.btnBackToDashboard)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, journalTitles)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(journalLinks[position]))
            startActivity(intent)
        }

        backButton.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}
