package anjali.learning.skilshare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import anjali.learning.skilshare.Adapter.SwapRequestAdapter;
import anjali.learning.skilshare.model.SwapRequest;

public class ViewSwapRequestsFragment extends Fragment {

    private String username;
    private RecyclerView recyclerView;
    private SwapRequestAdapter adapter;
    private List<SwapRequest> requestList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_swap_requests, container, false);

        username = getArguments() != null ? getArguments().getString("username") : null;

        recyclerView = view.findViewById(R.id.recyclerViewSwapRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        requestList = new ArrayList<>();
        adapter = new SwapRequestAdapter(requestList, new SwapRequestAdapter.OnActionClickListener() {
            @Override
            public void onAccept(SwapRequest request) {
                updateStatus(request.getId(), "accepted");

            }

            @Override
            public void onReject(SwapRequest request) {
                updateStatus(request.getId(), "rejected");

            }
        });
        recyclerView.setAdapter(adapter);

        loadSwapRequests();

        return view;
    }

    private void loadSwapRequests() {
        if (username == null) {
            Toast.makeText(getContext(), "No username!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("swaprequests");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    SwapRequest request = snap.getValue(SwapRequest.class);
                    if (request != null && username.equals(request.getToUser())) {
                        requestList.add(request);
                    }
                }
                adapter.notifyDataSetChanged();
                if (requestList.isEmpty()) {
                    Toast.makeText(getContext(), "No swap requests.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatus(String requestId, String status) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("swaprequests").child(requestId).child("status");
        ref.setValue(status);
        Toast.makeText(getContext(), "Marked as " + status, Toast.LENGTH_SHORT).show();
    }
}
