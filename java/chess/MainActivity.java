package chess;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import chess.R;

import pieces.*;
import chess.*;


public class MainActivity extends AppCompatActivity {

    GridView gridView;
    TextView playersTurn;
    Button undoBTN;
    Button drawBTN;
    Button resignBTN;
    Button aiBTN;
    TextView saveGame;
    EditText editText;
    Button submit;


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

    ArrayList<Integer> blackSquares = new ArrayList<Integer>();
    ArrayList<Integer> whiteSquares = new ArrayList<Integer>();

    int firstSelected = -1;
    int secondSelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setting up game
        final Board[] current = {new Board()};
        final Board[] previous = {new Board()};

        //String userMove;
        final boolean[] checkMate = {false};
        final boolean[] whitesMove = {true};
        final boolean[] cont = {false};
        final boolean[] undoClicked = {true};

        ArrayList<String> moves = new ArrayList<String>();

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

        for (int i = 0; i < 16; i++){
            blackSquares.add(i);
            whiteSquares.add(i+48);
        }

        // Setting Up View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);
        playersTurn = findViewById(R.id.turn);
        undoBTN = findViewById(R.id.undoButton);
        drawBTN = findViewById(R.id.drawButton);
        resignBTN = findViewById(R.id.resignButton);
        aiBTN = findViewById(R.id.aiButton);
        saveGame = findViewById(R.id.savegame);
        editText = findViewById(R.id.edittext);
        submit = findViewById(R.id.submit);

        editText.setVisibility(View.INVISIBLE);
        submit.setVisibility(View.INVISIBLE);

        Context thisContext = this;
        CustomAdapter customAdapter = new CustomAdapter(squares, pieces,this);
        playersTurn.setText("White's Turn");

        gridView.setAdapter(customAdapter);


