package anjali.learning.skilshare.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import anjali.learning.skilshare.R;
import anjali.learning.skilshare.model.UserProfile;

public class UserSwapAdapter extends RecyclerView.Adapter<UserSwapAdapter.ViewHolder> {

    private List<UserProfile> userList;
    private OnRequestClickListener listener;

    public interface OnRequestClickListener {
        void onRequestClick(UserProfile user);
    }

    public UserSwapAdapter(List<UserProfile> userList, OnRequestClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_swap, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserProfile user = userList.get(position);
        holder.usernameTV.setText("User: " + user.getUsername());
        holder.offeredTV.setText("Offers: " + user.getOfferedSkill());
        holder.requestedTV.setText("Wants: " + user.getRequestedSkill());

        holder.sendRequestBtn.setOnClickListener(v -> listener.onRequestClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTV, offeredTV, requestedTV;
        Button sendRequestBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTV = itemView.findViewById(R.id.usernameTV);
            offeredTV = itemView.findViewById(R.id.offeredTV);
            requestedTV = itemView.findViewById(R.id.requestedTV);
            sendRequestBtn = itemView.findViewById(R.id.sendRequestBtn);
        }
    }
}
