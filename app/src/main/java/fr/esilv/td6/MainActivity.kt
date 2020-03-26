package fr.esilv.td6

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.esilv.td6.ui.home.HomeFragment
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val FAVORIS : String = "MesFavoris"

interface OnFavouriteClickListener{
    fun onFavouriteClicked(fav: FavouriteTeams)
}


class MainActivity : AppCompatActivity(), OnFavouriteClickListener {

    private val TAG = "MainActivity"

    override fun onFavouriteClicked(teams: FavouriteTeams) {
        val intent = Intent(this, bottom::class.java)
        var SP_Fav : SharedPreferences = getSharedPreferences("DataTemp", Context.MODE_PRIVATE)
        val editor = SP_Fav.edit()
        editor.putString("teamID",teams.teamId.toString())
        editor.putString("leagueID",teams.leagueId.toString())
        editor.apply()
        intent.putExtra("team_id", teams.teamId)
        intent.putExtra("league_id", teams.leagueId)
        startActivity(intent)
    }

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

            var elements: ArrayList<FavouriteTeams> = ArrayList<FavouriteTeams>()

            //val allFavourites = SP_Fav.getAll()
            //val api = retrofit.create(FootBallService::class.java)
            val click: OnFavouriteClickListener = this

            SP_Fav.all.forEach{
                val teamName = it.key
                val str = it.value.toString().split('|').toTypedArray()
                val teamId = str[0].toInt()
                val leagueId = str[1].toInt()
                val logoUrl = str[2]
                Log.d("Team Name: ",teamName + "| Team Id: " + teamId + "| League Id: " + leagueId + "| Logo URL: " + logoUrl)

                elements.add(FavouriteTeams(teamId, leagueId, teamName, logoUrl))
            }
            Log.e(TAG, "end of requests")
            println(elements)
            var recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFavouriteTeams)
            recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            recyclerView.adapter = FavouriteAdapter(elements, click)
        }
        /*
        setContentView(R.layout.activity_main)
        val teamsButton = findViewById<Button>(R.id.addFavouriteButton)
        teamsButton.setOnClickListener {
            val intent = Intent(this, Leagues::class.java)
            startActivity(intent)
        }
        */
    }


    class FavouriteAdapter(val itemList: List<FavouriteTeams>, private val itemClickListener: OnFavouriteClickListener) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

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

            fun bindItems(item: FavouriteTeams, clickListener: OnFavouriteClickListener) {
                val teamName = itemView.findViewById<TextView>(R.id.textViewFavouriteTeam)
                val teamImg = itemView.findViewById<ImageView>(R.id.imageViewFavouriteTeam)
                teamName.text = item.teamName
                Glide.with(itemView).load(item.logoUrl).into(teamImg)

                itemView.setOnClickListener{
                    clickListener.onFavouriteClicked(item)
                }

            }
        }
    }
}

data class FavouriteTeams(val teamId: Int, val leagueId: Int, val teamName: String, val logoUrl: String)

interface FootBallService{
    @GET("?action=get_leagues")
    fun getLeagues(@Query(value="country_id") country_id: Int, @Query(value="APIkey") apiKey: String): Call<List<League>>

    @GET("?action=get_teams")
    fun getTeams(@Query(value="league_id") league_id: Int, @Query(value="APIkey") apiKey: String): Call<List<TeamsList>>

    @GET("?action=get_teams")
    fun getTeamMajorArgs(@Query(value="team_id") team_id: Int, @Query(value="APIkey") apiKey: String): Call<List<TeamsList>>
}


data class UserApiResponse(val data: String)

class KEYS{
    companion object{
        const val API_KEY: String = "6707c229027cce084037a2264d4b4e8116f19f7953f03a8a942a8245e1278758"
        const val BASE_URL: String = "https://apiv2.apifootball.com/"
    }
}