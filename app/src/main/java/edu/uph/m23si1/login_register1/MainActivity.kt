package edu.uph.m23si1.login_register1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginfirebase.RegisterActivity
import edu.uph.m23si1.login_register1.databinding.ActivityMainBinding // Pastikan import ini ada
import org.mindrot.jbcrypt.BCrypt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Deklarasi binding
    private lateinit var sharedPreferences: SharedPreferences

    private val HARDCODED_EMAIL = "user@example.com"
    private val HARDCODED_PASSWORD_HASH = BCrypt.hashpw("password123", BCrypt.gensalt())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater) // Inisialisasi binding
        setContentView(binding.root) // Menggunakan binding.root

        sharedPreferences = getSharedPreferences("MindCarePrefs", MODE_PRIVATE)

        checkLoginStatus()

        setupUI()
        setupClickListeners()
    }

    private fun checkLoginStatus() {
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            showWelcomeMessage()
        }
    }

    private fun setupUI() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets -> // Menggunakan binding.main
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener { // Menggunakan binding.btnLogin
            performLogin()
        }

        binding.tvSignUp.setOnClickListener { // Menggunakan binding.tvSignUp
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.tvForgotPassword.setOnClickListener { // Menggunakan binding.tvForgotPassword
            Toast.makeText(this, "Forgot password feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim() // Menggunakan binding.etEmail
        val password = binding.etPassword.text.toString().trim() // Menggunakan binding.etPassword

        if (!validateInput(email, password)) {
            return
        }

        binding.btnLogin.isEnabled = false
        binding.btnLogin.text = "Signing In..."

        Thread {
            Thread.sleep(1000)
            val loginSuccess = (email == HARDCODED_EMAIL && BCrypt.checkpw(password, HARDCODED_PASSWORD_HASH))

            runOnUiThread {
                binding.btnLogin.isEnabled = true
                binding.btnLogin.text = "Sign In"

                if (loginSuccess) {
                    val userId = 1
                    val userFullName = "Demo User"
                    saveLoginStatus(userId, userFullName, email)

                    Toast.makeText(this, "Welcome back, $userFullName!", Toast.LENGTH_LONG).show()
                    showWelcomeMessage()
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            binding.etEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Please enter a valid email"
            binding.etEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            binding.etPassword.requestFocus()
            return false
        }

        if (password.length < 6) {
            binding.etPassword.error = "Password must be at least 6 characters"
            binding.etPassword.requestFocus()
            return false
        }

        return true
    }

    private fun saveLoginStatus(userId: Int, fullName: String, email: String) {
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", true)
            putInt("userId", userId)
            putString("userFullName", fullName)
            putString("userEmail", email)
            putLong("loginTime", System.currentTimeMillis())
            apply()
        }
    }

    private fun showWelcomeMessage() {
        val userName = sharedPreferences.getString("userFullName", "User")
        Toast.makeText(this, "Welcome to MindCare+, $userName!", Toast.LENGTH_LONG).show()

        binding.cardLogin.alpha = 0.5f // Menggunakan binding.cardLogin
        binding.btnLogin.text = "Already Logged In"
        binding.btnLogin.isEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
