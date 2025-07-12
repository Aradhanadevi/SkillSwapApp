package anjali.learning.skilshare;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SwapRequestsActivity extends AppCompatActivity {

    TextView requesterNameTV, requestedSkillTV, offeredSkillTV;
    Button acceptButton, rejectButton;

    String username = "userB"; // this is the logged-in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swap_requests);

        requesterNameTV = findViewById(R.id.requesterNameTV);
        requestedSkillTV = findViewById(R.id.requestedSkillTV);
        offeredSkillTV = findViewById(R.id.offeredSkillTV);
        acceptButton = findViewById(R.id.acceptButton);
        rejectButton = findViewById(R.id.rejectButton);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("swapRequests").child(username);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot request : snapshot.getChildren()) {
                        String requester = request.getKey();
                        String requestedSkill = request.child("skillRequested").getValue(String.class);
                        String offeredSkill = request.child("skillOffered").getValue(String.class);

                        requesterNameTV.setText("Requester: " + requester);
                        requestedSkillTV.setText("Requested Skill: " + requestedSkill);
                        offeredSkillTV.setText("Offered Skill: " + offeredSkill);

                        acceptButton.setOnClickListener(v -> {
                            dbRef.child(requester).child("status").setValue("accepted");
                            Toast.makeText(SwapRequestsActivity.this, "Accepted!", Toast.LENGTH_SHORT).show();
                        });

                        rejectButton.setOnClickListener(v -> {
                            dbRef.child(requester).removeValue();
                            Toast.makeText(SwapRequestsActivity.this, "Rejected!", Toast.LENGTH_SHORT).show();
                        });

                        break; // only handle first request for now
                    }
                } else {
                    Toast.makeText(SwapRequestsActivity.this, "No requests", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SwapRequestsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

