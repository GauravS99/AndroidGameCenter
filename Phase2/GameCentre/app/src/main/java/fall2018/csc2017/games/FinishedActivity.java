package fall2018.csc2017.games;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import fall2018.csc2017.slidingtiles.R;

/**
 * An activity to display once the user has completed a game
 */
public class FinishedActivity extends AppCompatActivity implements Observer {

    /**
     * The display for the score achieved
     */
    TextView currentScore;
    /**
     * the display for the user's previous high scores for this game, for this difficulty
     */
    TextView prevScores;
    /**
     * the display for all user's previous high scores for this game, for this difficulty
     */
    TextView topScores;
    /**
     * a Game object for the current game
     */
    Game currentGame;

    /**
     * a list for for the user's previous high scores for this game, for this difficulty
     */
    List<Integer> prevScoreList = new ArrayList<>();
    /**
     * a list for all user's previous high scores for this game, for this difficulty
     */
    List<String[]> topScoreList = new ArrayList<>();
    /**
     * a ScoreboardManager to access scores
     */
    ScoreboardManager dm;

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);
        int lastScore = getIntent().getIntExtra("SCORE", 0);
        currentGame = (Game) getIntent().getSerializableExtra("GAME");

        dm = new ScoreboardManager(currentGame);
        dm.addObserver(this);
        dm.addUserScore(lastScore);

        currentScore = findViewById(R.id.current_score);
        prevScores = findViewById(R.id.prev_scores);
        topScores = findViewById(R.id.top_scores);

        currentScore.setText(lastScore + "");
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o == ScoreboardManager.ADDED_USER_SCORE) {
            prevScoreList = dm.getSortedUserScores();
            topScoreList = dm.getTopScores();
        }
        if (o == ScoreboardManager.RETRIEVED_SCORES) {
            prevScores.setText(userConverter(prevScoreList));
        }
        if (o == ScoreboardManager.RETRIEVED_TOP_SCORES) {
            topScores.setText(overallConverter(topScoreList));
        }
    }

    /**
     * Creates a nice table to display overall top scores
     *
     * @param item the list of overall top scores
     * @return a string that is the overall table of scores
     */
    private String overallConverter(List<String[]> item) {
        int counter = 1;
        StringBuilder topScoresTable = new StringBuilder("Overall Top Scorers\n\n Rank    User    " +
                "Score + \n");

        for (String[] m : item) {
            topScoresTable.append(counter).append("        ").append(m[0]).append("         ")
                    .append(m[1]).append("\n");
            counter += 1;
        }

        return topScoresTable.toString();

    }

    /**
     * Creates a nice table to display the current user's top scores
     *
     * @param item the list of the current user's top scores
     * @return a string that is a table of the current user's top scores
     */
    private String userConverter(List<Integer> item) {
        StringBuilder topScoresTable = new StringBuilder("Your Top Scores\n\n Rank   Score + \n");

        for (int i = 0; i < item.size(); i++) {
            topScoresTable = new StringBuilder(" " + topScoresTable + (i + 1) + "          " +
                    Integer.toString(item.get(i)) + "\n");
        }
        return topScoresTable.toString();

    }

    //empty because we do not want the user to be able go go back to the completed game
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}