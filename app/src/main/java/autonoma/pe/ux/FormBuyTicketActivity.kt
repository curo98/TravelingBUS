package autonoma.pe.ux

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import autonoma.pe.ux.io.ApiService
import autonoma.pe.ux.models.BusCompany
import autonoma.pe.ux.models.Destination
import autonoma.pe.ux.util.ActionBarUtils
import autonoma.pe.ux.util.PreferenceHelper
import autonoma.pe.ux.util.PreferenceHelper.set
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormBuyTicketActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private lateinit var etLocationUser: EditText
    private lateinit var etDestination: EditText
    private lateinit var btnNextBuy1: Button

    private var selectedDestinationId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_buy_ticket)

        ActionBarUtils.setCustomTitle(
            this,
            "Compra tu boleto"
        )

        etLocationUser = findViewById(R.id.etLocationUser)
        btnNextBuy1 = findViewById(R.id.btnNextBuy1)

        // Asignar "Lima" por defecto
        etLocationUser.setText("Lima")

        preferences["userLocation"] = "Lima"


        // Inhabilitar la edición
        etLocationUser.isEnabled = false
        etLocationUser.isFocusable = false
        etLocationUser.isClickable = false

        btnNextBuy1.setOnClickListener {
                getBuses(selectedDestinationId)
        }
        loadDestinations()
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

    private fun loadDestinations() {
        val spDestinations = findViewById<SearchableSpinner>(R.id.spDest)

        val call = apiService.getDestinations()
        call.enqueue(object : Callback<ArrayList<Destination>> {
            override fun onResponse(
                call: Call<ArrayList<Destination>>,
                response: Response<ArrayList<Destination>>
            ) {
                if (response.isSuccessful) {
                    val destinations = response.body() ?: ArrayList()

                    // Crear una lista de DestinationItem a partir de los destinos
                    val destinationItems = destinations.map { Destination(it.id, it.name) }

                    // Crear un ArrayAdapter personalizado para mostrar solo los nombres
                    val adapter = ArrayAdapter(
                        this@FormBuyTicketActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        destinationItems
                    )

                    // Establecer el adaptador personalizado en el SearchableSpinner
                    spDestinations.adapter = adapter

                    // Establecer el oyente para manejar la selección del elemento
                    spDestinations.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parentView: AdapterView<*>?,
                                selectedItemView: View?,
                                position: Int,
                                id: Long
                            ) {
                                // Obtener el ID del elemento seleccionado
                                selectedDestinationId = destinationItems[position].id
                                // Manejar el ID como sea necesario
                                // Obtener el nombre del destino seleccionado
                                val selectedDestinationName = destinationItems[position].name

                                preferences["userDestination"] = selectedDestinationName
                            }

                            override fun onNothingSelected(parentView: AdapterView<*>?) {
                                // No hacer nada aquí
                            }
                        }

                    // Establecer el texto de aviso
                    spDestinations.setTitle("Selecciona un destino")

                } else {
                    // Manejar respuesta no exitosa aquí
                }
            }

            override fun onFailure(call: Call<ArrayList<Destination>>, t: Throwable) {
                // Manejar fallo en la solicitud aquí
            }
        })
    }

}