package autonoma.pe.ux.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import autonoma.pe.ux.R
import autonoma.pe.ux.models.TouristSpot
import com.squareup.picasso.Picasso

class TouristSpotAdapter : RecyclerView.Adapter<TouristSpotAdapter.ViewHolder>() {
    private var touristSpots = ArrayList<TouristSpot>()
    private var filteredList = ArrayList<TouristSpot>()
    private var itemClickListener: OnItemClickListener? = null
    private var infoClickListener: OnInfoClickListener? = null

    // Interfaz para manejar clics generales en los elementos del adaptador
    interface OnItemClickListener {
        fun onItemClick(touristSpot: TouristSpot)
    }

    // Interfaz para manejar clics en el nombre de los elementos del adaptador
    interface OnInfoClickListener {
        fun onInfoClick(touristSpot: TouristSpot)
    }

    // Método para establecer el listener de clics generales desde la actividad
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    // Método para establecer el listener de clics en el nombre desde la actividad
    fun setOnInfoClickListener(listener: OnInfoClickListener) {
        this.infoClickListener = listener
    }
    fun updateTouristSpots(newTouristSpots: List<TouristSpot>) {
        touristSpots.clear()
        touristSpots.addAll(newTouristSpots)
        filterTouristSpots("")
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterTouristSpots(query: String) {
        filteredList.clear()

        if (query.isEmpty()) {
            filteredList.addAll(touristSpots)
        } else {
            for (ts in touristSpots) {
                if (ts.name.toLowerCase().contains(query)) {
                    filteredList.add(ts)
                }
            }
        }

        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View, private val itemClickListener: OnItemClickListener?, private val infoClickListener: OnInfoClickListener?) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val info: TextView = itemView.findViewById(R.id.tvInfo)
        private val destination: TextView = itemView.findViewById(R.id.destination)
        private val location: TextView = itemView.findViewById(R.id.location)
        private val imageView: ImageView = itemView.findViewById(R.id.image)

        fun bind(ts: TouristSpot) {
            val uri = ts.uri
            val domain = "https://gespro-iberoplast.tech"
            val imageUrl = "$domain$uri"

            Picasso.get().load(imageUrl).into(imageView)

            name.text = ts.name
            destination.text = ts.destination.name
            location.text = ts.exact_location

            // Agregar un clic al ImageView
            imageView.setOnClickListener {
                itemClickListener?.onItemClick(ts)
            }

            info.setOnClickListener {
                infoClickListener?.onInfoClick(ts)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.items_tourist_spots, parent, false),
            itemClickListener, infoClickListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ts = filteredList[position]
        holder.bind(ts)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }
}

