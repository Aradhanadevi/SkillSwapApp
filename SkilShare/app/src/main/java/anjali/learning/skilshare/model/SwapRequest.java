package anjali.learning.skilshare.model;

public class SwapRequest {

    private String id;
    private String fromUser;
    private String toUser;
    private String offeredSkill;
    private String requestedSkill;
    private String status;

    public SwapRequest() {
        // Required for Firebase
    }

    public SwapRequest(String id, String fromUser, String toUser, String offeredSkill, String requestedSkill, String status) {
        this.id = id;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.offeredSkill = offeredSkill;
        this.requestedSkill = requestedSkill;
        this.status = status;
    }

    // 🔑 Use this if you want to create without ID first
    public SwapRequest(String fromUser, String toUser, String offeredSkill, String requestedSkill) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.offeredSkill = offeredSkill;
        this.requestedSkill = requestedSkill;
        this.status = "pending";
    }

    // Getters and setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFromUser() { return fromUser; }
    public void setFromUser(String fromUser) { this.fromUser = fromUser; }

    public String getToUser() { return toUser; }
    public void setToUser(String toUser) { this.toUser = toUser; }

    public String getOfferedSkill() { return offeredSkill; }
    public void setOfferedSkill(String offeredSkill) { this.offeredSkill = offeredSkill; }

    public String getRequestedSkill() { return requestedSkill; }
    public void setRequestedSkill(String requestedSkill) { this.requestedSkill = requestedSkill; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
