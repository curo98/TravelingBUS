package autonoma.pe.ux

import android.animation.AnimatorInflater
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import autonoma.pe.ux.io.ApiService
import autonoma.pe.ux.util.ActionBarUtils

class InterestsActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val btnExploreAventure by lazy { findViewById<TextView>(R.id.btnExploreAventure) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interests)

        ActionBarUtils.setCustomTitle(
            this,
            "seleccion de actividad"
        )

        val cardView = findViewById<CardView>(R.id.myCardView)
        val blinkAnimation = AnimatorInflater.loadAnimator(this, R.animator.blink_animation)
        blinkAnimation.setTarget(cardView)
        blinkAnimation.start()

        btnExploreAventure.setOnClickListener {
            val intent = Intent(this, DestinationsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}