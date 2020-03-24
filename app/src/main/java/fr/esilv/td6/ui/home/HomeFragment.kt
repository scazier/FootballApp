package fr.esilv.td6.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.esilv.td6.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FootBallService{
    //@GET("search?part=snippet&type=video&maxResults=50")
    @GET("?action=get_countries")
    //fun search(@Query(value="q") query: String, @Query(value="key") apiKey: String): Call<SearchResult>
    fun search(@Query(value="APIkey") apiKey: String): Call<List<SearchResult>>
}

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private val TAG = "HomeFragment"
    //private val API_KEY = "AIzaSyDnCBylJZCs5wA460a-wNzKc88FY3a71P4"
    private val API_KEY = "6707c229027cce084037a2264d4b4e8116f19f7953f03a8a942a8245e1278758"
    private lateinit var recyclerView: RecyclerView
    private lateinit var api: FootBallService
    //private lateinit var root: LayoutInflater

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
        recyclerView.layoutManager = LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false)

        // https://www.googleapis.com/youtube/v3/ https://apiv2.apifootball.com/

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://apiv2.apifootball.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(FootBallService::class.java)
        launchSearch()
        return root
    }

    fun launchSearch() {
        var countries = ArrayList<SearchResult>()
        val call: Call<List<SearchResult>> = api.search(API_KEY)
        call.enqueue(object: Callback<List<SearchResult>> {

            override fun onResponse(call: Call<List<SearchResult>>, response: Response<List<SearchResult>>) {
                Log.d(TAG, "onResponse")
                if (response.code() == 200) {
                    val result : List<SearchResult> = response.body()!!
                    println(result)
                    //val listItems : List<SearchItem> = result.items

                    for (country in result){
                        countries.add(country)
                    }

                    recyclerView.adapter = CustomAdapter(countries)
                }
            }

            override fun onFailure(call: Call<List<SearchResult>>, t: Throwable?) {
                println("Bug Failure")
                Log.e(TAG, "onFailure", t)
            }
        })
    }


    class CustomAdapter(val itemList: List<SearchResult>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
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

            fun bindItems(item: SearchResult) {
                //val snippet: Snippet = item.snippet
                val textViewName = itemView.findViewById<TextView>(R.id.textViewCountryName)
                val textViewDesc  = itemView.findViewById<TextView>(R.id.textViewCountryId)
                //val imageViewThumbnail = itemView.findViewById<ImageView>(R.id.imageThumbnail)
                textViewName.text = item.country_id
                textViewDesc.text = item.country_name
                //Glide.with(itemView).load(snippet.thumbnails.default.url).into(imageViewThumbnail)

                itemView.setOnClickListener(View.OnClickListener {
                    fun onClick(v: View){
                        //DetailActivity().start(v.getContext(), item.id.videoId)
                    }
                })

            }
        }
    }


}

data class SearchResult(val country_id: String, val country_name: String)
