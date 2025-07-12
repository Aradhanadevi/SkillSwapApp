package anjali.learning.skilshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SkillUserAdapter extends RecyclerView.Adapter<SkillUserAdapter.ViewHolder> {

    private Context context;
    private List<SkillUser> userList;
    private boolean isLoggedIn = false; // TODO: Replace this with actual login check later

    public SkillUserAdapter(Context context, List<SkillUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public SkillUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.skill_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillUserAdapter.ViewHolder holder, int position) {
        SkillUser user = userList.get(position);

        holder.userName.setText(user.name);
        holder.skillsOffered.setText("Skills Offered => " + user.skillsOffered);
        holder.skillsWanted.setText("Skills Wanted => " + user.skillsWanted);
        holder.userRating.setText("Rating: " + user.rating + "/5");

        holder.requestButton.setOnClickListener(v -> {
            if (isLoggedIn) {
                Toast.makeText(context, "Request sent to " + user.name, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Please login to send request!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName, skillsOffered, skillsWanted, userRating;
        Button requestButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            skillsOffered = itemView.findViewById(R.id.skillsOffered);
            skillsWanted = itemView.findViewById(R.id.skillsWanted);
            userRating = itemView.findViewById(R.id.userRating);
            requestButton = itemView.findViewById(R.id.requestButton);
        }
    }
}
