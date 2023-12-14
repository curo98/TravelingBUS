package autonoma.pe.ux

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import autonoma.pe.ux.util.ActionBarUtils
import autonoma.pe.ux.util.PreferenceHelper
import autonoma.pe.ux.util.PreferenceHelper.get

class InfoCardActivity : AppCompatActivity() {
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private val tvNameCard by lazy { findViewById<TextView>(R.id.tvNameCard) }
    private val tvNumberCard by lazy { findViewById<TextView>(R.id.tvNumberCard) }
    private val tvMMCard by lazy { findViewById<TextView>(R.id.tvMMCard) }
    private val tvAACard by lazy { findViewById<TextView>(R.id.tvAACard) }
    private val btnGoToHome by lazy { findViewById<Button>(R.id.btnGoToHome) }
    private lateinit var etNameUser: EditText
    private lateinit var etNumberCard: EditText
    private lateinit var etMM: EditText
    private lateinit var etAA: EditText
    private lateinit var etCvc: EditText
    private lateinit var cbAcceptPolicy: CheckBox
    private lateinit var btnFinalizeBuy: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_card)

        ActionBarUtils.setCustomTitle(
            this,
            "Metodo de pago"
        )

        val userName = preferences["userName", ""]
        val userLastName = preferences["userLastName", ""]

        // Divide y vuelve a unir el apellido si contiene dos palabras
        val formattedUserLastName = formatLastName(userLastName)

        // Obtener el número de asiento del intent
        val origin = intent.getStringExtra("origin")
        val destination = intent.getStringExtra("destination")
        val nameBus = intent.getStringExtra("nameBus")
        val priceBus = intent.getStringExtra("priceBus")
        val currentDate = intent.getStringExtra("currentDate")
        val seatNumber = intent.getStringExtra("SEAT_NUMBER")

        val nameComplete = "$userName $formattedUserLastName"

        etNameUser = findViewById(R.id.etNameUser)
        etNameUser.filters = arrayOf<InputFilter>(InputFilter.AllCaps())

        val tvNameCard = findViewById<TextView>(R.id.tvNameCard)
        etNameUser.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
                // No es necesario implementar este método
            }

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                tvNameCard.text = charSequence.toString()
            }

            override fun afterTextChanged(editable: Editable) {
                // No es necesario implementar este método
            }
        })


        etNameUser.setText(nameComplete)
        tvNameCard.setText(nameComplete)

        etNumberCard = findViewById(R.id.etNumberCard)

        etMM = findViewById(R.id.etMM)
        etMM.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
                // No es necesario implementar este método
            }

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                tvMMCard.text = charSequence.toString()
            }

            override fun afterTextChanged(editable: Editable) {
                // No es necesario implementar este método
            }
        })
        etAA = findViewById(R.id.etAA)
        etAA.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
                // No es necesario implementar este método
            }

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                val inputText = charSequence.toString()
                val lastTwoDigits = if (inputText.length >= 2) inputText.substring(inputText.length - 2) else inputText
                tvAACard.text = lastTwoDigits
            }

            override fun afterTextChanged(editable: Editable) {
                // No es necesario implementar este método
            }
        })
        etCvc = findViewById(R.id.etCvc)
        cbAcceptPolicy = findViewById(R.id.cbAcceptPolicy)
        btnFinalizeBuy = findViewById(R.id.btnFinalizeBuy)
        btnCancel = findViewById(R.id.btnCancel)

        setupTextWatcherNumber()

        btnFinalizeBuy.setOnClickListener {
            if (validateInput()) {
                // Obtener los valores de los EditText
                val nameUser = etNameUser.text.toString()
                val numberCard = etNumberCard.text.toString().replace("-", "")
                val mm = etMM.text.toString()
                val aa = etAA.text.toString()
                val cvc = etCvc.text.toString()

                // Crear un Intent para pasar a la siguiente actividad
                val intent = obtenerIntentSiguienteActividad(
                    nameUser,
                    numberCard,
                    mm,
                    aa,
                    cvc,
                    origin,
                    destination,
                    nameBus,
                    priceBus,
                    currentDate,
                    seatNumber
                )

                // Iniciar la siguiente actividad
                startActivity(intent)
            }
        }

        btnCancel.setOnClickListener {
            mostrarDialogoCancelarCompra()
        }
    }
    private fun formatLastName(lastName: String): String {
        // Divide y vuelve a unir el apellido si contiene dos palabras
        val lastNameWords = lastName.split(" ")
        return if (lastNameWords.size > 1) {
            lastNameWords.joinToString(" ")
        } else {
            lastName
        }
    }

    private fun setupTextWatcherNumber() {
        etNumberCard.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(19)) // Permitir 19 caracteres (16 dígitos + 3 guiones)

        etNumberCard.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitas implementar esto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvNumberCard.text = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // Obtén el texto actual del EditText
                val cardNumber = s.toString().replace("-", "")

                // Verifica si la longitud del número de tarjeta es mayor que 16
                if (cardNumber.length > 16) {
                    // Inserta guiones después de cada cuatro dígitos
                    val formattedCardNumber = StringBuilder(cardNumber)
                        .insert(4, "-")
                        .insert(9, "-")
                        .insert(14, "-")
                        .toString()

                    // Establece el nuevo texto con guiones
                    etNumberCard.setText(formattedCardNumber)

                    // Coloca el cursor al final del texto
                    etNumberCard.setSelection(etNumberCard.text.length)
                }
            }
        })
    }

