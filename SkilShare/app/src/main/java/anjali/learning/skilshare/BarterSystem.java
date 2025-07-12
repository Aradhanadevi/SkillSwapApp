package anjali.learning.skilshare;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import anjali.learning.skilshare.model.SwapRequest;

public class BarterSystem {

    private DatabaseReference swapsRef;

    public BarterSystem() {
        swapsRef = FirebaseDatabase.getInstance().getReference("swaps");
    }

    public void createSwapRequest(String fromUser, String toUser, String offeredSkill, String requestedSkill) {
        String requestId = swapsRef.push().getKey();
        SwapRequest request = new SwapRequest(fromUser, toUser, offeredSkill, requestedSkill);
        swapsRef.child(requestId).setValue(request);
    }

    public void acceptSwap(String requestId) {
        swapsRef.child(requestId).child("status").setValue("accepted");
    }

    public void rejectSwap(String requestId) {
        swapsRef.child(requestId).child("status").setValue("rejected");
    }

    public void cancelSwap(String requestId) {
        swapsRef.child(requestId).child("status").setValue("cancelled");
    }
}
