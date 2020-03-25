package fr.esilv.td6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.main_button)
        button.setOnClickListener {
            val intent = Intent(this, bottom::class.java)
            startActivity(intent)
        }

        val teamsButton = findViewById<Button>(R.id.leagues_button)
        teamsButton.setOnClickListener {
            val intent = Intent(this, Leagues::class.java)
            startActivity(intent)
        }
    }
}


interface FootBallService{
    @GET("?action=get_leagues")
    fun getLeagues(@Query(value="country_id") country_id: Int, @Query(value="APIkey") apiKey: String): Call<List<League>>

    @GET("?action=get_teams")
    fun getTeams(@Query(value="league_id") league_id: Int, @Query(value="APIkey") apiKey: String): Call<List<TeamsList>>

    /*@GET("?action=get_teams")
    fun getTeamDetail(@Query(value="team_id") team_id: Int, @Query(value="APIkey") apiKey: String): Call<List<TeamDetailList>>
*/
}


class KEYS{
    companion object{
        const val API_KEY: String = "6707c229027cce084037a2264d4b4e8116f19f7953f03a8a942a8245e1278758"
        const val BASE_URL: String = "https://apiv2.apifootball.com/"
    }
}