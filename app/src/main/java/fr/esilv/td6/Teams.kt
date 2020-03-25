package fr.esilv.td6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.esilv.td6.ui.home.FootBallService
import fr.esilv.td6.ui.home.KEYS
import fr.esilv.td6.ui.home.Leagues
import fr.esilv.td6.ui.home.OnItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OnTeamlickListener{
    fun onItemClicked(teams: TeamsList)
}

class Teams : AppCompatActivity(), OnTeamlickListener {

    private lateinit var api: FootBallService
    private lateinit var recyclerView: RecyclerView
    private val TAG = "TeamsActivity"
    private val API_KEY = KEYS.API_KEY


    override fun onItemClicked(teams: TeamsList) {
        Toast.makeText(this,"League name ${teams.team_name} \n League ID:${teams.team_key}",  Toast.LENGTH_LONG).show()
        Log.i("USER_",teams.team_name)
        //val intent = Intent(this, Teams::class.java)
        //intent.putExtra("league_id", teams.team_key)
        //intent.putExtra("league_name", teams.team_name)
        //startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams)

        val leagueId = intent.getIntExtra("league_id", 1)
        val leagueName = intent.getStringExtra("league_name")
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
        val click: OnTeamlickListener = this

        api.getTeams(leagueId, API_KEY).enqueue(object : Callback<List<TeamsList>> {

            override fun onResponse(call: Call<List<TeamsList>>, response: Response<List<TeamsList>>) {
                Log.d(TAG, "onResponse")
                println(response)
                if (response.code() == 200) {
                    println(response)
                    val result: List<TeamsList> = response.body()!!

                    for (team in result){
                        elements.add(team)
                    }

                    var sortedElements = elements.sortedWith(compareBy({ it.team_name }))
                    recyclerView.adapter = TeamAdapter(sortedElements, click)
                }

            }

            override fun onFailure(call: Call<List<TeamsList>>, t: Throwable?) {
                Log.e(TAG, "onFailure", t)
            }
        })
    }


    class TeamAdapter(val itemList: List<TeamsList>, private val itemClickListener: OnTeamlickListener) : RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_teams, parent, false)
            return ViewHolder(v)
        }

        //this method is binding the data on the list
        override fun onBindViewHolder(holder: TeamAdapter.ViewHolder, position: Int) {
            holder.bindItems(itemList[position], itemClickListener)
        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return itemList.size
        }

        //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItems(item: TeamsList, clickListener: OnTeamlickListener) {
                val teamName = itemView.findViewById<TextView>(R.id.textViewTeam)
                val teamImg = itemView.findViewById<ImageView>(R.id.imageViewTeam)
                teamName.text = item.team_name
                Glide.with(itemView).load(item.team_badge).into(teamImg)

                itemView.setOnClickListener{
                    clickListener.onItemClicked(item)
                }

            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

data class TeamsList(val team_key: String, val team_name: String, val team_badge: String)