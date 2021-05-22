package chess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;

public class ReplayGame extends AppCompatActivity {

    GridView gridView;
    TextView playersTurn;
    Button nextMove;
    int currentMovePosition = 0;

    Context thisContext = this;

    int[] squares = {
            R.drawable.whitesquare, R.drawable.greensquare, R.drawable.whitesquare,R.drawable.greensquare,R.drawable.whitesquare,R.drawable.greensquare, R.drawable.whitesquare, R.drawable.greensquare,
            R.drawable.greensquare, R.drawable.whitesquare, R.drawable.greensquare, R.drawable.whitesquare,R.drawable.greensquare,R.drawable.whitesquare,R.drawable.greensquare, R.drawable.whitesquare,
            R.drawable.whitesquare, R.drawable.greensquare, R.drawable.whitesquare,R.drawable.greensquare,R.drawable.whitesquare,R.drawable.greensquare,R.drawable.whitesquare, R.drawable.greensquare,
            R.drawable.greensquare, R.drawable.whitesquare, R.drawable.greensquare, R.drawable.whitesquare,R.drawable.greensquare,R.drawable.whitesquare,R.drawable.greensquare, R.drawable.whitesquare,
            R.drawable.whitesquare,R.drawable.greensquare, R.drawable.whitesquare, R.drawable.greensquare, R.drawable.whitesquare,R.drawable.greensquare,R.drawable.whitesquare,R.drawable.greensquare,
            R.drawable.greensquare, R.drawable.whitesquare, R.drawable.greensquare, R.drawable.whitesquare,R.drawable.greensquare,R.drawable.whitesquare,R.drawable.greensquare, R.drawable.whitesquare,
            R.drawable.whitesquare,R.drawable.greensquare, R.drawable.whitesquare, R.drawable.greensquare, R.drawable.whitesquare,R.drawable.greensquare,R.drawable.whitesquare,R.drawable.greensquare,
            R.drawable.greensquare,R.drawable.whitesquare,R.drawable.greensquare, R.drawable.whitesquare,R.drawable.greensquare,R.drawable.whitesquare,R.drawable.greensquare,R.drawable.whitesquare
    };

    int[] pieces = {
            R.drawable.blackrook, R.drawable.blackknight, R.drawable.blackbishop, R.drawable.blackqueen, R.drawable.blackking, R.drawable.blackbishop, R.drawable.blackknight, R.drawable.blackrook,
            R.drawable.blackpawn, R.drawable.blackpawn, R.drawable.blackpawn, R.drawable.blackpawn, R.drawable.blackpawn, R.drawable.blackpawn, R.drawable.blackpawn, R.drawable.blackpawn,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            R.drawable.whitepawn, R.drawable.whitepawn, R.drawable.whitepawn, R.drawable.whitepawn, R.drawable.whitepawn, R.drawable.whitepawn, R.drawable.whitepawn, R.drawable.whitepawn,
            R.drawable.whiterook, R.drawable.whiteknight, R.drawable.whitebishop, R.drawable.whitequeen, R.drawable.whiteking, R.drawable.whitebishop, R.drawable.whiteknight, R.drawable.whiterook
    };

    int firstSelected = -1;
    int secondSelected = -1;

    boolean disabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setting up game
        final Board[] current = {new Board()};
        final Board[] previous = {new Board()};

        final boolean[] checkMate = {false};
        final boolean[] whitesMove = {true};
        final boolean[] cont = {false};
        final int[] lastSelected1 = new int[1];
        final int[] lastSelected2 = new int[1];
        final boolean[] undoClicked = {true};

