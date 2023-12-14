package autonoma.pe.ux.models

import android.os.Parcel
import android.os.Parcelable

data class TouristSpot(
    val id: Int,
    val name: String,
    val description: String,
    val uri: String,
    val destinationId: Int,
    val exact_location: String,
    val destination: Destination
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readParcelable(Destination::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(uri)
        parcel.writeInt(destinationId)
        parcel.writeString(exact_location)
        parcel.writeParcelable(destination, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TouristSpot> {
        override fun createFromParcel(parcel: Parcel): TouristSpot {
            return TouristSpot(parcel)
        }

        override fun newArray(size: Int): Array<TouristSpot?> {
            return arrayOfNulls(size)
        }
    }
}
