package autonoma.pe.ux

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import autonoma.pe.ux.adapters.TouristSpotAdapter
import autonoma.pe.ux.io.ApiService
import autonoma.pe.ux.models.BusCompany
import autonoma.pe.ux.models.TouristSpot
import autonoma.pe.ux.util.ActionBarUtils
import autonoma.pe.ux.util.PreferenceHelper
import autonoma.pe.ux.util.PreferenceHelper.set
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DestinationsActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private lateinit var tsAdapter: TouristSpotAdapter
    private lateinit var rv: RecyclerView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destinations)

        ActionBarUtils.setCustomTitle(
            this,
            "Destinos turisticos"
        )

        rv = findViewById(R.id.rv)

        tsAdapter = TouristSpotAdapter()
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = tsAdapter

        tsAdapter.setOnItemClickListener(object : TouristSpotAdapter.OnItemClickListener {
            override fun onItemClick(touristSpot: TouristSpot) {
                // Manejar el clic en el elemento aquí
                val selectedDestinationId = touristSpot.destination.id
//                Toast.makeText(
//                    this@DestinationsActivity,
//                    "Clic en ${touristSpot.destination.id}",
//                    Toast.LENGTH_SHORT
//                ).show()
                preferences["userDestination"] = touristSpot.destination.name

                getBuses(selectedDestinationId)
            }
        })

//        tsAdapter.setOnInfoClickListener(object : TouristSpotAdapter.OnInfoClickListener {
//            override fun onInfoClick(touristSpot: TouristSpot) {
//                // Manejar el clic en el nombre aquí
//                Toast.makeText(
//                    this@DestinationsActivity,
//                    "Clic en mas info de: ${touristSpot}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        })
        tsAdapter.setOnInfoClickListener(object : TouristSpotAdapter.OnInfoClickListener {
            override fun onInfoClick(touristSpot: TouristSpot) {
                // Abrir la actividad de detalles y pasar el objeto TouristSpot
                val intent = Intent(this@DestinationsActivity, DetailTouristSpotActivity::class.java)
                intent.putExtra("touristSpot", touristSpot)
                startActivity(intent)
            }
        })

        loadTouristSpot()
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

    private fun loadTouristSpot() {

        val call = apiService.getTouristSpots()
        call.enqueue(object : Callback<ArrayList<TouristSpot>> {
            override fun onResponse(call: Call<ArrayList<TouristSpot>>, response: Response<ArrayList<TouristSpot>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        tsAdapter.updateTouristSpots(it)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<TouristSpot>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}