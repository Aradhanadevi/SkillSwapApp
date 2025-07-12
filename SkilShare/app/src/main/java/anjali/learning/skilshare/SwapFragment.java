package anjali.learning.skilshare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SwapFragment extends Fragment {

    private String username; // 🔑 Will be set dynamically from arguments

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swap, container, false);

        // ✅ Get username from arguments
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        Button swapButton = view.findViewById(R.id.swap_button);
        swapButton.setOnClickListener(v -> findAndRequestSwap());

        return view;
    }

    private void findAndRequestSwap() {
        if (username == null) {
            Toast.makeText(getActivity(), "Username not provided!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot currentUserSnapshot) {
                if (!currentUserSnapshot.exists()) {
                    Toast.makeText(getActivity(), "User not found!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String offeredSkill = currentUserSnapshot.child("skilloffered").getValue(String.class);
                String requestedSkill = currentUserSnapshot.child("skillrequested").getValue(String.class);

                if (offeredSkill == null || requestedSkill == null) {
                    Toast.makeText(getActivity(), "Skills not set!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ✅ Search for a matching user
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot allUsersSnapshot) {
                        boolean matchFound = false;

                        for (DataSnapshot userSnapshot : allUsersSnapshot.getChildren()) {
                            String otherUsername = userSnapshot.getKey();
                            if (otherUsername.equals(username)) continue; // Skip self

                            String otherOffered = userSnapshot.child("skilloffered").getValue(String.class);
                            String otherRequested = userSnapshot.child("skillrequested").getValue(String.class);

                            if (offeredSkill.equalsIgnoreCase(otherRequested) &&
                                    requestedSkill.equalsIgnoreCase(otherOffered)) {

                                // ✅ Create swap request
                                DatabaseReference swapRef = FirebaseDatabase.getInstance()
                                        .getReference("swaprequests")
                                        .push();

                                String requestId = swapRef.getKey();
                                SwapRequest request = new SwapRequest(
                                        requestId,
                                        username,
                                        otherUsername,
                                        offeredSkill,
                                        requestedSkill,
                                        "pending"
                                );

                                swapRef.setValue(request);
                                Toast.makeText(getActivity(), "Swap request sent to " + otherUsername, Toast.LENGTH_SHORT).show();
                                matchFound = true;
                                break;
                            }
                        }

                        if (!matchFound) {
                            Toast.makeText(getActivity(), "No matching user found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
