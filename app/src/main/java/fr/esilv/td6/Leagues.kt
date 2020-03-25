package fr.esilv.td6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.esilv.td6.R
import fr.esilv.td6.TeamDetailList
import fr.esilv.td6.Teams
import fr.esilv.td6.TeamsList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class Leagues : AppCompatActivity(), OnItemClickListener {

    private val TAG = "HomeFragment"
    private val API_KEY = KEYS.API_KEY
    private lateinit var recyclerView: RecyclerView
    private lateinit var api: FootBallService

    override fun onItemClicked(league: League) {
        Toast.makeText(this,"League name ${league.league_name} \n League ID:${league.league_id}",  Toast.LENGTH_LONG).show()
        Log.i("USER_",league.league_name)
        val intent = Intent(this, Teams::class.java)
        intent.putExtra("league_id", league.league_id.toInt())
        intent.putExtra("league_name", league.league_name)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leagues)

        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        var leagues = ArrayList<League>()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(KEYS.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(FootBallService::class.java)
        val click: OnItemClickListener = this

        api.getLeagues(46, API_KEY).enqueue(object : Callback<List<League>> {

                override fun onResponse(
                    call: Call<List<League>>,
                    response: Response<List<League>>
                ) {
                    Log.d(TAG, "onResponse")
                    if (response.code() == 200) {
                        val result: List<League> = response.body()!!
                        println(result)
                        //val listItems : List<SearchItem> = result.items

                        for (league in result) {
                            leagues.add(league)
                        }

                        var customAdapter = CustomAdapter(leagues, click)
                        recyclerView.adapter = customAdapter
                    }
                }

                override fun onFailure(call: Call<List<League>>, t: Throwable?) {
                    println("Bug Failure")
                    Log.e(TAG, "onFailure", t)
                }
            })
    }



    class CustomAdapter(val itemList: List<League>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
            return ViewHolder(v)
        }

        //this method is binding the data on the list
        override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
            holder.bindItems(itemList[position], itemClickListener)
        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return itemList.size
        }

        //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItems(item: League, clickListener: OnItemClickListener) {
                val leagueName = itemView.findViewById<TextView>(R.id.textViewleagueName)
                val leagueImg = itemView.findViewById<ImageView>(R.id.imageViewleagueImg)
                leagueName.text = item.league_name
                Glide.with(itemView).load(item.league_logo).into(leagueImg)

                itemView.setOnClickListener{
                    clickListener.onItemClicked(item)
                }

            }
        }
    }

}

data class League(val league_id: String, val league_name: String, val league_logo: String)


interface OnItemClickListener{
    fun onItemClicked(leagues: League)
}
