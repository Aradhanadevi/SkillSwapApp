package anjali.learning.skilshare;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LearnRequestedSkill extends AppCompatActivity {

    TextView skillNameTV, tutorTV, descriptionTV, emailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_requested_skill);

        skillNameTV = findViewById(R.id.skillNameTV);
        tutorTV = findViewById(R.id.tutorTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        emailTV = findViewById(R.id.emailTV);

        // ✅ Get username from Intent dynamically!
        String username = getIntent().getStringExtra("username");

        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Username not provided!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("requestedskillcourses")
                .child(username);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String skillName = child.getKey();
                        String tutor = child.child("tutor").getValue(String.class);
                        String description = child.child("description").getValue(String.class);
                        String email = child.child("email").getValue(String.class);

                        skillNameTV.setText("Skill: " + skillName);
                        tutorTV.setText("Tutor: " + tutor);
                        descriptionTV.setText("Description: " + description);
                        emailTV.setText("Tutor Email: " + email);
                    }
                } else {
                    Toast.makeText(LearnRequestedSkill.this, "No requested skill found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(LearnRequestedSkill.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
