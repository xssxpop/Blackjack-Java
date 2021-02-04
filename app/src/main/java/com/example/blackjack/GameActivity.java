package com.example.blackjack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameActivity extends AppCompatActivity {


    public ArrayList<String> deck = new ArrayList<String>();
    public ArrayList<String> playerInventory = new ArrayList<String>();
    public ArrayList<String> cpuInventory = new ArrayList<String>();
    public ArrayList<ImageView> cardPlayerSlots = new ArrayList<>();
    public ArrayList<ImageView> cardCpuSlots = new ArrayList<>();
    public int playersTotal;
    public int cpuTotal;
    public Boolean startCPUTurn;
    public Boolean isPlayersTurn = false;
    public Boolean isCPUTurn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        //Get imageViews to edit cards when drawn.
        cardPlayerSlots.add((ImageView) findViewById(R.id.cardViewSlot1));
        cardPlayerSlots.add((ImageView) findViewById(R.id.cardViewSlot2));
        cardPlayerSlots.add((ImageView) findViewById(R.id.cardViewSlot3));
        cardPlayerSlots.add((ImageView) findViewById(R.id.cardViewSlot4));
        cardPlayerSlots.add((ImageView) findViewById(R.id.cardViewSlot5));
        cardCpuSlots.add((ImageView) findViewById(R.id.cardViewSlot6));
        cardCpuSlots.add((ImageView) findViewById(R.id.cardViewSlot7));
        cardCpuSlots.add((ImageView) findViewById(R.id.cardViewSlot8));
        cardCpuSlots.add((ImageView) findViewById(R.id.cardViewSlot9));
        cardCpuSlots.add((ImageView) findViewById(R.id.cardViewSlot10));

        StartGame();
    }


    void StartGame(){


        //Gets card names from text file and adds them to a arrayList of type string.
        try {
            DataInputStream textFileStream = new DataInputStream(getAssets().open(String.format("Cards.txt")));
            Scanner sc = new Scanner(textFileStream);
            while (sc.hasNextLine()) {
                deck.add(sc.nextLine());
            }
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Add 2 random cards to Player and Computers Inv.
        DrawCard(playerInventory);
        DrawCard(playerInventory);
        DrawCard(cpuInventory);
        DrawCard(cpuInventory);

        //Calculate both players hands.
        playersTotal = CalculateHand(playerInventory);
        cpuTotal = CalculateHand(cpuInventory);

        //Check if player has won already.
        PlayerTurnOver();

        //Update both player and cpu field.
        UpdateField(playerInventory,cardPlayerSlots);
        UpdateField(cpuInventory,cardCpuSlots);

        //Set turn booleans.
        isPlayersTurn = true;
        isCPUTurn = false;
    }



    public void DrawButton(View buttonView){

        //Checks if its the players turn and if so initiates a draw.
        if (isPlayersTurn && !isCPUTurn){

            //Draws Card
            DrawCard(playerInventory);

            //Disables buttons.
            ((Button)findViewById(R.id.DrawButton)).setEnabled(false);
            ((Button)findViewById(R.id.StickButton)).setEnabled(false);

            //Calculates players hand.
            playersTotal = CalculateHand(playerInventory);

            //Checks if player has won or is bust.
            PlayerTurnOver();

            //Updates players field.
            UpdateField(playerInventory,cardPlayerSlots);

        }
    }



    public void StickButton(View buttonView){

        //Checks if its players turn and if they press button it will initiate the cpu turn.
        if (isPlayersTurn && !isCPUTurn){
            isPlayersTurn = false;
            ((Button)findViewById(R.id.DrawButton)).setEnabled(false);
            ((Button)findViewById(R.id.StickButton)).setEnabled(false);
            isCPUTurn = true;
            StartCPUTurn();
            startCPUTurn = true;
        }
    }


    //Checks if players hand equals, is less than or exceeds 21.
    void PlayerTurnOver(){
        if (playersTotal > 21){
            DisplayText("You Went Bust!");
        }
        if (playersTotal < 21){
            ((Button)findViewById(R.id.DrawButton)).setEnabled(true);
            ((Button)findViewById(R.id.StickButton)).setEnabled(true);
        }
        if(playersTotal < 21 && playerInventory.size() == 5){
            DisplayText("You Win!");
        }
        if (playersTotal == 21){
            DisplayText("You Win!");
        }
    }


    // Resets all values, cleans the field and restarts the game.
    public void RestartButton(View v){
        playersTotal = 0;
        cpuTotal = 0;
        isPlayersTurn = true;
        isCPUTurn = false;
        playerInventory.clear();
        cpuInventory.clear();
        ClearField(cardPlayerSlots);
        ClearField(cardCpuSlots);
        ((TextView)findViewById(R.id.OutcomeText)).setText(null);
        StartGame();
    }


    // Calculates the hand of the inventory passed in.
    int CalculateHand(ArrayList<String> inv){

        int total = 0;

        //Creating arraylists for splitting aces and other cards.
        ArrayList<String> others = new ArrayList<>();
        ArrayList<String> aces = new ArrayList<String>();


        //Foreach loop that adds all aces in inventory to the ace list.
        //Adds all other cards to the other list.
        for(String card : inv) {
            if (card.contains("A")){
                aces.add(card);
            }
            else{
                others.add(card);
            }

        }


        //Foreach loop that calculates the total depending on what the name of the card is in the list.
        for(String card : others) {

            if (card.contains("J") || card.contains("Q") || card.contains("K") || card.contains("10")) {
                total += 10;
            }
            else{
                String intValue = card.replaceAll("[^0-9]", "");
                int number = 0;
                number = Integer.parseInt(intValue);
                total += number;
                }
            }


        //Foreach loop that depending on the total up to this point decides if the aces should be
        //counted as 11 or 1.
        for(String card : aces) {
            if (total + 11 > 21){
                total += 1;
            }
            else if(total + 11 <= 21){
                total += 11;
            }
        }
        return total;
    }



    // Changes the outcome textView depending on who won.
    void DisplayText(String message){
        ((TextView)findViewById(R.id.OutcomeText)).setText(message);
        ((Button)findViewById(R.id.DrawButton)).setEnabled(false);
        ((Button)findViewById(R.id.StickButton)).setEnabled(false);
    }


    public void StartCPUTurn(){


        //Calculates and updates the cpu's hand and field.
        cpuTotal = CalculateHand(cpuInventory);
        UpdateField(cpuInventory,cardCpuSlots);


        //While the cpu has less than 17 it will draw a card and update their field.
        while(cpuTotal < 17){
            DrawCard(cpuInventory);
            cpuTotal = CalculateHand(cpuInventory);
            UpdateField(cpuInventory,cardCpuSlots);
            System.out.println(cpuInventory);

            //Once they go above 17 or 21 break.
            if (cpuTotal > 21){
                break;
            }
        }

        //Compare both players hands to find the outcome.
        CompareHands();
        System.out.println(cpuInventory);

    }


    //Compares hands and calculates who was won.
    void CompareHands(){

        if (cpuTotal > 21){
            DisplayText("Cpu Went Bust!");
        }
        else if(cpuTotal < 21 && cpuInventory.size() == 5){
            DisplayText("You Lose!");
        }
        else if (cpuTotal == 21){
            DisplayText("You Lose!");
        }

        else if (playersTotal > cpuTotal){
            DisplayText("You Win!");
        }

        else if(playersTotal == cpuTotal){
            DisplayText("Draw!");
        }
        else if (playersTotal < cpuTotal){
            DisplayText("You Lose!");
        }
    }



    void DrawCard(ArrayList<String> inv){

        //Gets a random number between 0 and the size of the deck and adds it to the inventory
        //passed in and then removes it from the deck.
        Random rand = new Random();
        int number;
        String card;
        number = rand.nextInt(deck.size());
        card = deck.get(number);
        deck.remove(number);
        inv.add(card);
    }


    //Sets all the card imageView of the slots passed in to invisible.
    void ClearField(ArrayList<ImageView> cardSlots){
        for (ImageView slot : cardSlots)
        {
            slot.setImageResource(android.R.color.transparent);
        }
    }


    //Sets all the cards to the respective image in drawable depending on the slot in the inventory
    //passed in.
    void UpdateField(ArrayList<String> inv, ArrayList<ImageView> cardSlots){


        //If player inventory is passed in then loop through and set all the card imageViews to the
        //respective card image in drawable.
        if (inv == playerInventory){
            for(int i = 0; i < 5; i++){
                if (i >= inv.size()){
                    break;
                }
                cardSlots.get(i).setImageResource(getResources().getIdentifier(inv.get(i).toLowerCase(),"drawable",getPackageName()));
            }
        }


        //If cpu inventory is passed in then loop through and set all the card image views to
        //the respective card image in drawable then, set the first won to the card back so its
        //turned over.
        else if(inv == cpuInventory){


            for(int i = 0; i < 5; i++){
                if (i >= inv.size()){
                    break;
                }
                cardSlots.get(i).setImageResource(getResources().getIdentifier(inv.get(i).toLowerCase(),"drawable",getPackageName()));
            }

            if(!isCPUTurn){
                cardSlots.get(0).setImageResource(R.drawable.blue_back);
            }

        }


    }

}
