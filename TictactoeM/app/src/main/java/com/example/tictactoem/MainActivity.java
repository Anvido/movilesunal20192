package com.example.tictactoem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button multiplayer, addNickname;
    EditText nickname;
    public String player1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNickname = findViewById(R.id.addNickname);
        nickname = findViewById(R.id.nickname);

        addNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player1 = nickname.getText().toString();
                if (player1.equalsIgnoreCase("")){
                    nickname.setError("Required");
                    Toast.makeText(MainActivity.this, "Ingresa un nombre de jugador por favor", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Nombre cambiado", Toast.LENGTH_LONG).show();
                }
            }
        });

        multiplayer = (Button) findViewById(R.id.multiplayer);
        multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player1 = nickname.getText().toString();
                if (player1.equalsIgnoreCase("")){
                    nickname.setError("Required");
                    Toast.makeText(MainActivity.this, "Ingresa un nombre de jugador por favor", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, RoomActivity.class);
                intent.putExtra("PLAYER", player1);
                startActivity(intent);
            }
        });
    }
}
