package anjali.learning.skilshare;

public class SwapRequest {

    private String requestId;  // Firebase push key
    private String requesterUsername; // Who is asking
    private String receiverUsername;  // Who is being asked
    private String skillOffered;      // e.g., "Photoshop"
    private String skillRequested;    // e.g., "Excel"
    private String status;            // pending / accepted / rejected

    public SwapRequest() {
        // Required empty constructor for Firebase
    }

    // Full constructor
    public SwapRequest(String requestId, String requesterUsername, String receiverUsername,
                       String skillOffered, String skillRequested, String status) {
        this.requestId = requestId;
        this.requesterUsername = requesterUsername;
        this.receiverUsername = receiverUsername;
        this.skillOffered = skillOffered;
        this.skillRequested = skillRequested;
        this.status = status;
    }

    // Getters
    public String getRequestId() {
        return requestId;
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public String getSkillOffered() {
        return skillOffered;
    }

    public String getSkillRequested() {
        return skillRequested;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setRequesterUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public void setSkillOffered(String skillOffered) {
        this.skillOffered = skillOffered;
    }

    public void setSkillRequested(String skillRequested) {
        this.skillRequested = skillRequested;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
