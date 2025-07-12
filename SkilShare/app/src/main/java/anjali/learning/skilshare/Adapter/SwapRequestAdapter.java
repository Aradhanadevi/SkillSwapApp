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
import anjali.learning.skilshare.model.SwapRequest;


public class SwapRequestAdapter extends RecyclerView.Adapter<SwapRequestAdapter.ViewHolder> {

    private List<SwapRequest> requestList;
    private OnActionClickListener listener;

    public interface OnActionClickListener {
        void onAccept(SwapRequest request);
        void onReject(SwapRequest request);
    }

    public SwapRequestAdapter(List<SwapRequest> requestList, OnActionClickListener listener) {
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_swap_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SwapRequest request = requestList.get(position);
        holder.requesterNameTV.setText("Requester: " + request.getFromUser());
        holder.requestedSkillTV.setText("Requested Skill: " + request.getRequestedSkill());
        holder.offeredSkillTV.setText("Offered Skill: " + request.getOfferedSkill());

        holder.acceptButton.setOnClickListener(v -> listener.onAccept(request));
        holder.rejectButton.setOnClickListener(v -> listener.onReject(request));
    }


    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView requesterNameTV, requestedSkillTV, offeredSkillTV;
        Button acceptButton, rejectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            requesterNameTV = itemView.findViewById(R.id.requesterNameTV);
            requestedSkillTV = itemView.findViewById(R.id.requestedSkillTV);
            offeredSkillTV = itemView.findViewById(R.id.offeredSkillTV);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }
}
