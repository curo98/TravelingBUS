package autonoma.pe.ux

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import autonoma.pe.ux.models.BusCompany
import autonoma.pe.ux.util.ActionBarUtils
import autonoma.pe.ux.util.PreferenceHelper
import autonoma.pe.ux.util.PreferenceHelper.get
import com.squareup.picasso.Picasso

class BusSelectionActivity : AppCompatActivity() {

    private lateinit var tvOrigin: TextView
    private lateinit var tvDestination: TextView

    private val tvPrice by lazy { findViewById<TextView>(R.id.tvPrice) }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_selection)

        ActionBarUtils.setCustomTitle(
            this,
            "Buses disponible"
        )

        val buses = intent.getParcelableArrayListExtra<BusCompany>("buses")

        tvOrigin = findViewById(R.id.tvOrigin)
        tvDestination = findViewById(R.id.tvDestination)

        val userLoc = preferences["userLocation", ""]
        val userDest = preferences["userDestination", ""]
        // Establecer los valores en los TextViews
        tvOrigin.text = userLoc
        tvDestination.text = userDest

        val containerLayout = findViewById<LinearLayout>(R.id.containerLayout)

        if (buses != null) {
            for (busCompany in buses) {
                val cardView =
                    layoutInflater.inflate(R.layout.item_bus, containerLayout, false) as CardView

                // Configurar los elementos de la CardView
                val tvNameBus = cardView.findViewById<TextView>(R.id.tvNameBus)
                val ivBus = cardView.findViewById<ImageView>(R.id.ivBus)
                val tvPrice = cardView.findViewById<TextView>(R.id.tvPrice)

                // Mostrar el nombre del bus
                tvNameBus.text = busCompany.name

                // Cargar la imagen con Picasso
                val domain = "https://gespro-iberoplast.tech" // Reemplaza con tu dominio
                val imageUrl = "$domain${busCompany.uri}"

                Picasso.get().load(imageUrl).into(ivBus)

                // Crear una cadena para almacenar todos los precios
                val priceStringBuilder = StringBuilder()

                // Iterar sobre las tarifas de la compañía y agregar los precios a la cadena
                for (fare in busCompany.fares!!) {
                    priceStringBuilder.append("S/. ${fare.price}")
                }

                // Mostrar la cadena de precios en el TextView
                tvPrice.text = priceStringBuilder.toString()


                // Agregar un OnClickListener a la CardView
                cardView.setOnClickListener {
                    // Al hacer clic en la CardView, iniciar la siguiente actividad con los datos del bus
                    val intent =
                        Intent(this@BusSelectionActivity, ChairSelectionActivity::class.java)
                    intent.putExtra("nameBus", busCompany.name)

                    val priceBus = tvPrice.text.toString()
                    intent.putExtra("priceBus", priceBus)

                    startActivity(intent)
                }

                // Agregar la CardView al contenedor
                containerLayout.addView(cardView)
            }
        }
    }
}