        ArrayList<Integer> whitePieces = new ArrayList<Integer>();
        ArrayList<Integer> blackPieces = new ArrayList<Integer>();
        whitePieces.add(R.drawable.whiterook);
        whitePieces.add(R.drawable.whiteknight);
        whitePieces.add(R.drawable.whitebishop);
        whitePieces.add(R.drawable.whitequeen);
        whitePieces.add(R.drawable.whiteking);
        whitePieces.add(R.drawable.whitepawn);
        blackPieces.add(R.drawable.blackrook);
        blackPieces.add(R.drawable.blackknight);
        blackPieces.add(R.drawable.blackbishop);
        blackPieces.add(R.drawable.blackqueen);
        blackPieces.add(R.drawable.blackking);
        blackPieces.add(R.drawable.blackpawn);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay_game);

        gridView = findViewById(R.id.gridView);
        playersTurn = findViewById(R.id.turn);
        nextMove = findViewById(R.id.nextButton);

        Context thisContext = this;
        ReplayGame.CustomAdapter customAdapter = new ReplayGame.CustomAdapter(squares, pieces,this);
        gridView.setAdapter(customAdapter);
        playersTurn.setText("White's Turn");


        //get filename
        Bundle bundle = getIntent().getExtras();
        String fileName = bundle.getString(Playback.GAME_NAME);

        ArrayList<String> moves = new ArrayList<String>();
        FileInputStream fis = null;
        try{
            fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) != null){
                moves.add(line);
                System.out.println(line);
            }
            fis.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }

        nextMove.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(disabled) return;
                //move the piece. dont have to check for check because we already know it works since its a replay
                String currentMove = moves.get(currentMovePosition);
                currentMovePosition++;
                if(currentMovePosition % 2 == 0){
                    playersTurn.setText("White's Turn");
                }
                else{
                    playersTurn.setText("Black's Turn");
                }

                if(!currentMove.equals("resign") && !currentMove.equals("draw")){
                    String[] splitMove = currentMove.split(" ");

                    int[] firstCoord = Board.rankFileToIndex(splitMove[0]);
                    int[] secondCoord = Board.rankFileToIndex(splitMove[0]);

                    int row = firstCoord[0];
                    int col = firstCoord[1];

                    int rowToMove = secondCoord[0];
                    int colToMove = secondCoord[1];

                    current[0].board[row][col].move(current[0].board, currentMove);
                }

                //update images
                int index;
                for(int k = 0; k < current[0].board.length; k++) {
                    for(int j = 0; j < current[0].board[0].length; j++) {
                        index = k*8+j;

                        if (current[0].board[k][j] == null && pieces[index] != 0) {
                            pieces[index] = 0;
                        }

                        if (current[0].board[k][j] instanceof King && current[0].board[k][j].getColor() == 'b') {
                            pieces[index] = R.drawable.blackking;
                        } else if (current[0].board[k][j] instanceof King && current[0].board[k][j].getColor() == 'w') {
                            pieces[index] = R.drawable.whiteking;
                        } else if (current[0].board[k][j] instanceof Queen && current[0].board[k][j].getColor() == 'b') {
                            pieces[index] = R.drawable.blackqueen;
                        } else if (current[0].board[k][j] instanceof Queen && current[0].board[k][j].getColor() == 'w') {
                            pieces[index] = R.drawable.whitequeen;
                        } else if (current[0].board[k][j] instanceof Rook && current[0].board[k][j].getColor() == 'w') {
                            pieces[index] = R.drawable.whiterook;
                        } else if (current[0].board[k][j] instanceof Rook && current[0].board[k][j].getColor() == 'b') {
                            pieces[index] = R.drawable.blackrook;
                        } else if (current[0].board[k][j] instanceof Knight && current[0].board[k][j].getColor() == 'w') {
                            pieces[index] = R.drawable.whiteknight;
                        } else if (current[0].board[k][j] instanceof Knight && current[0].board[k][j].getColor() == 'b') {
                            pieces[index] = R.drawable.blackknight;
                        } else if (current[0].board[k][j] instanceof Bishop && current[0].board[k][j].getColor() == 'w') {
                            pieces[index] = R.drawable.whitebishop;
                        } else if (current[0].board[k][j] instanceof Bishop && current[0].board[k][j].getColor() == 'b') {
                            pieces[index] = R.drawable.blackbishop;
                        } else if (current[0].board[k][j] instanceof Pawn && current[0].board[k][j].getColor() == 'w') {
                            pieces[index] = R.drawable.whitepawn;
                        } else if (current[0].board[k][j] instanceof Pawn && current[0].board[k][j].getColor() == 'b') {
                            pieces[index] = R.drawable.blackpawn;
                        }
                    }
                }

                if(currentMovePosition % 2 == 0 && currentMovePosition-1 == moves.size()-1){
                    disabled = true;
                    playersTurn.setText("Black Wins!");
                }
                else if(currentMovePosition % 2 == 1 && currentMovePosition-1 == moves.size()-1){
                    disabled = true;
                    playersTurn.setText("White Wins!");
                }

                if(currentMove.equals("resign")){
                    if(playersTurn.getText().toString().equals("White's Turn")){
                        playersTurn.setText("Black Resigns: White Wins");
                    }
                    else playersTurn.setText("White Resigns: Black Wins");
                    disabled = true;
                }

                if(currentMove.equals("draw")){
                    playersTurn.setText("Draw");
                    disabled = true;
                }

                ReplayGame.CustomAdapter customAdapter1 = new ReplayGame.CustomAdapter(squares, pieces, thisContext);
                gridView.setAdapter(customAdapter1);

//                if(currentMovePosition == moves.size()-1 && moves.get(moves.size()-1).equals("draw")){
//                    disabled = true;
//                    playersTurn.setText("Draw");
//                }
//
//                if(currentMovePosition == moves.size()-1 && moves.get(moves.size()-1).equals("resign")){
//                    if(currentMovePosition % 2 == 0){
//                        disabled = true;
//                        playersTurn.setText("Black Wins!");
//                    }
//                    else{
//                        disabled = true;
//                        playersTurn.setText("White Wins!");
//                    }
//                }
            }
        });
    }

    public class CustomAdapter extends BaseAdapter {
        private int[] imagePhotos;
        private int[] piecePhotos;
        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapter(int[] imagePhotos, int[] piecePhotos, Context context) {
            this.imagePhotos = imagePhotos;
            this.piecePhotos = piecePhotos;
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imagePhotos.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null){
                view = layoutInflater.inflate(R.layout.board, viewGroup, false);
            }

            ImageView imageView = view.findViewById(R.id.square);
            imageView.setImageResource(imagePhotos[i]);

            ImageView piece = view.findViewById(R.id.piece);
            piece.setImageResource(piecePhotos[i]);


            return view;
        }
    }
}