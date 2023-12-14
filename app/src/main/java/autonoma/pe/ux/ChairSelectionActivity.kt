package autonoma.pe.ux

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import autonoma.pe.ux.util.ActionBarUtils
import autonoma.pe.ux.util.PreferenceHelper
import autonoma.pe.ux.util.PreferenceHelper.get
import autonoma.pe.ux.util.PreferenceHelper.set
import autonoma.pe.ux.util.PreferenceHelper.getCurrentDate
import autonoma.pe.ux.util.UserDataDialogFragment

class ChairSelectionActivity : AppCompatActivity() {
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    // Array de recursos drawable
    private val drawables = arrayOf(R.drawable.ic_rec_avalaible, R.drawable.ic_rectangle)
    private var selectedSeat: ImageView? = null
    private var btnNextBuy3: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chair_selection)

        ActionBarUtils.setCustomTitle(
            this,
            "Seleccion de asiento"
        )

        val tvOrigin = findViewById<TextView>(R.id.tvOrigin)
        val tvDestination = findViewById<TextView>(R.id.tvDestination)
        val tvNameBus = findViewById<TextView>(R.id.tvNameBus)
        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        val tvDate = findViewById<TextView>(R.id.tvDate)

        // Obtener los datos del intent
        val origin = preferences["userLocation", ""]
        val destination = preferences["userDestination", ""]
        val nameBus = intent.getStringExtra("nameBus")
        val priceBus = intent.getStringExtra("priceBus")
        val currentDate = getCurrentDate()

        // Mostrar los datos en la actividad
        tvOrigin.text = origin
        tvDestination.text = destination
        tvNameBus.text = nameBus
        tvPrice.text = priceBus
        tvDate.text = currentDate


        btnNextBuy3 = findViewById(R.id.btnNextBuy3)
        updateNextButtonVisibility()

        for (i in 1..40) {
            val imageViewId = resources.getIdentifier("iv$i", "id", packageName)
            val imageView = findViewById<ImageView>(imageViewId)

            val randomIndex = (0..1).random()
            imageView.setImageResource(drawables[randomIndex])
            imageView.tag = drawables[randomIndex]
            imageView.setOnClickListener {
                onSeatClicked(imageView)
            }
        }

        btnNextBuy3?.setOnClickListener {
            if (selectedSeat != null) {
                val seatNumber = getSeatNumber(selectedSeat!!)
                showUserDataDialog(origin, destination, nameBus, priceBus, currentDate, seatNumber)

//                redirectToInfoCardActivity(origin, destination, nameBus, priceBus, currentDate, seatNumber)
            } else {
                showToast("Seleccione un asiento antes de continuar.")
            }
        }
    }

    private fun showUserDataDialog(
        origin: String,
        destination: String,
        nameBus: String?,
        priceBus: String?,
        currentDate: String,
        seatNumber: String
    ) {
        val userDataDialog = UserDataDialogFragment()
        userDataDialog.setOnDataEnteredListener(object : UserDataDialogFragment.OnDataEnteredListener {
            override fun onDataEntered(firstName: String, lastName: String, dni: String) {
                // Verifica si se ingresaron todos los datos necesarios
                if (firstName.isNotBlank() && lastName.isNotBlank() && dni.isNotBlank()) {
                    // Si todos los datos están presentes, permite continuar
                    preferences["userName"] = firstName
                    preferences["userLastName"] = lastName
                    preferences["userDni"] = dni
                    redirectToInfoCardActivity(origin, destination, nameBus, priceBus, currentDate, seatNumber)
                } else {
                    showToast("Por favor, ingresa todos tus datos.")
                }
            }
        })
        userDataDialog.show(supportFragmentManager, UserDataDialogFragment.TAG)
    }

    private fun onSeatClicked(imageView: ImageView) {
        val currentDrawableId = imageView.tag as? Int ?: 0

        if (currentDrawableId == R.drawable.ic_rec_avalaible) {
            if (selectedSeat == null) {
                imageView.setImageResource(R.drawable.ic_your_site)
                imageView.tag = R.drawable.ic_your_site
                selectedSeat = imageView

                showYourSeatTextView(true)
                updateNextButtonVisibility()
                startBlinkAnimation(imageView)

                val seatNumber = getSeatNumber(imageView)
                showToast("Asiento seleccionado: $seatNumber")
            } else {
                showToast("Ya seleccionaste un asiento, para seleccionar otro asiento, quite la selección actual.")
            }
        } else if (currentDrawableId == R.drawable.ic_rectangle) {
            showToast("Este asiento está ocupado. Por favor, seleccione otro asiento disponible.")
        } else if (currentDrawableId == R.drawable.ic_your_site) {
            stopBlinkAnimation(imageView)

            imageView.setImageResource(R.drawable.ic_rec_avalaible)
            imageView.tag = R.drawable.ic_rec_avalaible
            selectedSeat = null

            showYourSeatTextView(false)
            updateNextButtonVisibility()
        }
    }

    private fun showYourSeatTextView(visible: Boolean) {
        val yourSeatTextView = findViewById<TextView>(R.id.tvChairSelected)
        yourSeatTextView.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun updateNextButtonVisibility() {
        val isVisible = selectedSeat != null
        btnNextBuy3?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun startBlinkAnimation(imageView: ImageView) {
        val blinkAnimation = AlphaAnimation(1.0f, 0.0f)
        blinkAnimation.duration = 500 // Duración del parpadeo
        blinkAnimation.repeatMode = AlphaAnimation.REVERSE
        blinkAnimation.repeatCount = AlphaAnimation.INFINITE
        imageView.startAnimation(blinkAnimation)
    }

    private fun stopBlinkAnimation(imageView: ImageView) {
        imageView.clearAnimation()
    }

    private fun getSeatNumber(imageView: ImageView): String {
        val imageViewId = imageView.id
        return resources.getResourceEntryName(imageViewId).removePrefix("iv")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun redirectToInfoCardActivity(origin: String?, destination: String?, nameBus: String?, priceBus: String?, currentDate:String?, seatNumber: String) {
        val intent = Intent(this, InfoCardActivity::class.java)
        intent.putExtra("origin", origin)
        intent.putExtra("destination", destination)
        intent.putExtra("nameBus", nameBus)
        intent.putExtra("priceBus", priceBus)
        intent.putExtra("currentDate", currentDate)
        intent.putExtra("SEAT_NUMBER", seatNumber)
        startActivity(intent)
//        finish() // Opcional: finalizar esta actividad para que el usuario no pueda volver atrás
    }
}