// ...
    private fun obtenerIntentSiguienteActividad(
        nameUser: String,
        numberCard: String,
        mm: String,
        aa: String,
        cvc: String,
        origin: String?,
        destination: String?,
        nameBus: String?,
        priceBus: String?,
        currentDate: String?,
        seatNumber: String?
    ): Intent {
        // Crear un Intent para pasar a la siguiente actividad
        val intent = Intent(this@InfoCardActivity, SuccessfullyActivity::class.java)

        // Agregar los valores como extras al Intent
        intent.putExtra("nameUser", nameUser)
        intent.putExtra("numberCard", numberCard)
        intent.putExtra("mm", mm)
        intent.putExtra("aa", aa)
        intent.putExtra("cvc", cvc)

        // Agregar otros valores obtenidos del intent
        intent.putExtra("origin", origin)
        intent.putExtra("destination", destination)
        intent.putExtra("nameBus", nameBus)
        intent.putExtra("priceBus", priceBus)
        intent.putExtra("currentDate", currentDate)
        intent.putExtra("SEAT_NUMBER", seatNumber)

        return intent
    }

    private fun validateInput(): Boolean {
        // Validar el campo etNameUser
        if (TextUtils.isEmpty(etNameUser.text.toString())) {
            etNameUser.error = "Ingrese el nombre del titular"
            return false
        }

        // Validar el campo etNumberCard
        val cleanNumberCard = etNumberCard.text.toString().replace("-", "")
        if (cleanNumberCard.length != 16) {
            etNumberCard.error = "Debe ingresar 16 dígitos"
            return false
        }

        // Validar el campo etMM
        val mmText = etMM.text.toString()
        if (mmText.length != 2) {
            etMM.error = "Ingrese 2 dígitos para el mes"
            return false
        }
        val mmValue = mmText.toIntOrNull()
        if (mmValue == null || mmValue < 1 || mmValue > 12) {
            etMM.error = "Ingrese un mes válido (01-12)"
            return false
        }


        // Validar el campo etAA
        val aaText = etAA.text.toString()
        if (aaText.length != 4) {
            etAA.error = "Ingrese 4 dígitos para el año"
            return false
        }
        val aaValue = aaText.toIntOrNull()
        if (aaValue == null || aaValue < 2020 || aaValue > 2090) {
            etAA.error = "Ingrese un año válido (2020-2090)"
            return false
        }

        // Validar el campo etCvc
        if (etCvc.text.length != 3) {
            etCvc.error = "Ingrese 3 dígitos para el código CVC"
            return false
        }

        if (!cbAcceptPolicy.isChecked) {
            showToast("Debe aceptar la política de protección de datos")
            return false
        }

        return true
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun mostrarDialogoCancelarCompra() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar cancelación")
            .setMessage("¿Estás seguro que quieres cancelar esta compra?")
            .setPositiveButton("Sí") { _, _ ->
                // Redirigir a otro activity
                // Puedes agregar la lógica de redirección aquí
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}