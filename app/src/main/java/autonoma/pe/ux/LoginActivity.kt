package autonoma.pe.ux

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        // En tu actividad Kotlin
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvGoToRegister = findViewById<TextView>(R.id.tvGoToRegister)
        val llFormLogin = findViewById<LinearLayout>(R.id.llFormLogin)
        val llLoader = findViewById<LinearLayout>(R.id.llLoader)
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)

        btnLogin.setOnClickListener {
            // Obtener los valores de los campos de texto
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            // Validar que los campos no estén vacíos
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Mostrar la animación Lottie en toda la pantalla
                llFormLogin.visibility = View.GONE
                llLoader.visibility = View.VISIBLE
                lottieAnimationView.visibility = View.VISIBLE
                lottieAnimationView.playAnimation()

                // Deshabilitar el botón de inicio de sesión mientras se muestra la animación
                btnLogin.isEnabled = false

                // Simular una carga con un retraso (puedes reemplazar esto con tu lógica real)
                Handler(Looper.getMainLooper()).postDelayed({
                    // Habilitar el botón de inicio de sesión antes de la transición
                    btnLogin.isEnabled = true

                    // Ocultar la animación Lottie antes de la transición
                    llLoader.visibility = View.GONE
                    lottieAnimationView.visibility = View.GONE

                    // Redirigir a otra actividad
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 2000) // Aquí 2000 representa el tiempo de simulación de carga en milisegundos
            } else {
                // Mostrar un mensaje de error si algún campo está vacío
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        tvGoToRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}