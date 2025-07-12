package anjali.learning.skilshare.model;

public class UserProfile {
    private String username;
    private String offeredSkill;
    private String requestedSkill;

    public UserProfile() {}

    public UserProfile(String username, String offeredSkill, String requestedSkill) {
        this.username = username;
        this.offeredSkill = offeredSkill;
        this.requestedSkill = requestedSkill;
    }

    public String getUsername() { return username; }
    public String getOfferedSkill() { return offeredSkill; }
    public String getRequestedSkill() { return requestedSkill; }
}
