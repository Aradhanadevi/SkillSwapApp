package anjali.learning.skilshare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import anjali.learning.skilshare.model.SwapRequest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import anjali.learning.skilshare.Adapter.UserSwapAdapter;
import anjali.learning.skilshare.model.UserProfile;

public class ExploreSwapUsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserSwapAdapter adapter;
    private List<UserProfile> userList;
    private String currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_swap_users, container, false);

        currentUser = getArguments() != null ? getArguments().getString("username") : null;

        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();
        adapter = new UserSwapAdapter(userList, user -> sendRequestToUser(user));
        recyclerView.setAdapter(adapter);

        loadUsers();

        return view;
    }

    private void loadUsers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    UserProfile user = snap.getValue(UserProfile.class);
                    if (user != null && !user.getUsername().equals(currentUser)) {
                        userList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
                if (userList.isEmpty()) {
                    Toast.makeText(getContext(), "No other users found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendRequestToUser(UserProfile user) {
        DatabaseReference swapsRef = FirebaseDatabase.getInstance().getReference("swaprequests");
        String id = swapsRef.push().getKey();

        SwapRequest request = new SwapRequest(
                id,
                currentUser,
                user.getUsername(),
                user.getOfferedSkill(),
                user.getRequestedSkill(),
                "pending"
        );

        swapsRef.child(id).setValue(request);
        Toast.makeText(getContext(), "Request sent to " + user.getUsername(), Toast.LENGTH_SHORT).show();
    }
}
