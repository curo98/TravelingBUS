package autonoma.pe.ux.models

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

data class BusCompany(
    val id: Int,
    val name: String,
    val uri: String,
    val fares: List<Fare>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(Fare.CREATOR) ?: emptyList(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(uri)
        parcel.writeTypedList(fares)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BusCompany> {
        override fun createFromParcel(parcel: Parcel): BusCompany {
            return BusCompany(parcel)
        }

        override fun newArray(size: Int): Array<BusCompany?> {
            return arrayOfNulls(size)
        }
    }
}