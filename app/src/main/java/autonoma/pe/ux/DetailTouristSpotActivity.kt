package autonoma.pe.ux

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import autonoma.pe.ux.io.ApiService
import autonoma.pe.ux.models.BusCompany
import autonoma.pe.ux.models.TouristSpot
import autonoma.pe.ux.util.ActionBarUtils
import autonoma.pe.ux.util.PreferenceHelper
import autonoma.pe.ux.util.PreferenceHelper.set
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTouristSpotActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private lateinit var tvLoc: TextView
    private lateinit var ivImage: ImageView
    private lateinit var tvDescription: TextView
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tourist_spot)

        val touristSpot = intent.getParcelableExtra<TouristSpot>("touristSpot")

        // Inicializar las vistas
        tvLoc = findViewById(R.id.tvLoc)
        ivImage = findViewById(R.id.ivImage)
        tvDescription = findViewById(R.id.tvDescription)
        btnNext = findViewById(R.id.btnNext)

        // Verificar si se recibió un objeto TouristSpot
        if (touristSpot != null) {

            ActionBarUtils.setCustomTitle(
                this,
                "${touristSpot.name}"
            )
            // Establecer los datos en las vistas
            tvLoc.text = touristSpot.exact_location
            // Utiliza Picasso para cargar la imagen en el ImageView
            // Cargar la imagen con Picasso
            val domain = "https://gespro-iberoplast.tech" // Reemplaza con tu dominio
            val imageUrl = "$domain${touristSpot.uri}"

            Picasso.get().load(imageUrl).into(ivImage)
            tvDescription.text = touristSpot.description

            // Configurar un clic en el botón si es necesario
            btnNext.setOnClickListener {
                getBuses(touristSpot.destination.id)
                preferences["userDestination"] = touristSpot.destination.name
            }
        }
    }

    private fun getBuses(selectedDestinationId: Int) {
        val call = apiService.getBuses(selectedDestinationId)
        call.enqueue(object : Callback<ArrayList<BusCompany>> {
            override fun onResponse(
                call: Call<ArrayList<BusCompany>>,
                response: Response<ArrayList<BusCompany>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { busCompanies ->
                        // Enviar la lista de buses a otra actividad
                        sendBusesToNextActivity(busCompanies)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<BusCompany>>, t: Throwable) {
                // Manejar el fallo en la solicitud aquí
            }
        })
    }

    private fun sendBusesToNextActivity(busCompanies: ArrayList<BusCompany>) {
        // Crear un Intent para pasar a la siguiente actividad
        val intent = Intent(this, BusSelectionActivity::class.java)

        // Agregar la lista de buses al Intent
        intent.putParcelableArrayListExtra("buses", busCompanies)

        // Iniciar la siguiente actividad
        startActivity(intent)
    }
}