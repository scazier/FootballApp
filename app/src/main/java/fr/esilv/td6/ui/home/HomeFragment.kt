package fr.esilv.td6.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.esilv.td6.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*val activity: bottom = (bottom) getActivity()
        val teamId = (activity).getTeamId()
        val teamName = intent.getStringExtra("team_name")
        val leagueId = intent.getIntExtra("league_id", 1)*/
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        /*val teamId = getActivity()?.getIntent()?.getExtras().getInt("team_id", 1)


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

        val api = retrofit.create(FootBallService::class.java)*/

        return root
    }

}
