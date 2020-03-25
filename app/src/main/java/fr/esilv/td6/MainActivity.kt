package fr.esilv.td6

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val FAVORIS : String = "MesFavoris"

interface OnFavouriteClickListener{
    fun OnFavouriteClicked(fav: TeamsList)
}


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var SP_Fav: SharedPreferences = getSharedPreferences(FAVORIS, Context.MODE_PRIVATE)
        println("Values in shared preferences")
        println(SP_Fav.all)
        if (SP_Fav.getAll().isEmpty()) {
            val intent = Intent(this, Leagues::class.java)
            startActivity(intent)
        } else {
            setContentView(R.layout.activity_main)
            recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFavouriteTeams)
            recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

            //var elements = ArrayList<TeamsList>()

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(KEYS.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            var elements = ArrayList<TeamsList>()

            //val allFavourites = SP_Fav.getAll()
            val api = retrofit.create(FootBallService::class.java)
            val click: OnFavouriteClickListener = this

            SP_Fav.all.forEach{
                val teamName = it.key
                val str = it.value.toString().split('_').toTypedArray()
                val teamId = str[0].toInt()
                val leagueId = str[1].toInt()
                Log.d("Team Name: ",teamName + "| Team Id: " + teamId + "| League Id: " + leagueId)

                api.getTeamMajorArgs(teamId, KEYS.API_KEY).enqueue(object : Callback<List<TeamsList>> {

                    override fun onResponse(call: Call<List<TeamsList>>, response: Response<List<TeamsList>>) {
                        Log.d(TAG, "onResponse")
                        println(response)
                        if (response.code() == 200) {
                            println(response)
                            val result: List<TeamsList> = response.body()!!
                            for (item in result){
                                elements.add(item)
                            }
                        }

                    }

                    override fun onFailure(call: Call<List<TeamsList>>, t: Throwable?) {
                        Log.e(TAG, "onFailure", t)
                    }
                })
            }

            recyclerView.adapter = FavouriteAdapter(elements, click)



        }

        setContentView(R.layout.activity_main)
        val teamsButton = findViewById<Button>(R.id.addFavouriteButton)
        teamsButton.setOnClickListener {
            val intent = Intent(this, Leagues::class.java)
            startActivity(intent)
        }

    }


    class FavouriteAdapter(val itemList: List<TeamsList>, private val itemClickListener: OnFavouriteClickListener) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_list_favourite, parent, false)
            return ViewHolder(v)
        }

        //this method is binding the data on the list
        override fun onBindViewHolder(holder: FavouriteAdapter.ViewHolder, position: Int) {
            holder.bindItems(itemList[position], itemClickListener)
        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return itemList.size
        }

        //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItems(item: TeamsList, clickListener: OnFavouriteClickListener) {
                val teamName = itemView.findViewById<TextView>(R.id.textViewFavouriteTeam)
                val teamImg = itemView.findViewById<ImageView>(R.id.imageViewFavouriteTeam)
                teamName.text = item.team_name
                Glide.with(itemView).load(item.team_badge).into(teamImg)

                itemView.setOnClickListener{
                    clickListener.OnFavouriteClicked(item)
                }

            }
        }
    }
}



interface FootBallService{
    @GET("?action=get_leagues")
    fun getLeagues(@Query(value="country_id") country_id: Int, @Query(value="APIkey") apiKey: String): Call<List<League>>

    @GET("?action=get_teams")
    fun getTeams(@Query(value="league_id") league_id: Int, @Query(value="APIkey") apiKey: String): Call<List<TeamsList>>

    @GET("?action=get_teams")
    fun getTeamMajorArgs(@Query(value="team_id") team_id: Int, @Query(value="APIkey") apiKey: String): Call<List<TeamsList>>
}


class KEYS{
    companion object{
        const val API_KEY: String = "6707c229027cce084037a2264d4b4e8116f19f7953f03a8a942a8245e1278758"
        const val BASE_URL: String = "https://apiv2.apifootball.com/"
    }
}