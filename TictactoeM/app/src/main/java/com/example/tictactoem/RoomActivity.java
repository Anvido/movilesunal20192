package com.example.tictactoem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {

    private String currentPlayer;
    private List<Room> roomsList = new ArrayList<Room>();
    ArrayAdapter<Room> roomAdapter;

    EditText name;
    Button create;
    ListView rooms;

    FirebaseDatabase fbDatabase;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        currentPlayer = getIntent().getStringExtra("PLAYER");

        name = findViewById(R.id.roomName);
        create = findViewById(R.id.createRoom);
        rooms = findViewById(R.id.rooms);

        initFirebase();
        listRooms();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roomName =  name.getText().toString();

                if (roomName.equals("")){
                    Toast.makeText(RoomActivity.this, "Ingresa un nombre de sala por favor", Toast.LENGTH_LONG).show();
                    name.setError("Required");
                } else{
                    DatabaseReference newRom = dbReference.child("Rooms").push();
                    String id = newRom.getKey();
                    newRom.setValue(new Room(id, roomName, currentPlayer));
                    name.setText("");
                }

            }
        });

        rooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Room r = (Room) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(RoomActivity.this, TicTacToeActivity.class);
                if (r.player1.equalsIgnoreCase(currentPlayer) || r.player2.equalsIgnoreCase("") || r.player2.equalsIgnoreCase(currentPlayer)) {

                    intent.putExtra("PLAYER_2", "No");

                    if (!r.player1.equalsIgnoreCase(currentPlayer) && r.player2.equalsIgnoreCase("")){
                        r.setPlayer2(currentPlayer);
                        dbReference.child("Rooms").child(r.getId()).setValue(r);
                        intent.putExtra("PLAYER_2", "Yes");
                    }

                    intent.putExtra("ROOM_ID", r.getId());
                    intent.putExtra("CURR_PLA", currentPlayer);
                    intent.putExtra("TURN", r.getTurn());
                    startActivity(intent);
                } else {
                    Toast.makeText(RoomActivity.this, "Sala llena", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void listRooms() {
        dbReference.child("Rooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomsList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Room r  = ds.getValue(Room.class);
                    roomsList.add(r);

                    roomAdapter = new ArrayAdapter<Room>(RoomActivity.this, android.R.layout.simple_list_item_1, roomsList);
                    rooms.setAdapter(roomAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        fbDatabase = FirebaseDatabase.getInstance();
        dbReference = fbDatabase.getReference();
    }
}
