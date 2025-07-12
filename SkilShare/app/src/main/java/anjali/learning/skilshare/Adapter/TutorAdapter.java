package anjali.learning.skilshare.Adapter;



import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import anjali.learning.skilshare.ChatActivity;
import anjali.learning.skilshare.ChatForPeerSkillShareActivity;
import anjali.learning.skilshare.R;
import anjali.learning.skilshare.model.UserModel;

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.TutorViewHolder> {

    Context context;
    ArrayList<UserModel> tutorList;

    public TutorAdapter(Context context, ArrayList<UserModel> tutorList) {
        this.context = context;
        this.tutorList = tutorList;
    }

    @NonNull
    @Override
    public TutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tutor_card, parent, false);
        return new TutorViewHolder(view);
    }
//for skill swaping and on clicking message button redirect on ChatPeerSkillShareActivity
    @Override
    public void onBindViewHolder(@NonNull TutorViewHolder holder, int position) {
        UserModel user = tutorList.get(position);
        holder.nameTV.setText(user.name);
        holder.emailTV.setText(user.email);

        holder.sendBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatForPeerSkillShareActivity.class); // Replace with your actual chat activity
            intent.putExtra("receiverUsername", user.username);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    public static class TutorViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, emailTV;
        Button sendBtn;

        public TutorViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.tutorName);
            emailTV = itemView.findViewById(R.id.tutorEmail);
            sendBtn = itemView.findViewById(R.id.sendMessageBtn);
        }
    }
}

