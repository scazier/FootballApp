package fr.esilv.td6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import fr.esilv.td6.ui.home.FootBallService
import fr.esilv.td6.ui.home.Leagues
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FootballService{
    @GET("?action=get_countries")
    fun getCountries(@Query(value = "APIkey") apiKey: String): Call<List<Country>>

    @GET("?action=get_league")
    fun getLeagues(@Query(value="country_id") id: String, @Query(value = "APIkey") apikey: String): Call<List<League>>
}


class Teams : AppCompatActivity() {

    private val TAG = "Teams"
    private val API_KEY = "6707c229027cce084037a2264d4b4e8116f19f7953f03a8a942a8245e1278758"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams)

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://apiv2.apifootball.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(FootballService::class.java)
        var countriesName = ArrayList<String>()
        var countriesId = ArrayList<String>()
        api.getCountries(API_KEY).enqueue(object: Callback<List<Country>> {

                override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                    Log.d(TAG, "onResponse Country")
                    if (response.code() == 200) {
                        val result : List<Country> = response.body()!!

                        for (country in result){
                            countriesName.add(country.country_name)
                            countriesId.add(country.country_id)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Country>>, t: Throwable?) {
                    println("Bug Failure")
                    Log.e(TAG, "onFailure", t)
                }
        })

        val spinnerCountry = findViewById<Spinner>(R.id.spinnerCountry)
        var leaguesName = ArrayList<String>()
        if (spinnerCountry != null) {
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countriesName)
            spinnerCountry.adapter = arrayAdapter

            spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    Log.e(TAG, "item selected"+countriesId[position])
                    api.getLeagues(countriesId.get(position), API_KEY).enqueue(object: Callback<List<League>> {

                        override fun onResponse(call: Call<List<League>>, response: Response<List<League>>) {
                            Log.d(TAG, "onResponse League")
                            if (response.code() == 200) {
                                println(response)
                                val result : List<League> = response.body()!!

                                for (league in result){
                                    leaguesName.add(league.country_name)
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<League>>, t: Throwable?) {
                            println("Bug Failure")
                            Log.e(TAG, "onFailure", t)
                        }
                    })
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

        val spinnerLeague = findViewById<Spinner>(R.id.spinnerLeague)
        if (spinnerLeague != null) {
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, leaguesName)
            spinnerLeague.adapter = arrayAdapter

            spinnerLeague.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }

    }


}

data class Country(val country_id: String, val country_name: String)
data class League(val league_id: String, val league_name: String, val country_id: String, val country_name: String)
