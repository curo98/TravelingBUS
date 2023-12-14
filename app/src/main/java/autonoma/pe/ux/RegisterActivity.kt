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

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPass = findViewById<EditText>(R.id.etPass)
        val etPassConfirm = findViewById<EditText>(R.id.etPassConfirm)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val tvGoToLogin = findViewById<TextView>(R.id.tvGoToLogin)

        val llFormLogin = findViewById<LinearLayout>(R.id.llFormLogin)
        val llLoader = findViewById<LinearLayout>(R.id.llLoader)
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)

        btnRegistrar.setOnClickListener {
            // Obtener los valores de los campos de texto
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPass.text.toString()
            val confirmPassword = etPassConfirm.text.toString()

            // Validar que los campos no estén vacíos y que las contraseñas coincidan
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword) {
                // Mostrar la animación Lottie en toda la pantalla
                llFormLogin.visibility = View.GONE
                llLoader.visibility = View.VISIBLE
                lottieAnimationView.visibility = View.VISIBLE
                lottieAnimationView.playAnimation()

                // Deshabilitar el botón de inicio de sesión mientras se muestra la animación
                btnRegistrar.isEnabled = false

                // Simular una carga con un retraso (puedes reemplazar esto con tu lógica real)
                Handler(Looper.getMainLooper()).postDelayed({
                    // Habilitar el botón de inicio de sesión antes de la transición
                    btnRegistrar.isEnabled = true

                    // Ocultar la animación Lottie antes de la transición
                    llLoader.visibility = View.GONE
                    lottieAnimationView.visibility = View.GONE

                    // Redirigir a otra actividad
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 2000) // Aquí 2000 representa el tiempo de simulación de carga en milisegundos
            } else {
                // Mostrar un mensaje de error si algún campo está vacío o las contraseñas no coinciden
                Toast.makeText(this, "Por favor, complete todos los campos y asegúrese de que las contraseñas coincidan", Toast.LENGTH_SHORT).show()
            }
        }
        tvGoToLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}