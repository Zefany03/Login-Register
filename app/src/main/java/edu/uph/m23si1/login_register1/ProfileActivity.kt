package edu.uph.m23si1.login_register1

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var edtFullName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtDOB: EditText
    private lateinit var spnGender: Spinner
    private lateinit var btnSave: Button
    private lateinit var btnBack: Button

    private lateinit var sharedPreferences: SharedPreferences
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)

        // Inisialisasi View
        edtFullName = findViewById(R.id.edtFullName)
        edtEmail = findViewById(R.id.edtEmail)
        edtDOB = findViewById(R.id.edtDOB)
        spnGender = findViewById(R.id.spnGender)
        btnSave = findViewById(R.id.btnSave)
        btnBack = findViewById(R.id.btnBack)

        // Setup Spinner Gender
        val genderOptions = arrayOf("Male", "Female", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnGender.adapter = adapter

        // Load data jika ada
        loadUserData()

        // Picker Tanggal
        edtDOB.setOnClickListener {
            showDatePicker()
        }

        // Simpan data
        btnSave.setOnClickListener {
            saveUserData()
        }

        // Kembali
        btnBack.setOnClickListener {
            finish() // atau ganti dengan startActivity(Intent(...)) untuk kembali ke dashboard
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val format = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                edtDOB.setText(format.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun loadUserData() {
        val name = sharedPreferences.getString("full_name", "")
        val email = sharedPreferences.getString("email", "")
        val dob = sharedPreferences.getString("date_of_birth", "")
        val gender = sharedPreferences.getString("gender", "Male")

        edtFullName.setText(name)
        edtEmail.setText(email)
        edtDOB.setText(dob)

        val genderIndex = when (gender) {
            "Male" -> 0
            "Female" -> 1
            else -> 2
        }
        spnGender.setSelection(genderIndex)
    }

    private fun saveUserData() {
        val name = edtFullName.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val dob = edtDOB.text.toString().trim()
        val gender = spnGender.selectedItem.toString()

        if (name.isEmpty()) {
            edtFullName.error = "Nama tidak boleh kosong"
            edtFullName.requestFocus()
            return
        }

        if (email.isEmpty()) {
            edtEmail.error = "Email tidak boleh kosong"
            edtEmail.requestFocus()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.error = "Format email tidak valid"
            edtEmail.requestFocus()
            return
        }

        sharedPreferences.edit().apply {
            putString("full_name", name)
            putString("email", email)
            putString("date_of_birth", dob)
            putString("gender", gender)
            apply()
        }

        Toast.makeText(this, "Profil berhasil disimpan", Toast.LENGTH_SHORT).show()
    }
}
