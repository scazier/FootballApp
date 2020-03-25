package fr.esilv.td6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TeamDetail : AppCompatActivity(){

    private lateinit var api: FootBallService
    private lateinit var recyclerView: RecyclerView
    private val TAG = "TeamsActivity"
    private val API_KEY = KEYS.API_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        val teamId = intent.getIntExtra("team_id", 1)
        val teamName = intent.getStringExtra("team_name")

        val actionbar = supportActionBar
        actionbar!!.title = teamName
        actionbar.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTeamDetail)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        var elements = ArrayList<TeamDetailList>()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(KEYS.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(FootBallService::class.java)

        api.getTeamDetail(teamId, API_KEY).enqueue(object : Callback<List<TeamDetailList>> {

            override fun onResponse(call: Call<List<TeamDetailList>>, response: Response<List<TeamDetailList>>) {
                Log.d(TAG, "onResponse")
                println(response)
                if (response.code() == 200) {
                    println(response)
                    val result: List<TeamDetailList> = response.body()!!

                    for (team in result) {
                        elements.add(team)
                    }

                    recyclerView.adapter = TeamDetailAdapter(elements)
                }

            }

            override fun onFailure(call: Call<List<TeamDetailList>>, t: Throwable?) {
                Log.e(TAG, "onFailure", t)
            }
        })
    }

    class TeamDetailAdapter(val itemList: List<TeamDetailList>) :
        RecyclerView.Adapter<TeamDetailAdapter.ViewHolder>() {

        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamDetailAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_team_detail, parent, false)
            return ViewHolder(v)
        }

        //this method is binding the data on the list
        override fun onBindViewHolder(holder: TeamDetailAdapter.ViewHolder, position: Int) {
            holder.bindItems(itemList[position])
        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return itemList.size
        }

        //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItems(item: TeamDetailList) {
                val players: List<Player> = item.players
                val coaches: List<Coach> = item.coaches

                PlayerAdapter(players)
                CoachAdapter(coaches)

            }
        }
    }

    class PlayerAdapter(val itemList: List<Player>) :
        RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_team_detail, parent, false)
            return ViewHolder(v)
        }

        //this method is binding the data on the list
        override fun onBindViewHolder(holder: PlayerAdapter.ViewHolder, position: Int) {
            holder.bindItems(itemList[position])
        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return itemList.size
        }

        //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItems(item: Player) {
                //val players: List<Player> = item.players
                //val coaches: List<Coach> = item.coaches

                val playerName = itemView.findViewById<TextView>(R.id.textViewPlayerName)
                val playerAge = itemView.findViewById<TextView>(R.id.textViewPlayerAge)
                val playerCountry = itemView.findViewById<TextView>(R.id.textViewPlayerCountry)
                val playerType = itemView.findViewById<TextView>(R.id.textViewPlayerType)
                val playerYellow = itemView.findViewById<TextView>(R.id.textViewPlayerYellowCards)
                val playerRed = itemView.findViewById<TextView>(R.id.textViewPlayerRedCards)
                val playerGoals = itemView.findViewById<TextView>(R.id.textViewPlayerGoals)
                val playerMatch = itemView.findViewById<TextView>(R.id.textViewPlayerMatchPlayed)
                val playerNumber= itemView.findViewById<TextView>(R.id.textViewPlayerNumber)

                playerName.text = item.player_name
                playerAge.text = item.player_age
                playerCountry.text = item.player_country
                playerType.text = item.player_type
                playerYellow.text = item.player_yellow_cards
                playerRed.text = item.player_red_cards
                playerGoals.text = item.player_goals
                playerMatch.text = item.player_match_played
                playerNumber.text = item.player_number
            }
        }
    }

    class CoachAdapter(val itemList: List<Coach>) :
        RecyclerView.Adapter<CoachAdapter.ViewHolder>() {

        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoachAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_team_detail, parent, false)
            return ViewHolder(v)
        }

        //this method is binding the data on the list
        override fun onBindViewHolder(holder: CoachAdapter.ViewHolder, position: Int) {
            holder.bindItems(itemList[position])
        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return itemList.size
        }

        //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItems(item: Coach) {

                val coachName = itemView.findViewById<TextView>(R.id.textViewPlayerName)
                val coahcAge = itemView.findViewById<TextView>(R.id.textViewPlayerAge)
                val coachCountry = itemView.findViewById<TextView>(R.id.textViewPlayerCountry)

                coachName.text = item.coach_name
                coahcAge.text = item.coach_age
                coachCountry.text = item.coach_country
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

data class TeamDetailList(val players: List<Player>, val coaches: List<Coach>)
data class Player(val player_name: String, val player_country: String, val player_age: String, val player_number: String, val player_type: String, val player_match_played: String, val player_goals: String, val player_yellow_cards: String, val player_red_cards: String)
data class Coach(val coach_name: String, val coach_country: String, val coach_age: String)
