package fr.esilv.td6.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.esilv.td6.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface FootBallService{
    @GET("?action=get_leagues")
    fun getLeagues(@Query(value="country_id") country_id: Int, @Query(value="APIkey") apiKey: String): Call<List<Leagues>>
}

interface OnItemClickListener{
    fun onItemClicked(leagues: Leagues)
}

/*
interface FootBallService{
    //@GET("search?part=snippet&type=video&maxResults=50")
    @GET("?action=get_countries")
    //fun search(@Query(value="q") query: String, @Query(value="key") apiKey: String): Call<SearchResult>
    fun search(@Query(value="APIkey") apiKey: String): Call<List<SearchResult>>
}*/

class HomeFragment : Fragment(), OnItemClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private val TAG = "HomeFragment"
    private val API_KEY = "API_KEY"
    private lateinit var recyclerView: RecyclerView
    private lateinit var api: FootBallService

    override fun onItemClicked(league: Leagues) {
        Toast.makeText(getActivity(),"League name ${league.league_name} \n League ID:${league.league_id}",  Toast.LENGTH_LONG).show()
        Log.i("USER_",league.league_name)
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        //val root = LayoutInflater.from(parent.context).inflate(R.layout.fragment_home, parent, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        var root = inflater.inflate(R.layout.fragment_home, container, false)
        //val textView: TextView = root.findViewById(R.id.text_home)
        //val back: Button = root.findViewById(R.id.back_button)
        /*
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
         */
        recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(getActivity(), 2)
        //gridView = root.findViewById<GridView>(R.id.gridView)

        // https://www.googleapis.com/youtube/v3/ https://apiv2.apifootball.com/

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://apiv2.apifootball.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(FootBallService::class.java)
        launchSearch(this)
        return root
    }

    fun launchSearch(click: OnItemClickListener) {
        var leagues = ArrayList<Leagues>()
        api.getLeagues(46, API_KEY).enqueue(object: Callback<List<Leagues>> {

            override fun onResponse(call: Call<List<Leagues>>, response: Response<List<Leagues>>) {
                Log.d(TAG, "onResponse")
                if (response.code() == 200) {
                    val result : List<Leagues> = response.body()!!
                    println(result)
                    //val listItems : List<SearchItem> = result.items

                    for (league in result){
                        leagues.add(league)
                    }

                    var customAdapter = CustomAdapter(leagues, click)
                    recyclerView.adapter = customAdapter
                }
            }

            override fun onFailure(call: Call<List<Leagues>>, t: Throwable?) {
                println("Bug Failure")
                Log.e(TAG, "onFailure", t)
            }
        })
    }


    class CustomAdapter(val itemList: List<Leagues>, val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

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

            fun bindItems(item: Leagues, clickListener: OnItemClickListener) {
                //val snippet: Snippet = item.snippet
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

data class Leagues(val league_id: String, val league_name: String, val league_logo: String)
data class SearchResult(val country_id: String, val country_name: String)