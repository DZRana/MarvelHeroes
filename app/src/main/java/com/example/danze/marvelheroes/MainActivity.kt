package com.example.danze.marvelheroes

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    val ts = "1"
    val pubKey = "e06645912ccb1236b46a694fede88f54"
    val privKey  = "40845007024de71ce88333497864331af856685f"
    val keyHash = (HashUtils.md5(ts+privKey+pubKey)).toLowerCase()
    val gson = Gson()

    object HashUtils {
        fun md5(input: String) = hashString("MD5", input)

        private fun hashString(type: String, input: String): String {
            val HEX_CHARS = "0123456789ABCDEF"
            val bytes = MessageDigest
                    .getInstance(type)
                    .digest(input.toByteArray())
            val result = StringBuilder(bytes.size * 2)

            bytes.forEach {
                val i = it.toInt()
                result.append(HEX_CHARS[i shr 4 and 0x0f])
                result.append(HEX_CHARS[i and 0x0f])
            }

            return result.toString()
        }
    }

    fun Any.toast(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast {
        return Toast.makeText(context, this.toString(), duration).apply { show() }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testBtn.setOnClickListener {
            val searchHeroQuery = searchHero.text
            "http://gateway.marvel.com/v1/public/characters?nameStartsWith=$searchHeroQuery&ts=$ts&apikey=$pubKey&hash=$keyHash".httpGet().responseString { request, response, result ->
                //do something with response
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        println(ex)
                        "Not A Valid Search Query".toast(context = applicationContext)
                    }
                    is Result.Success -> {
                        val jsonData = result.get()
                        val hero = gson.fromJson(jsonData, Hero::class.java)
                        heroName.text = hero.data.results[0].name
                        Picasso.get().load(hero.data.results[0].thumbnail.path + "/portrait_incredible." + hero.data.results[0].thumbnail.extension).into(heroImg)
                    }
                }
            }
        }
    }
}
