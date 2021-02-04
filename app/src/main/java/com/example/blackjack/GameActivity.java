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


        System.out.println(deck);
        StartGame();
    }


    void StartGame(){

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
        playersTotal = CalculateHand(playerInventory);
        cpuTotal = CalculateHand(cpuInventory);
       // String playString = String.valueOf(playersTotal);
     //   String cpuString = String.valueOf(cpuTotal);
       // ((TextView)findViewById(R.id.PlayerAmountView)).setText(playString);
     //   ((TextView)findViewById(R.id.CpuAmountView)).setText(cpuString);
        PlayerTurnOver();
        UpdateField(playerInventory,cardPlayerSlots);
        UpdateField(cpuInventory,cardCpuSlots);
        System.out.println(playerInventory);
        System.out.println(cpuInventory);
        //System.out.println(deck);
        isPlayersTurn = true;
        isCPUTurn = false;
    }



    public void DrawButton(View buttonView){
        if (isPlayersTurn && !isCPUTurn){
            DrawCard(playerInventory);
            ((Button)findViewById(R.id.DrawButton)).setEnabled(false);
            ((Button)findViewById(R.id.StickButton)).setEnabled(false);
            playersTotal = CalculateHand(playerInventory);
           // String playString = String.valueOf(playersTotal);
           // String cpuString = String.valueOf(cpuTotal);
          //  ((TextView)findViewById(R.id.PlayerAmountView)).setText(playString);
         //   ((TextView)findViewById(R.id.CpuAmountView)).setText(cpuString);
            PlayerTurnOver();
            UpdateField(playerInventory,cardPlayerSlots);
            System.out.println(playerInventory);
            System.out.println(playersTotal);
            System.out.println(deck);
            //Check if players cards exceed 21 or equal 21.
        }
    }

    public void StickButton(View buttonView){
        if (isPlayersTurn && !isCPUTurn){
            isPlayersTurn = false;
            ((Button)findViewById(R.id.DrawButton)).setEnabled(false);
            ((Button)findViewById(R.id.StickButton)).setEnabled(false);
            isCPUTurn = true;
            StartCPUTurn();
            startCPUTurn = true;
        }
    }

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

    int CalculateHand(ArrayList<String> inv){
        int total = 0;
        ArrayList<String> others = new ArrayList<>();
        ArrayList<String> aces = new ArrayList<String>();

        for(String card : inv) {
            if (card.contains("A")){
                aces.add(card);
            }
            else{
                others.add(card);
            }

        }


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

    void DisplayText(String message){
        ((TextView)findViewById(R.id.OutcomeText)).setText(message);
        ((Button)findViewById(R.id.DrawButton)).setEnabled(false);
        ((Button)findViewById(R.id.StickButton)).setEnabled(false);
    }


    public void StartCPUTurn(){

        cpuTotal = CalculateHand(cpuInventory);
        UpdateField(cpuInventory,cardCpuSlots);
    //    String playString = String.valueOf(playersTotal);
     //   String cpuString = String.valueOf(cpuTotal);
     //   ((TextView)findViewById(R.id.PlayerAmountView)).setText(playString);
      //  ((TextView)findViewById(R.id.CpuAmountView)).setText(cpuString);

        while(cpuTotal < 17){
            DrawCard(cpuInventory);
            cpuTotal = CalculateHand(cpuInventory);
         //   playString = String.valueOf(playersTotal);
         //   cpuString = String.valueOf(cpuTotal);
        //    ((TextView)findViewById(R.id.PlayerAmountView)).setText(playString);
        //    ((TextView)findViewById(R.id.CpuAmountView)).setText(cpuString);
            UpdateField(cpuInventory,cardCpuSlots);
            System.out.println(cpuInventory);
            if (cpuTotal > 21){
                break;
            }
        }

        CompareHands();
        System.out.println(cpuInventory);

    }

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
        Random rand = new Random();
        int number;
        String card;
        number = rand.nextInt(deck.size());
        card = deck.get(number);
        deck.remove(number);
        inv.add(card);
    }
    /*
    void ACalculateComputerHand(){
        cpuTotal = 0;
        for(String card : cpuInventory) {

            if (card.contains("1") && !card.contains("0")) {
                cpuTotal += 1;
            }
            else if (card.contains("2")) {
                cpuTotal += 2;
            }
            else if (card.contains("3")) {
                cpuTotal += 3;
            }
            else if (card.contains("4")) {
                cpuTotal += 4;
            }
            else if (card.contains("5")) {
                cpuTotal += 5;
            }
            else if (card.contains("6")) {
                cpuTotal += 6;
            }
            else if (card.contains("7")) {
                cpuTotal += 7;
            }
            else if (card.contains("8")) {
                cpuTotal += 8;
            }
            else if (card.contains("9")) {
                cpuTotal += 9;
            }
            else if (card.contains("J") || card.contains("Q") || card.contains("K") || card.contains("10")) {
                cpuTotal += 10;
            }
            else if (card.contains("A")) {
                if (cpuTotal + 11 > 21) {
                    cpuTotal += 1;
                } else if (cpuTotal + 11 < 21) {
                    cpuTotal += 11;
                }
            }
        }
        if(startCPUTurn){
            startCPUTurn = false;
            StartCPUTurn();
        }
        else if(cpuTotal > 21){
            Win("You");
        }
        else{
            StartCPUTurn();
        }

        System.out.println( "Cpu amount"+ cpuTotal);

    }
    */


    void ClearField(ArrayList<ImageView> cardSlots){
        for (ImageView slot : cardSlots)
        {
            slot.setImageResource(android.R.color.transparent);
        }
    }
    void UpdateField(ArrayList<String> inv, ArrayList<ImageView> cardSlots){

        if (inv == playerInventory){
            for(int i = 0; i < 5; i++){
                if (i >= inv.size()){
                    break;
                }
                cardSlots.get(i).setImageResource(getResources().getIdentifier(inv.get(i).toLowerCase(),"drawable",getPackageName()));
            }
        }

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
