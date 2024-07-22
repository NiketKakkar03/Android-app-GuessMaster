package com.example.guessmaster;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;

import java.util.Random;

import android.os.Bundle;
import android.view.View;
import android.widget.* ;


public class GuessMaster extends AppCompatActivity {
    private int numOfEntities;
    private Entity[] entities;
    private int[] tickets;
    private int numOfTickets;
    String entName; //Stores Entity Name
    int entityId;
    int currentTicketWon = 0;

    private TextView entityName;
    private TextView ticketsum;
    private Button guessButton;
    private EditText userIn;
    private Button btnclearContent;
    private String user_input;
    private ImageView entityImage;
    String answer;

    Entity myEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_master);

        guessButton = (Button) findViewById(R.id.btnGuess);
        btnclearContent = (Button) findViewById(R.id.btnClear);
        userIn = (EditText) findViewById(R.id.guessinput);
        ticketsum = (TextView) findViewById(R.id.ticket);
        entityName = (TextView) findViewById(R.id.entityName);
        entityImage = (ImageView) findViewById(R.id.entityImage);

        addEntity(new Politician("Justin Trudeau", new Date("December", 25, 1971), "Male", "Liberal", 0.25));
        addEntity(new Singer("Celine Dion", new Date("March", 30, 1961), "Female", "La voix du bon Dieu", new Date("November", 6, 1981), 0.5));
        addEntity(new Person("My Creator", new Date("September", 1, 2000), "Female", 1));
        addEntity(new Country("United States", new Date("July", 4, 1776), "Washinton D.C.", 0.1));

        changeEntity();
        welcomeToGame(myEntity);


        btnclearContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeEntity();
                    }
                }
        );

        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGame(entityId);
            }
        }
        );


    }

    private void changeEntity() {
        // Clear user entries from the userIn EditText
        userIn.setText("");

        // Randomly choose another entity
        Random random = new Random();
        entityId = random.nextInt(numOfEntities); // Assuming numOfEntities is the total number of entities

        // Update displayed entity name and image
        myEntity = entities[entityId];
        ImageSetter(myEntity);
        entityName.setText(myEntity.getName());
        Toast.makeText(this, "New Entity, Guess again!", Toast.LENGTH_SHORT).show();
    }

    private void ImageSetter(Entity entity) {
        switch (entity.getName()) {
            case "United States":
                entityImage.setImageResource(R.drawable.usaflag);
                break;
            case "Justin Trudeau":
                entityImage.setImageResource(R.drawable.justint);
                break;
            case "Celine Dion":
                entityImage.setImageResource(R.drawable.celidion);
                break;
            case "My Creator":
                entityImage.setImageResource(R.drawable.mylovelyprofessor);
                break;
        }
    }


    private void welcomeToGame(Entity entity) {
        // Build the AlertDialog
        AlertDialog.Builder welcomeAlert = new AlertDialog.Builder(this);
        welcomeAlert.setTitle("GuessMaster Game v3"); // Set title
        welcomeAlert.setMessage(entity.welcomeMessage()); // Set message
        welcomeAlert.setCancelable(false);

        // Set a negative button ("START GAME") with a click listener
        welcomeAlert.setNegativeButton("START GAME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Game is Starting... Enjoy", Toast.LENGTH_SHORT).show();
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = welcomeAlert.create();
        dialog.show();
    }

    private void displayAlertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GuessMaster.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }


    public GuessMaster() {
        numOfEntities = 0;
        entities = new Entity[10];
    }

    public void addEntity(Entity entity) {
//		entities[numOfEntities++] = new Entity(entity);
//		entities[numOfEntities++] = entity;//////
        entities[numOfEntities++] = entity.clone();
    }

    public void playGame(int entityId) {
        Entity entity = entities[entityId];
        playGame(entity);
    }

    public void playGame(Entity entity) {
        System.out.println("***************************");////
        System.out.printf(entity.welcomeMessage());////
        if (entity.getName().equals("Myself"))
            System.out.printf("\n\nGuess %s's birthday\n", entity.getName());
        else
            System.out.printf("\n\nGuess my birthday\n", entity.getName());
        System.out.println("(mm/dd/yyyy)");

            answer = userIn.getText().toString();
            answer = answer.replace("\n", "").replace("\r", "");

            if (answer.equals("quit")) {
                System.exit(0);
            }

            Date date = new Date(answer);
//			System.out.println("you guess is: " + date);

        if (date.precedes(entity.getBorn())) {
            // Incorrect guess, display AlertDialog
            displayAlertDialog("Incorrect", "Try a later date");
        } else if (entity.getBorn().precedes(date)) {
            // Incorrect guess, display AlertDialog
            displayAlertDialog("Incorrect", "Try an earlier date");
        } else {

            // Display AlertDialog for winning
            AlertDialog.Builder winAlert = new AlertDialog.Builder(this);
            winAlert.setTitle("You won");
            winAlert.setMessage("BINGO! " + entity.closingMessage() + "\n\nAwarded tickets: " + entity.getAwardedTicketNumber());
            winAlert.setCancelable(false);
            winAlert.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "Awarded tickets: " + entity.getAwardedTicketNumber(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = winAlert.create();
            dialog.show();

            // Update current tickets won
            currentTicketWon += entity.getAwardedTicketNumber();
            ticketsum.setText(String.format("Tickets: %d", currentTicketWon));
            changeEntity();
        }
    }
}

