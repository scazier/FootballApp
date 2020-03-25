package fr.esilv.td6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_list_coach.view.*
import kotlinx.android.synthetic.main.layout_list_players.view.*
import kotlinx.android.synthetic.main.layout_players.view.*
import kotlinx.android.synthetic.main.layoutlist_teams.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TeamDetail : AppCompatActivity(){

    private lateinit var api: FootBallService
    //private lateinit var recyclerView: RecyclerView
    private val TAG = "TeamsActivity"
    private val API_KEY = KEYS.API_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layoutlist_teams)

        val teamId = intent.getIntExtra("team_id", 1)
        val teamName = intent.getStringExtra("team_name")
        val leagueId = intent.getIntExtra("league_id", 1)


        val actionbar = supportActionBar
        actionbar!!.title = teamName
        actionbar.setDisplayHomeAsUpEnabled(true)

        var recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTeamDetail)
        recyclerView.layoutManager = LinearLayoutManager(this@TeamDetail, RecyclerView.VERTICAL, false)
        //recyclerView.setHasFixedSize(true)

        var elements = ArrayList<TeamDetailList>()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(KEYS.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(FootBallService::class.java)

        /*api.getTeamDetail(teamId, API_KEY).enqueue(object : Callback<List<TeamDetailList>> {

            override fun onResponse(call: Call<List<TeamDetailList>>, response: Response<List<TeamDetailList>>) {
                Log.d(TAG, "onResponse")
                println(response)
                if (response.code() == 200) {
                    val result: List<TeamDetailList> = response.body()!!

                    for (team in result) {
                        elements.add(team)
                    }
                    println(elements)
                    println(recyclerView.layoutManager)
                    recyclerView.adapter = TeamDetailAdapter(elements)
                }

            }

            override fun onFailure(call: Call<List<TeamDetailList>>, t: Throwable?) {
                Log.e(TAG, "onFailure", t)
            }
        })*/
    }

    class TeamDetailAdapter(private val itemList: List<TeamDetailList>) : RecyclerView.Adapter<TeamDetailAdapter.ViewHolder>() {

        private val viewPool = RecyclerView.RecycledViewPool()

        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamDetailAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_players, parent, false)
            return ViewHolder(v)
        }

        //this method is binding the data on the list
        override fun onBindViewHolder(holder: TeamDetailAdapter.ViewHolder, position: Int) {
            val item = itemList[position]
            val playerLayoutManager = LinearLayoutManager(holder.playerRecyclerView.context, RecyclerView.VERTICAL, false)
            val coachLayoutManager = LinearLayoutManager(holder.coachRecyclerView.context, RecyclerView.VERTICAL, false)

            //playerLayoutManager.initialPrefetchItemCount = 4
            holder.playerRecyclerView.layoutManager = playerLayoutManager
            holder.playerRecyclerView.adapter = PlayerAdapter(item.players)

            holder.coachRecyclerView.layoutManager = coachLayoutManager
            holder.coachRecyclerView.adapter = CoachAdapter(item.coaches)

        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return itemList.size
        }

        //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val playerRecyclerView: RecyclerView = itemView.recyclerViewPlayer
            val coachRecyclerView: RecyclerView = itemView.recyclerViewCoach
        }
    }

    class PlayerAdapter(private val itemList: List<Player>) : RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_list_players, parent, false)
            return ViewHolder(v)
        }

        //this method is binding the data on the list
        override fun onBindViewHolder(holder: PlayerAdapter.ViewHolder, position: Int) {
            val item = itemList[position]
            holder.playerName.text = item.player_name
            holder.playerAge.text = item.player_age
            holder.playerCountry.text = item.player_country
            holder.playerType.text = item.player_type
            holder.playerYellow.text = item.player_yellow_cards
            holder.playerRed.text = item.player_red_cards
            holder.playerGoals.text = item.player_goals
            holder.playerMatch.text = item.player_match_played
            holder.playerNumber.text = item.player_number
        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return itemList.size
        }

        //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
            val playerNumber = itemView.findViewById<TextView>(R.id.textViewPlayerNumber)
            /*val playerName: TextView = itemView.textViewPlayerName
            val playerAge: TextView = itemView.textViewPlayerAge
            val playerCountry: TextView = itemView.textViewPlayerCountry
            val playerType: TextView = itemView.textViewPlayerType
            val playerYellow: TextView = itemView.textViewPlayerYellowCards
            val playerRed: TextView = itemView.textViewPlayerRedCards
            val playerGoals: TextView = itemView.textViewPlayerGoals
            val playerMatch: TextView = itemView.textViewPlayerMatchPlayed
            val playerNumber: TextView = itemView.textViewPlayerNumber*/


        }
    }

    class CoachAdapter(private val itemList: List<Coach>) : RecyclerView.Adapter<CoachAdapter.ViewHolder>() {

        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoachAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_list_coach, parent, false)
            return ViewHolder(v)
        }

        //this method is binding the data on the list
        override fun onBindViewHolder(holder: CoachAdapter.ViewHolder, position: Int) {
            val item = itemList[position]
            holder.coachName.text = item.coach_name
            holder.coachAge.text = item.coach_age
            holder.coachCountry.text = item.coach_country
        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return itemList.size
        }

        //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            /*val coachName: TextView = itemView.textViewCoachName
            val coachAge: TextView = itemView.textViewCoachAge
            val coachCountry: TextView = itemView.textViewCoachCountry*/

            val coachName = itemView.findViewById<TextView>(R.id.textViewCoachName)
            val coachAge = itemView.findViewById<TextView>(R.id.textViewCoachAge)
            val coachCountry = itemView.findViewById<TextView>(R.id.textViewCoachCountry)

        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

data class TeamDetailList(val players: List<Player>, val coaches: List<Coach>)
//data class TeamDetailList(val players: List<Player>)
data class Player(val player_name: String, val player_country: String, val player_age: String, val player_number: String, val player_type: String, val player_match_played: String, val player_goals: String, val player_yellow_cards: String, val player_red_cards: String)
data class Coach(val coach_name: String, val coach_country: String, val coach_age: String)
