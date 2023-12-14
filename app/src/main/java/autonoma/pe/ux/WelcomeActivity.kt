package autonoma.pe.ux

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import android.widget.LinearLayout
import autonoma.pe.ux.util.ActionBarUtils
import com.airbnb.lottie.LottieAnimationView

class WelcomeActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        webView = findViewById(R.id.webView)

        ActionBarUtils.setCustomTitle(
            this,
            "Bienvenido"
        )

        // Configurar WebView y cargar código de LiveChat
        webView.settings.javaScriptEnabled = true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        val liveChatCode = """
            <!-- Start of LiveChat (www.livechat.com) code -->
            <script>
                window.__lc = window.__lc || {};
                window.__lc.license = 16840968;
                ;(function(n,t,c){function i(n){return e._h?e._h.apply(null,n):e._q.push(n)}var e={_q:[],_h:null,_v:"2.0",on:function(){i(["on",c.call(arguments)])},once:function(){i(["once",c.call(arguments)])},off:function(){i(["off",c.call(arguments)])},get:function(){if(!e._h)throw new Error("[LiveChatWidget] You can't use getters before load.");return i(["get",c.call(arguments)])},call:function(){i(["call",c.call(arguments)])},init:function(){var n=t.createElement("script");n.async=!0,n.type="text/javascript",n.src="https://cdn.livechatinc.com/tracking.js",t.head.appendChild(n)}};!n.__lc.asyncInit&&e.init(),n.LiveChatWidget=n.LiveChatWidget||e}(window,document,[].slice))
            </script>
            <noscript><a href="https://www.livechat.com/chat-with/16840968/" rel="nofollow">Chat with us</a>, powered by <a href="https://www.livechat.com/?welcome" rel="noopener nofollow" target="_blank">LiveChat</a></noscript>
            <!-- End of LiveChat code -->
        """.trimIndent()

        webView.loadDataWithBaseURL(
            "https://www.livechat.com/",
            liveChatCode,
            "text/html",
            "UTF-8",
            null
        )

        val btnInterest = findViewById<Button>(R.id.btnInterest)
        val btnBuyTicket = findViewById<Button>(R.id.btnBuyTicket)

        btnInterest.setOnClickListener {
            val intent = Intent(this, InterestsActivity::class.java)
            startActivity(intent)
        }

        btnBuyTicket.setOnClickListener {
            val intent = Intent(this, FormBuyTicketActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val menuItem = menu?.findItem(R.id.menu_chat)
        val actionView = menuItem?.actionView

        if (actionView is LinearLayout) {
            val lottieAnimationView = actionView.findViewById<LottieAnimationView>(R.id.lottieAnimationView)
            lottieAnimationView.setOnClickListener {
                toggleChatVisibility()
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_chat -> {
                toggleChatVisibility()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun toggleChatVisibility() {
        if (webView.visibility == View.VISIBLE) {
            webView.visibility = View.GONE
        } else {
            webView.visibility = View.VISIBLE
        }
    }


}