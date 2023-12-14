package autonoma.pe.ux

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import autonoma.pe.ux.util.ActionBarUtils

class ShowTicketActivity : AppCompatActivity() {
    private val tvBusCompany by lazy { findViewById<TextView>(R.id.tvBusCompany) }
    private val tvDescriptionBusCompany by lazy { findViewById<TextView>(R.id.tvDescriptionBusCompany) }
    private val tvOrigin by lazy { findViewById<TextView>(R.id.tvOrigin) }
    private val tvDestination by lazy { findViewById<TextView>(R.id.tvDestination) }
    private val tvDate by lazy { findViewById<TextView>(R.id.tvDate) }
    private val tvNameUser by lazy { findViewById<TextView>(R.id.tvNameUser) }
    private val tvNumberChair by lazy { findViewById<TextView>(R.id.tvNumberChair) }
    private val tvPrice by lazy { findViewById<TextView>(R.id.tvPrice) }
    private val tvPriceOP by lazy { findViewById<TextView>(R.id.tvPriceOP) }
    private val tvPriceTotal by lazy { findViewById<TextView>(R.id.tvPriceTotal) }
    private val tvDescriptionPrice by lazy { findViewById<TextView>(R.id.tvDescriptionPrice) }
    private val btnGoToHome by lazy { findViewById<Button>(R.id.btnGoToHome) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_ticket)

        ActionBarUtils.setCustomTitle(
            this,
            "Boleto generado"
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
        val priceBus = intent.getStringExtra("priceBus") ?: "S/ .0"
        val currentDate = intent.getStringExtra("currentDate")
        val seatNumber = intent.getStringExtra("SEAT_NUMBER")

        tvBusCompany?.text = nameBus
        tvDescriptionBusCompany?.text = "Empresa de transporte $nameBus S.A"

        tvOrigin?.text = origin
        tvDestination?.text = destination
        tvDate?.text = currentDate

        tvNameUser?.text = nameUser
        tvNumberChair?.text = seatNumber

        tvPrice?.text = priceBus
        tvPriceOP?.text = priceBus
        tvPriceTotal?.text = priceBus

        // Utilizar una expresión regular para extraer la parte numérica de la cadena
        val regex = Regex("[0-9]+[.]?[0-9]*")
        val matchResult = regex.find(priceBus)
        val numericValue = matchResult?.value?.toDouble() ?: 0.0

        // Convertir el valor numérico a letras manualmente
        val priceInWords = convertirNumeroALetras(numericValue)

        // Formatear la cadena según el formato deseado
        val formattedPrice = "SON: ${priceInWords}CON 00/100 SOLES"

        // Mostrar el resultado en el TextView con id tvDescriptionPrice
        tvDescriptionPrice.text = formattedPrice

        btnGoToHome.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun convertirNumeroALetras(numero: Double): String {
        val unidades = arrayOf("", "UNO", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE")
        val decenas = arrayOf("", "DIEZ", "VEINTE", "TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA", "OCHENTA", "NOVENTA")
        val centenas = arrayOf("", "CIENTO", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS", "QUINIENTOS", "SEISCIENTOS", "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS")

        val valorAbsoluto = Math.abs(numero)
        val parteEntera = valorAbsoluto.toInt()
        val parteDecimal = ((valorAbsoluto - parteEntera) * 100).toInt()

        val parteEnteraEnLetras = if (parteEntera == 0) "CERO" else convertirGrupoALetras(parteEntera, centenas, decenas, unidades)

        val parteDecimalEnLetras = if (parteDecimal == 0) "" else "CON ${convertirGrupoALetras(parteDecimal, centenas, decenas, unidades)}"

        return "$parteEnteraEnLetras $parteDecimalEnLetras"
    }

    private fun convertirGrupoALetras(numero: Int, centenas: Array<String>, decenas: Array<String>, unidades: Array<String>): String {
        return when {
            numero >= 100 -> centenas[numero / 100] + " " + convertirGrupoALetras(numero % 100, centenas, decenas, unidades)
            numero >= 10 -> decenas[numero / 10] + "" + convertirGrupoALetras(numero % 10, centenas, decenas, unidades)
            numero > 0 -> unidades[numero]
            else -> ""
        }
    }
}