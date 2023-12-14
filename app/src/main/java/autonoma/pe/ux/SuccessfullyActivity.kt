package autonoma.pe.ux

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import autonoma.pe.ux.util.ActionBarUtils

class SuccessfullyActivity : AppCompatActivity() {
    private lateinit var btnNextDetailTicket: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successfully)

        ActionBarUtils.setCustomTitle(
            this,
            "Compra existosa"
        )

        // Obtener los valores del intent
        val nameUser = intent.getStringExtra("nameUser")
        val numberCard = intent.getStringExtra("numberCard")
        val mm = intent.getStringExtra("mm")
        val aa = intent.getStringExtra("aa")
        val cvc = intent.getStringExtra("cvc")

        val origin = intent.getStringExtra("origin")
        val destination = intent.getStringExtra("destination")
        val nameBus = intent.getStringExtra("nameBus")
        val priceBus = intent.getStringExtra("priceBus")
        val currentDate = intent.getStringExtra("currentDate")
        val seatNumber = intent.getStringExtra("SEAT_NUMBER")

        btnNextDetailTicket = findViewById(R.id.btnNextDetailTicket)
        btnNextDetailTicket.setOnClickListener {
            // Crear un Intent para pasar a la siguiente actividad
            val intent = Intent(this@SuccessfullyActivity, ShowTicketActivity::class.java)

            // Agregar todos los valores como extras al Intent
            intent.putExtra("nameUser", nameUser)
            intent.putExtra("numberCard", numberCard)
            intent.putExtra("mm", mm)
            intent.putExtra("aa", aa)
            intent.putExtra("cvc", cvc)

            intent.putExtra("origin", origin)
            intent.putExtra("destination", destination)
            intent.putExtra("nameBus", nameBus)
            intent.putExtra("priceBus", priceBus)
            intent.putExtra("currentDate", currentDate)
            intent.putExtra("SEAT_NUMBER", seatNumber)

            // Iniciar la siguiente actividad
            startActivity(intent)
        }
    }
}