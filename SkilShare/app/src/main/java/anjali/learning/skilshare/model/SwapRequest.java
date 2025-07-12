package anjali.learning.skilshare.model;

public class SwapRequest {
    private String requesterName;
    private String requestedSkill;
    private String offeredSkill;
    private String contactInfo;

    public SwapRequest() {
        // Needed for Firebase
    }

    public SwapRequest(String requesterName, String requestedSkill, String offeredSkill, String contactInfo) {
        this.requesterName = requesterName;
        this.requestedSkill = requestedSkill;
        this.offeredSkill = offeredSkill;
        this.contactInfo = contactInfo;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequestedSkill() {
        return requestedSkill;
    }

    public void setRequestedSkill(String requestedSkill) {
        this.requestedSkill = requestedSkill;
    }

    public String getOfferedSkill() {
        return offeredSkill;
    }

    public void setOfferedSkill(String offeredSkill) {
        this.offeredSkill = offeredSkill;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
