package chess;

import android.content.Intent;
import android.os.Bundle;

import chess.R;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import java.io.File;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Button gameButton = (Button)findViewById(R.id.playGameButton);
        gameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomeScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button recordingButton = (Button)findViewById(R.id.playRecordingButton);
        recordingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(HomeScreen.this, Playback.class);
                //commented out code to use if you want to delete all replays
//                File dir = getFilesDir();
//                String[] paths = dir.list();
//                for(String path : paths){
//                    File file = new File(dir, path);
//                    boolean deleted = file.delete();
//                }
                startActivity(intent);
            }
        });
    }
}