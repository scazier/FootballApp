package fr.esilv.td6

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import fr.esilv.td6.ui.home.HomeFragment

class bottom : AppCompatActivity() {

    private var teamId: Int = -1
    private var teamName: String = ""
    private var leagueId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom)


        teamId = intent.getIntExtra("team_id", 1)
        //teamName = intent.getStringExtra("team_name")
        leagueId = intent.getIntExtra("league_id", 1)

        TEMPDATA.leagueID = leagueId
        TEMPDATA.teamID = teamId

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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


class TEMPDATA{
    companion object{
        var teamID: Int = -1
        var leagueID: Int = -1

    }
}