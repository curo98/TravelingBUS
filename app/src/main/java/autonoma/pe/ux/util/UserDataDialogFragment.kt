package autonoma.pe.ux.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import autonoma.pe.ux.R
import autonoma.pe.ux.util.PreferenceHelper.set
import java.util.Locale

class UserDataDialogFragment : DialogFragment() {

    interface OnDataEnteredListener {
        fun onDataEntered(firstName: String, lastName: String, dni: String)
    }

    private var onDataEnteredListener: OnDataEnteredListener? = null

    fun setOnDataEnteredListener(listener: OnDataEnteredListener) {
        onDataEnteredListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_data_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtén referencias a tus EditText en el layout del fragmento
        val etFirstName = view.findViewById<EditText>(R.id.etFirstName)
        val etLastName = view.findViewById<EditText>(R.id.etLastName)
        val etDni = view.findViewById<EditText>(R.id.etDni)

        // Configura un botón para guardar los datos
        view.findViewById<Button>(R.id.btnSave).setOnClickListener {
            var firstName = etFirstName.text.toString().toUpperCase(Locale.getDefault())
            var lastName = etLastName.text.toString().toUpperCase(Locale.getDefault())
            var dni = etDni.text.toString()

            dni = dni.replace("\\s".toRegex(), "")

            // Divide y vuelve a unir el apellido si contiene dos palabras
            val lastNameWords = lastName.split(" ")
            if (lastNameWords.size > 1) {
                lastName = lastNameWords.joinToString(" ")
            }

            if (validateUserData(firstName, lastName, dni)) {
                onDataEnteredListener?.onDataEntered(firstName, lastName, dni)
                dismiss()
            }
        }

        // Configura el tamaño del diálogo
        val width = resources.displayMetrics.widthPixels * 0.90 // Porcentaje del ancho de la pantalla
        val height = ViewGroup.LayoutParams.WRAP_CONTENT // Puedes ajustar esto según tus necesidades

        // Aplica el tamaño al diálogo
        dialog?.window?.setLayout(width.toInt(), height)
        // Aplica padding al contenido del diálogo
        val paddingInPixels = resources.getDimensionPixelSize(R.dimen.dialog_padding)
        view.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels)

        // Configura el título del diálogo
        dialog?.setTitle("Ingresa tus datos personales")
    }

    // Función para validar los datos del usuario y mostrar errores
    private fun validateUserData(firstName: String, lastName: String, dni: String): Boolean {
        val etFirstName = view?.findViewById<EditText>(R.id.etFirstName)
        val etLastName = view?.findViewById<EditText>(R.id.etLastName)
        val etDni = view?.findViewById<EditText>(R.id.etDni)
        var isValid = true

        if (firstName.isBlank()) {
            etFirstName?.error = "Ingrese su nombre"
            isValid = false
        }

        if (lastName.isBlank()) {
            etLastName?.error = "Ingrese su apellido"
            isValid = false
        }

        if (dni.isBlank()) {
            etDni?.error = "Ingrese su DNI"
            isValid = false
        }

        return isValid
    }

    companion object {
        val TAG: String? = null
    }
}

