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
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
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

interface OnTeamClickListener{
    fun onItemClicked(teams: TeamsList)
}


class Teams : AppCompatActivity(), OnTeamClickListener {
//class Teams : AppCompatActivity(){


    private lateinit var api: FootBallService
    private lateinit var recyclerView: RecyclerView
    private val TAG = "TeamsActivity"
    private val API_KEY = KEYS.API_KEY
    private var leagueId: Int = -1
    private var leagueName: String = ""
    private var teamId: Int = -1
    private var teamName: String = ""


    override fun onItemClicked(teams: TeamsList) {
        /*Toast.makeText(this,"Team name ${teams.team_name} \n Team ID:${teams.team_key}",  Toast.LENGTH_LONG).show()
        Log.i("USER_",teams.team_name)
        teamName = teams.team_name
        teamId = teams.team_key.toInt()

        val intent = Intent(this, bottom::class.java)
        intent.putExtra("team_id", teams.team_key.toInt())
        intent.putExtra("team_name", teams.team_name)
        intent.putExtra("league_id", leagueId)*/
        val FAVORIS : String = "MesFavoris"
        var SP_Fav : SharedPreferences = getSharedPreferences(FAVORIS, Context.MODE_PRIVATE)
        val editor = SP_Fav.edit()
        editor.putString(teams.team_name,teams.team_key+"|"+leagueId+"|"+teams.team_badge)
        editor.apply()
        //startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams)

        leagueId = intent.getIntExtra("league_id", 1)
        leagueName = intent.getStringExtra("league_name")
        val actionbar = supportActionBar
        actionbar!!.title = leagueName
        actionbar.setDisplayHomeAsUpEnabled(true)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTeams)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        var elements = ArrayList<TeamsList>()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(KEYS.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(FootBallService::class.java)
        val click: OnTeamClickListener = this

        api.getTeams(leagueId, API_KEY).enqueue(object : Callback<List<TeamsList>> {

            override fun onResponse(call: Call<List<TeamsList>>, response: Response<List<TeamsList>>) {
                Log.d(TAG, "onResponse")
                println(response)
                if (response.code() == 200) {
                    println(response)
                    val result: List<TeamsList> = response.body()!!

                    for (team in result){
                        // Some teams don't have any players inside so we don't take them
                        if (team.players.isNotEmpty()) {
                            elements.add(team)
                        }
                    }

                    var sortedElements = elements.sortedWith(compareBy({ it.team_name }))
                    //recyclerView.adapter = TeamAdapter(sortedElements)
                    recyclerView.adapter = TeamAdapter(sortedElements, click)
                }

            }

            override fun onFailure(call: Call<List<TeamsList>>, t: Throwable?) {
                Log.e(TAG, "onFailure", t)
            }
        })
    }


    class TeamAdapter(val itemList: List<TeamsList>, private val itemClickListener: OnTeamClickListener) : RecyclerView.Adapter<TeamAdapter.ViewHolder>() {
    //class TeamAdapter(val itemList: List<TeamsList>) : RecyclerView.Adapter<TeamAdapter.ViewHolder>() {
        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_teams, parent, false)
            return ViewHolder(v)
        }

        //this method is binding the data on the list
        override fun onBindViewHolder(holder: TeamAdapter.ViewHolder, position: Int) {
            //holder.bindItems(itemList[position])
            holder.bindItems(itemList[position], itemClickListener)
        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return itemList.size
        }

        //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItems(item: TeamsList, clickListener: OnTeamClickListener) {
            //fun bindItems(item: TeamsList) {
                val teamName = itemView.findViewById<TextView>(R.id.textViewTeam)
                val teamImg = itemView.findViewById<ImageView>(R.id.imageViewTeam)
                teamName.text = item.team_name
                Glide.with(itemView).load(item.team_badge).into(teamImg)

                val buttonFav = itemView.findViewById<Button>(R.id.button_fav)
                buttonFav.setOnClickListener{
                    clickListener.onItemClicked(item)
                }
                /*itemView.setOnClickListener{
                    clickListener.onItemClicked(item)
                }*/

            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun getTeamId(): Int{
        return teamId
    }

    fun getTeamName(): String{
        return teamName
    }

    fun getLeagueId(): Int{
        return leagueId
    }
}

data class TeamsList(val team_key: String, val team_name: String, val team_badge: String, val players: List<TestPLayer>)
data class TestPLayer(val player_name: String)