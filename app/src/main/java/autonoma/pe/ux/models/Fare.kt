package autonoma.pe.ux.models

import android.os.Parcel
import android.os.Parcelable


data class Fare(
    val destinationId: Int,
    val price: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(destinationId)
        parcel.writeString(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Fare> {
        override fun createFromParcel(parcel: Parcel): Fare {
            return Fare(parcel)
        }

        override fun newArray(size: Int): Array<Fare?> {
            return arrayOfNulls(size)
        }
    }
}