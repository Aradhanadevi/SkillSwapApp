package anjali.learning.skilshare.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.*;

import anjali.learning.skilshare.R;
import anjali.learning.skilshare.model.UserModel;

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.TutorViewHolder> {

    private final Context context;
    private final ArrayList<UserModel> tutorList;
    private final String currentUsername;  // logged‑in user

    public TutorAdapter(Context ctx, ArrayList<UserModel> list, String currentUsername) {
        this.context          = ctx;
        this.tutorList        = list;
        this.currentUsername  = currentUsername;
    }

    @NonNull
    @Override
    public TutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tutor_card, parent, false);
        return new TutorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorViewHolder holder, int pos) {
        UserModel tutor = tutorList.get(pos);
        holder.nameTV.setText(tutor.name);
        holder.emailTV.setText(tutor.email);

        holder.sendBtn.setOnClickListener(v -> openSwapDialog(tutor));
    }

    @Override
    public int getItemCount() { return tutorList.size(); }

    // ─────────────────────────────────────────────────────────────
    //  POP‑UP (Screen‑5) + Firebase write
    // ─────────────────────────────────────────────────────────────
    private void openSwapDialog(UserModel tutor) {
        View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_swap_request, null, false);

        Spinner myOfferedSpinner   = dialogView.findViewById(R.id.spinnerMyOffered);
        Spinner tutorWantedSpinner = dialogView.findViewById(R.id.spinnerTutorWanted);
        EditText messageET         = dialogView.findViewById(R.id.etMessage);

        // 1. Load the current user’s offered skills & tutor’s requested skills
        DatabaseReference usersRef = FirebaseDatabase.getInstance()
                .getReference("users");

        usersRef.child(currentUsername)
                .addListenerForSingleValueEvent(new SimpleValue() {
                    @Override public void onDataChange(@NonNull DataSnapshot meSnap) {
                        String mySkills = meSnap.child("skilloffered").getValue(String.class);
                        ArrayAdapter<String> a1 = new ArrayAdapter<>(
                                context, android.R.layout.simple_spinner_item,
                                splitCsv(mySkills));
                        a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        myOfferedSpinner.setAdapter(a1);
                    }
                });

        usersRef.child(tutor.username)
                .addListenerForSingleValueEvent(new SimpleValue() {
                    @Override public void onDataChange(@NonNull DataSnapshot tSnap) {
                        String tutorWanted = tSnap.child("skillrequested").getValue(String.class);
                        ArrayAdapter<String> a2 = new ArrayAdapter<>(
                                context, android.R.layout.simple_spinner_item,
                                splitCsv(tutorWanted));
                        a2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        tutorWantedSpinner.setAdapter(a2);
                    }
                });

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Send swap request to " + tutor.name)
                .setView(dialogView)
                .setPositiveButton("Submit", (d, which) -> {
                    String offered   = myOfferedSpinner.getSelectedItem().toString();
                    String requested = tutorWantedSpinner.getSelectedItem().toString();
                    String message   = messageET.getText().toString().trim();

                    pushMessageToFirebase(tutor.username, offered, requested, message);
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }

    private void pushMessageToFirebase(String tutorUsername,
                                       String offered, String requested, String msg) {

        Map<String, Object> data = new HashMap<>();
        data.put("from",          currentUsername);
        data.put("offeredSkill",  offered);
        data.put("requestedSkill",requested);
        data.put("message",       msg);
        data.put("timestamp",     ServerValue.TIMESTAMP);

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(tutorUsername)
                .child("messages");

        ref.push().setValue(data)
                .addOnSuccessListener(a -> Toast.makeText(context,
                        "Request sent!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context,
                        "Failed: "+e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    /** Simple helper to split comma‑separated skills into a List */
    private List<String> splitCsv(String csv) {
        if (csv == null || csv.trim().isEmpty()) return Collections.singletonList("N/A");
        String[] arr = csv.split("\\s*,\\s*");
        return Arrays.asList(arr);
    }

    /** Shorthand abstract class for single‑use listeners */
    private abstract static class SimpleValue implements ValueEventListener {
        @Override public void onCancelled(@NonNull DatabaseError error) { }
    }

    // ─── ViewHolder ─────────────────────────────────────────────
    static class TutorViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, emailTV; Button sendBtn;
        TutorViewHolder(@NonNull View v) {
            super(v);
            nameTV = v.findViewById(R.id.tutorName);
            emailTV = v.findViewById(R.id.tutorEmail);
            sendBtn = v.findViewById(R.id.sendMessageBtn);
        }
    }
}