        // When the undo button is clicked undo the last user's move
        undoBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if (!checkMate[0] && !undoClicked[0]) {
                    undoClicked[0] = true;
                    if (whitesMove[0]) {
                        whitesMove[0] = false;

                        playersTurn.setText("Black's Turn");

                    } else {
                        whitesMove[0] = true;

                        playersTurn.setText("White's Turn");
                    }

                    moves.remove(moves.size()-1);

                    Piece[][] previousPieces = Board.deepClone(previous[0].board);
                    current[0].board = previousPieces;

                    int index;
                    for(int k = 0; k < current[0].board.length; k++) {
                        for(int j = 0; j < current[0].board[0].length; j++) {
                            index = k*8+j;

                            if (current[0].board[k][j] == null && pieces[index] != 0) {
                                pieces[index] = 0;
                                whiteSquares.remove(new Integer(index));
                                blackSquares.remove(new Integer(index));
                            }

                            if (current[0].board[k][j] instanceof King && current[0].board[k][j].getColor() == 'b') {
                                pieces[index] = R.drawable.blackking;
                                blackSquares.add(index);
                            } else if (current[0].board[k][j] instanceof King && current[0].board[k][j].getColor() == 'w') {
                                pieces[index] = R.drawable.whiteking;
                                whiteSquares.add(index);
                            } else if (current[0].board[k][j] instanceof Queen && current[0].board[k][j].getColor() == 'b') {
                                pieces[index] = R.drawable.blackqueen;
                                blackSquares.add(index);
                            } else if (current[0].board[k][j] instanceof Queen && current[0].board[k][j].getColor() == 'w') {
                                pieces[index] = R.drawable.whitequeen;
                                whiteSquares.add(index);
                            } else if (current[0].board[k][j] instanceof Rook && current[0].board[k][j].getColor() == 'w') {
                                pieces[index] = R.drawable.whiterook;
                                whiteSquares.add(index);
                            } else if (current[0].board[k][j] instanceof Rook && current[0].board[k][j].getColor() == 'b') {
                                pieces[index] = R.drawable.blackrook;
                                blackSquares.add(index);
                            } else if (current[0].board[k][j] instanceof Knight && current[0].board[k][j].getColor() == 'w') {
                                pieces[index] = R.drawable.whiteknight;
                                whiteSquares.add(index);
                            } else if (current[0].board[k][j] instanceof Knight && current[0].board[k][j].getColor() == 'b') {
                                pieces[index] = R.drawable.blackknight;
                                blackSquares.add(index);
                            } else if (current[0].board[k][j] instanceof Bishop && current[0].board[k][j].getColor() == 'w') {
                                pieces[index] = R.drawable.whitebishop;
                                whiteSquares.add(index);
                            } else if (current[0].board[k][j] instanceof Bishop && current[0].board[k][j].getColor() == 'b') {
                                pieces[index] = R.drawable.blackbishop;
                                blackSquares.add(index);
                            } else if (current[0].board[k][j] instanceof Pawn && current[0].board[k][j].getColor() == 'w') {
                                pieces[index] = R.drawable.whitepawn;
                                whiteSquares.add(index);
                            } else if (current[0].board[k][j] instanceof Pawn && current[0].board[k][j].getColor() == 'b') {
                                pieces[index] = R.drawable.blackpawn;
                                blackSquares.add(index);
                            }
                        }
                    }

                    CustomAdapter customAdapter1 = new CustomAdapter(squares, pieces, thisContext);
                    gridView.setAdapter(customAdapter1);
                }
            }
        });

        // When the draw button is clicked
        drawBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if (checkMate[0] == false) {
                    playersTurn.setText("Draw");
                    saveGame.setText("To save this game enter a name and press submit:");
                    editText.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    checkMate[0] = true;
                    moves.add("draw");
                }
            }
        });

        // When the resign button is clicked
        resignBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if (checkMate[0] == false) {
                    if (whitesMove[0]) {
                        playersTurn.setText("White Resigns: Black Wins");
                    } else {
                        playersTurn.setText("Black Resigns: White Wins");
                    }
                    saveGame.setText("To save this game enter a name and press submit:");
                    editText.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    checkMate[0] = true;
                    moves.add("resign");
                }
            }
        });

        // When the ai button is clicked
        aiBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!checkMate[0]) {

                    Random rd = new Random(); // creating Random object
                    boolean moved = false;
                    int row = 0;
                    int col = 0;
                    String userMove = null;
                    int firstIDX = 0;
                    int firstSpot = 0;


                    // When a move hasn't been made
                    while (!moved) {
                        if (Board.inCheck('w', current[0].board) && whitesMove[0]) {
                            userMove = Board.getValidMove('w', current[0].board);

                            Piece[][] previousPieces = Board.deepClone(current[0].board);
                            previous[0].board = previousPieces;

                            String first = new StringBuilder().append(userMove.charAt(0)).append(userMove.charAt(1)).toString();
                            row = Board.rankFileToIndex(first)[0];
                            col = Board.rankFileToIndex(first)[1];

                        } else if (Board.inCheck('b', current[0].board) && !whitesMove[0]) {
                            userMove = Board.getValidMove('b', current[0].board);

                            Piece[][] previousPieces = Board.deepClone(current[0].board);
                            previous[0].board = previousPieces;

                            String first = new StringBuilder().append(userMove.charAt(0)).append(userMove.charAt(1)).toString();
                            row = Board.rankFileToIndex(first)[0];
                            col = Board.rankFileToIndex(first)[1];

                        } else {
                            if (whitesMove[0]) {
                                firstIDX = rd.nextInt(whiteSquares.size());
                                firstSpot = whiteSquares.get(firstIDX);
                            } else {
                                firstIDX = rd.nextInt(blackSquares.size());
                                firstSpot = blackSquares.get(firstIDX);
                            }
                            row = firstSpot / 8;
                            col = firstSpot % 8;

                            int secondSpot = rd.nextInt(63);

                            int rowToMove = secondSpot / 8;
                            int colToMove = secondSpot % 8;

                            userMove = current[0].indexToRankFile(row, col) + " " + current[0].indexToRankFile(rowToMove, colToMove);
                        }

                        // moving
                        if (current[0].board[row][col] != null && whitesMove[0] && current[0].board[row][col].getColor() == 'w'
                                && current[0].board[row][col].validateCommand(current[0].board, userMove)) {
                            //check to see if this would cause check
                            Piece[][] checkCheckClone = Board.deepClone(current[0].board);
                            checkCheckClone[row][col].move(checkCheckClone, userMove);
                            if (Board.inCheck('w', checkCheckClone)) {
                                //cant do this, would put king in check
                                cont[0] = true;
                            }

                            // Move pieces
                            if (!cont[0]) {
                                undoClicked[0] = false;

                                Piece[][] previousPieces = Board.deepClone(current[0].board);
                                previous[0].board = previousPieces;

                                current[0].board[row][col].move(current[0].board, userMove);
                                moved = true;

                                //see if this move caused a check
                                if (Board.inCheck('b', current[0].board)) {
                                    if (Board.inCheckmate('b', current[0].board)) {
                                        checkMate[0] = true;
                                    }
                                    playersTurn.setText("Check: Black's Turn");
                                } else {
                                    playersTurn.setText("Black's Turn");
                                }
                                whitesMove[0] = false;
                            }

                        } else if (current[0].board[row][col] != null && !whitesMove[0] && current[0].board[row][col].getColor() == 'b'
                                && current[0].board[row][col].validateCommand(current[0].board, userMove)) {
                            Piece[][] checkCheckClone = Board.deepClone(current[0].board);
                            checkCheckClone[row][col].move(checkCheckClone, userMove);
                            if (Board.inCheck('b', checkCheckClone)) {
                                //cant do this, would put king in check
                                cont[0] = true;
                            }

                            // Move pieces
                            if (!cont[0]) {
                                undoClicked[0] = false;

                                Piece[][] previousPieces = Board.deepClone(current[0].board);
                                previous[0].board = previousPieces;

                                current[0].board[row][col].move(current[0].board, userMove);
                                moved = true;

                                if (Board.inCheck('w', current[0].board)) {
                                    if (Board.inCheckmate('w', current[0].board)) {
                                        checkMate[0] = true;
                                    }
                                    playersTurn.setText("Check: White's Turn");
                                } else {
                                    playersTurn.setText("White's Turn");
                                }
                                whitesMove[0] = true;
                            }

                        }

                        if (checkMate[0]) {
                            if (whitesMove[0]) {
                                playersTurn.setText("Checkmate: Black Wins");
                            } else {
                                playersTurn.setText("Checkmate: White Wins");
                            }

                            saveGame.setText("To save this game enter a name and press submit:");
                            editText.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                        }

                        int index;
                        for (int k = 0; k < current[0].board.length; k++) {
                            for (int j = 0; j < current[0].board[0].length; j++) {
                                index = k * 8 + j;

                                if (current[0].board[k][j] == null && pieces[index] != 0) {
                                    pieces[index] = 0;
                                    whiteSquares.remove(new Integer(index));
                                    blackSquares.remove(new Integer(index));
                                }

                                if (current[0].board[k][j] instanceof King && current[0].board[k][j].getColor() == 'b') {
                                    pieces[index] = R.drawable.blackking;
                                    blackSquares.add(index);
                                } else if (current[0].board[k][j] instanceof King && current[0].board[k][j].getColor() == 'w') {
                                    pieces[index] = R.drawable.whiteking;
                                    whiteSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Queen && current[0].board[k][j].getColor() == 'b') {
                                    pieces[index] = R.drawable.blackqueen;
                                    blackSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Queen && current[0].board[k][j].getColor() == 'w') {
                                    pieces[index] = R.drawable.whitequeen;
                                    whiteSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Rook && current[0].board[k][j].getColor() == 'w') {
                                    pieces[index] = R.drawable.whiterook;
                                    whiteSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Rook && current[0].board[k][j].getColor() == 'b') {
                                    pieces[index] = R.drawable.blackrook;
                                    blackSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Knight && current[0].board[k][j].getColor() == 'w') {
                                    pieces[index] = R.drawable.whiteknight;
                                    whiteSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Knight && current[0].board[k][j].getColor() == 'b') {
                                    pieces[index] = R.drawable.blackknight;
                                    blackSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Bishop && current[0].board[k][j].getColor() == 'w') {
                                    pieces[index] = R.drawable.whitebishop;
                                    whiteSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Bishop && current[0].board[k][j].getColor() == 'b') {
                                    pieces[index] = R.drawable.blackbishop;
                                    blackSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Pawn && current[0].board[k][j].getColor() == 'w') {
                                    pieces[index] = R.drawable.whitepawn;
                                    whiteSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Pawn && current[0].board[k][j].getColor() == 'b') {
                                    pieces[index] = R.drawable.blackpawn;
                                    blackSquares.add(index);
                                }
                            }
                        }

                        CustomAdapter customAdapter1 = new CustomAdapter(squares, pieces, thisContext);
                        gridView.setAdapter(customAdapter1);

                        firstSelected = -1;
                        secondSelected = -1;
                    }
                    moves.add(userMove);
                }
            }
        });

        // When the submit button is clicked
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                File dir = getFilesDir();
                String[] paths = dir.list();

                String title = editText.getText().toString();

                Calendar date = Calendar.getInstance();
                String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
                String month = String.valueOf(date.get(Calendar.MONTH)+1); //stupid
                String year = String.valueOf(date.get(Calendar.YEAR));

                //set filename
                String fileName = editText.getText().toString() + " | " + month + "-" + day + "-" + year;


                //get name of replay and check if its already in use
                for(String path : paths){
                    //System.out.println(path);
                    String[] segments = path.split(" | ");
                    String fileTitle = segments[0];
                    if(fileTitle.equals(title)){
                        saveGame.setText("Already in use, please try another name.");
                        return;
                    }
                }

                //create and write to file
                try{
                    FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                    for(String move : moves){
                        fos.write(move.getBytes());
                        fos.write("\n".getBytes());
                    }
                    fos.close();
                } catch(IOException e){
                    e.printStackTrace();
                }

                saveGame.setText("Saved!  You may now exit.");

                //System.out.println(new File(fileName).exists());
            }
        });

        // When the board is clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (checkMate[0] == false){

                    int selectedSquare = squares[i];
                    int selectedPiece = pieces[i];

                    if (firstSelected == -1){
                        firstSelected = i;
                        if ((whitesMove[0] && !whitePieces.contains(selectedPiece)) || (!whitesMove[0] && !blackPieces.contains(selectedPiece))){
                            firstSelected = -1;
                        }
                        return;
                    }

                    if (secondSelected == -1) {
                        cont[0] = false;
                        secondSelected = i;

                        // Check if move is valid and move pieces
                        int row = firstSelected / 8;
                        int col = firstSelected % 8;

                        int rowToMove = secondSelected / 8;
                        int colToMove = secondSelected % 8;

                        String userMove = current[0].indexToRankFile(row, col) + " " + current[0].indexToRankFile(rowToMove, colToMove);
                        moves.add(userMove);

                        if (current[0].board[row][col] != null && whitesMove[0] && current[0].board[row][col].getColor() == 'w'
                                && current[0].board[row][col].validateCommand(current[0].board, userMove)) {
                            //check to see if this would cause check
                            Piece[][] checkCheckClone = Board.deepClone(current[0].board);
                            checkCheckClone[row][col].move(checkCheckClone, userMove);
                            if (Board.inCheck('w', checkCheckClone)) {
                                //cant do this, would put king in check
                                cont[0] = true;
                            }

                            // Move pieces
                            if (!cont[0]) {
                                undoClicked[0] = false;

                                Piece[][] previousPieces = Board.deepClone(current[0].board);
                                previous[0].board = previousPieces;

                                current[0].board[row][col].move(current[0].board, userMove);

                                //see if this move caused a check
                                if (Board.inCheck('b', current[0].board)) {
                                    if (Board.inCheckmate('b', current[0].board)) {
                                        checkMate[0] = true;
                                    }
                                    playersTurn.setText("Check: Black's Turn");
                                } else {
                                    playersTurn.setText("Black's Turn");
                                }
                                whitesMove[0] = false;
                            }

                        } else if (current[0].board[row][col] != null && !whitesMove[0] && current[0].board[row][col].getColor() == 'b'
                                && current[0].board[row][col].validateCommand(current[0].board, userMove)) {
                            Piece[][] checkCheckClone = Board.deepClone(current[0].board);
                            checkCheckClone[row][col].move(checkCheckClone, userMove);
                            if (Board.inCheck('b', checkCheckClone)) {
                                //cant do this, would put king in check
                                cont[0] = true;
                            }

                            // Move pieces
                            if (!cont[0]) {
                                undoClicked[0] = false;

                                Piece[][] previousPieces = Board.deepClone(current[0].board);
                                previous[0].board = previousPieces;

                                current[0].board[row][col].move(current[0].board, userMove);

                                if (Board.inCheck('w', current[0].board)) {
                                    if (Board.inCheckmate('w', current[0].board)) {
                                        checkMate[0] = true;
                                    }
                                    playersTurn.setText("Check: White's Turn");
                                } else {
                                    playersTurn.setText("White's Turn");
                                }
                                whitesMove[0] = true;
                            }

                        }

                        if (checkMate[0]){
                            if (whitesMove[0]) {
                                playersTurn.setText("Checkmate: Black Wins");
                            } else {
                                playersTurn.setText("Checkmate: White Wins");
                            }

                            saveGame.setText("To save this game enter a name and press submit:");
                            editText.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                        }



                        int index;
                        for(int k = 0; k < current[0].board.length; k++) {
                            for(int j = 0; j < current[0].board[0].length; j++) {
                                index = k*8+j;

                                if (current[0].board[k][j] == null && pieces[index] != 0) {
                                    pieces[index] = 0;
                                    whiteSquares.remove(new Integer(index));
                                    blackSquares.remove(new Integer(index));
                                }

                                if (current[0].board[k][j] instanceof King && current[0].board[k][j].getColor() == 'b') {
                                    pieces[index] = R.drawable.blackking;
                                    blackSquares.add(index);
                                } else if (current[0].board[k][j] instanceof King && current[0].board[k][j].getColor() == 'w') {
                                    pieces[index] = R.drawable.whiteking;
                                    whiteSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Queen && current[0].board[k][j].getColor() == 'b') {
                                    pieces[index] = R.drawable.blackqueen;
                                    blackSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Queen && current[0].board[k][j].getColor() == 'w') {
                                    pieces[index] = R.drawable.whitequeen;
                                    whiteSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Rook && current[0].board[k][j].getColor() == 'w') {
                                    pieces[index] = R.drawable.whiterook;
                                    whiteSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Rook && current[0].board[k][j].getColor() == 'b') {
                                    pieces[index] = R.drawable.blackrook;
                                    blackSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Knight && current[0].board[k][j].getColor() == 'w') {
                                    pieces[index] = R.drawable.whiteknight;
                                    whiteSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Knight && current[0].board[k][j].getColor() == 'b') {
                                    pieces[index] = R.drawable.blackknight;
                                    blackSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Bishop && current[0].board[k][j].getColor() == 'w') {
                                    pieces[index] = R.drawable.whitebishop;
                                    whiteSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Bishop && current[0].board[k][j].getColor() == 'b') {
                                    pieces[index] = R.drawable.blackbishop;
                                    blackSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Pawn && current[0].board[k][j].getColor() == 'w') {
                                    pieces[index] = R.drawable.whitepawn;
                                    whiteSquares.add(index);
                                } else if (current[0].board[k][j] instanceof Pawn && current[0].board[k][j].getColor() == 'b') {
                                    pieces[index] = R.drawable.blackpawn;
                                    blackSquares.add(index);
                                }
                            }
                        }

                        CustomAdapter customAdapter1 = new CustomAdapter(squares, pieces, thisContext);
                        gridView.setAdapter(customAdapter1);

                        firstSelected = -1;
                        secondSelected = -1;

                    }

                    return;
                }
            }
        });
    }

    public class CustomAdapter extends BaseAdapter{
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