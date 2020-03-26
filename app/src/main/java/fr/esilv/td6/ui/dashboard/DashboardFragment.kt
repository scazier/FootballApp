package fr.esilv.td6.ui.dashboard

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.esilv.td6.KEYS
import fr.esilv.td6.R
import fr.esilv.td6.TEMPDATA
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val TMP:String = "DataTemp"
interface FootBallService{
    @GET("?action=get_events&from=2019-02-01&to=2019-06-20")
    fun search(@Query(value="team_id")team_id: String?, @Query(value="APIkey") apiKey: String): Call<List<SearchEvents>>
}

class DashboardFragment : Fragment() {



    private lateinit var dashboardViewModel: DashboardViewModel
    private val TAG = "GamesFragment"
    private lateinit var recyclerView: RecyclerView
    private lateinit var api: FootBallService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
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
        var events = ArrayList<SearchEvents>()
        val call: Call<List<SearchEvents>> = api.search(TEMPDATA.teamID.toString(),KEYS.API_KEY)
        call.enqueue(object: Callback<List<SearchEvents>> {

            override fun onResponse(call: Call<List<SearchEvents>>, response: Response<List<SearchEvents>>) {
                Log.d(TAG, "onResponse")
                if (response.code() == 200) {
                    val result : List<SearchEvents> = response.body()!!
                    println(result)

                    for (event in result){
                        events.add(event)
                    }

                    recyclerView.adapter = CustomAdapter(events)
                }
            }

            override fun onFailure(call: Call<List<SearchEvents>>, t: Throwable?) {
                println("Bug Failure")
                Log.e(TAG, "onFailure", t)
            }
        })
    }

    class CustomAdapter(val itemList: List<SearchEvents>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.events_layout, parent, false)
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

            fun bindItems(item: SearchEvents) {
                val textViewHTName  = itemView.findViewById<TextView>(R.id.textViewHTName)
                val textViewHTScore = itemView.findViewById<TextView>(R.id.textViewHTScore)
                val imageViewHTBadge = itemView.findViewById<ImageView>(R.id.imageViewHTBadge)
                val textViewAName  = itemView.findViewById<TextView>(R.id.textViewAName)
                val textViewAScore  = itemView.findViewById<TextView>(R.id.textViewAScore)
                val imageViewABadge = itemView.findViewById<ImageView>(R.id.imageViewABadge)
                val textViewMDate  = itemView.findViewById<TextView>(R.id.textViewMDate)
                val textViewMStatus  = itemView.findViewById<TextView>(R.id.textViewMStatus)
                val textViewMTime  = itemView.findViewById<TextView>(R.id.textViewMTime)

                textViewHTName.text = item.match_hometeam_name
                textViewHTScore.text = item.match_hometeam_score
                Glide.with(itemView).load(item.team_home_badge).into(imageViewHTBadge)
                textViewAName.text = item.match_awayteam_name
                textViewAScore.text = item.match_awayteam_score
                Glide.with(itemView).load(item.team_away_badge).into(imageViewABadge)
                textViewMDate.text = item.match_date
                textViewMStatus.text = item.match_status
                textViewMTime.text = item.match_time
            }
        }
    }
}

data class SearchEvents(val match_id: String, val match_hometeam_name: String,
                        val match_awayteam_name: String, val match_hometeam_score: String,
                        val match_awayteam_score: String, val team_home_badge: String,
                        val team_away_badge: String, val match_date: String,
                        val match_status: String, val match_time: String)