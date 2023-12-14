package autonoma.pe.ux.io

import autonoma.pe.ux.models.BusCompany
import autonoma.pe.ux.models.Destination
import autonoma.pe.ux.models.TouristSpot
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("buses/{id}")
    fun getBuses(@Path("id") id: Int): Call<ArrayList<BusCompany>>

    @GET("tourist-spots")
    fun getTouristSpots(): Call<ArrayList<TouristSpot>>

    @GET("destinations")
    fun getDestinations(): Call<ArrayList<Destination>>

    companion object Factory {
        private const val BASE_URL = "https://gespro-iberoplast.tech/api/"

        fun create(): ApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}