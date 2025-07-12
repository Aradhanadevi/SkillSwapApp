package anjali.learning.skilshare;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

import anjali.learning.skilshare.Adapter.TutorAdapter;
import anjali.learning.skilshare.model.UserModel;

public class LearnRequestedSkill extends AppCompatActivity {

    RecyclerView recyclerView;
    TutorAdapter adapter;
    ArrayList<UserModel> tutorList;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_requested_skill);

        recyclerView = findViewById(R.id.recyclerTutors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tutorList = new ArrayList<>();

        // Get skill requested from Intent
        String skillRequested = getIntent().getStringExtra("skillrequested");
        if (skillRequested == null || skillRequested.isEmpty()) {
            Toast.makeText(this, "No skill requested passed", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔐 Get current logged-in username (adjust logic as per your project)
        String currentUsername = getIntent().getStringExtra("currentUsername");
        if (currentUsername == null || currentUsername.isEmpty()) {
            Toast.makeText(this, "Username not passed!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialize Adapter with currentUsername
        // after you build tutorList and have currentUsername
        TutorAdapter adapter = new TutorAdapter(this, tutorList, currentUsername);
        recyclerView.setAdapter(adapter);


        // Firebase DB reference
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tutorList.clear();

                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    String skillOffered = userSnap.child("skilloffered").getValue(String.class);

                    if (skillRequested.equalsIgnoreCase(skillOffered)) {
                        String name = userSnap.child("name").getValue(String.class);
                        String email = userSnap.child("email").getValue(String.class);
                        String username = userSnap.child("username").getValue(String.class);

                        tutorList.add(new UserModel(name, email, username));
                    }
                }

                adapter.notifyDataSetChanged();

                if (tutorList.isEmpty()) {
                    Toast.makeText(LearnRequestedSkill.this,
                            "No tutors found offering this skill", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LearnRequestedSkill.this,
                        "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
