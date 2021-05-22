package chess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.view.View;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class
Playback extends AppCompatActivity {

    ListView listView;
    Button sortNameButton;
    Button sortDateButton;

    List<String> replayNamesAL = new ArrayList<String>();
    String[] replayNamesArr;

    public static final String GAME_NAME = "game_name";

    Context thisContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        sortNameButton = findViewById(R.id.sortNameButton);
        sortDateButton = findViewById(R.id.sortDateButton);

        //get replay names
        File dir = getFilesDir();
        String[] paths = dir.list();

        System.out.println(dir.getName());

        for(String path : paths){
            //System.out.println(path);
            replayNamesAL.add(path);
        }

        replayNamesArr = replayNamesAL.toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.replay_name, replayNamesArr);

        listView = findViewById(R.id.replays_list);
        listView.setAdapter(adapter);

        //for when a replay is selected
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                replayGame(i);
            }
        });

        sortNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(replayNamesAL);
                replayNamesArr = replayNamesAL.toArray(new String[0]);
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(thisContext, R.layout.replay_name, replayNamesArr);
                listView.setAdapter(adapter1);
            }
        });

        sortDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Arrays.sort(replayNamesArr, new Comparator<String>(){
                    public int compare(String str1, String str2){
                        String sub1 = str1.substring(str1.indexOf("|")+2, str1.length());
                        String sub2 = str2.substring(str2.indexOf("|")+2, str2.length());

                        return sub2.compareTo(sub1);
                    }
                });
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(thisContext, R.layout.replay_name, replayNamesArr);
                listView.setAdapter(adapter1);
            }
        });
    }

    private void replayGame(int pos){
        Bundle bundle = new Bundle();
        bundle.putString(GAME_NAME, replayNamesArr[pos]);
        Intent intent = new Intent(this, ReplayGame.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}