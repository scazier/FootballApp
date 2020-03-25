package fr.esilv.td6.ui.notifications

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.esilv.td6.KEYS
import fr.esilv.td6.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val TMP:String = "DataTemp"

interface FootBallService{
    @GET("?action=get_standings")
    fun search(@Query(value="league_id")league_id: String?, @Query(value="APIkey") apiKey: String): Call<List<SearchStandings>>
}

class NotificationsFragment : Fragment() {

    fun getLeagueId(sp_name : String) : String?{
        val SPID : SharedPreferences = activity!!.getSharedPreferences(sp_name, Context.MODE_PRIVATE)
        val leagueid : String? = SPID.getString("leagueID",null)
        return leagueid
    }

    private lateinit var notificationsViewModel: NotificationsViewModel
    private val TAG = "StandingsFragment"
    private val API_KEY = KEYS.API_KEY
    private val LEAGUE_ID = getLeagueId(TMP)
    private lateinit var recyclerView: RecyclerView
    private lateinit var api: FootBallService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false)

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://apiv2.apifootball.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(FootBallService::class.java)
        launchSearch()
        return root
    }
    fun launchSearch() {
        var standings = ArrayList<SearchStandings>()
        val call: Call<List<SearchStandings>> = api.search(LEAGUE_ID,API_KEY)
        call.enqueue(object: Callback<List<SearchStandings>> {

            override fun onResponse(call: Call<List<SearchStandings>>, response: Response<List<SearchStandings>>) {
                Log.d(TAG, "onResponse")
                if (response.code() == 200) {
                    val result : List<SearchStandings> = response.body()!!
                    println(result)

                    for (stand in result){
                        standings.add(stand)
                    }

                    recyclerView.adapter = CustomAdapter(standings)
                }
            }

            override fun onFailure(call: Call<List<SearchStandings>>, t: Throwable?) {
                println("Bug Failure")
                Log.e(TAG, "onFailure", t)
            }
        })
    }

    class CustomAdapter(val itemList: List<SearchStandings>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.stand_layout, parent, false)
            return ViewHolder(v)
        }

        //this method is binding the data on the list
        override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
            holder.bindItems(itemList[position])
        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return itemList.size
        }

        //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItems(item: SearchStandings) {
                val textViewName  = itemView.findViewById<TextView>(R.id.textViewName)
                val imageViewBadge = itemView.findViewById<ImageView>(R.id.imageViewBadge)
                val textViewPos = itemView.findViewById<TextView>(R.id.textViewPos)
                val textViewPlayed  = itemView.findViewById<TextView>(R.id.textViewPlayed)
                val textViewWins  = itemView.findViewById<TextView>(R.id.textViewWins)
                val textViewDraws  = itemView.findViewById<TextView>(R.id.textViewDraws)
                val textViewLoss  = itemView.findViewById<TextView>(R.id.textViewLoss)
                val textViewGF  = itemView.findViewById<TextView>(R.id.textViewGF)
                val textViewGA  = itemView.findViewById<TextView>(R.id.textViewGA)
                val textViewPoints  = itemView.findViewById<TextView>(R.id.textViewPoints)

                textViewName.text = item.team_name
                Glide.with(itemView).load(item.team_badge).into(imageViewBadge)
                textViewPos.text = item.overall_league_position
                textViewPlayed.text = item.overall_league_payed
                textViewWins.text = item.overall_league_W
                textViewDraws.text = item.overall_league_D
                textViewLoss.text = item.overall_league_L
                textViewGF.text = item.overall_league_GF
                textViewGA.text = item.overall_league_GA
                textViewPoints.text = item.overall_league_PTS

            }
        }
    }
}


data class SearchStandings(val team_name: String, val team_badge: String,
                           val overall_league_position: String, val overall_league_payed: String,
                           val overall_league_W: String, val overall_league_D: String,
                           val overall_league_L: String, val overall_league_GF: String,
                           val overall_league_GA: String, val overall_league_PTS: String)