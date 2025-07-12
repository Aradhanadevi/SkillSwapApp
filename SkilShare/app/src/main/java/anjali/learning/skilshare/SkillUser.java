package anjali.learning.skilshare;

public class SkillUser {
    public String name;
    public String skillsOffered;
    public String skillsWanted;
    public float rating;

    public SkillUser(String name, String skillsOffered, String skillsWanted, float rating) {
        this.name = name;
        this.skillsOffered = skillsOffered;
        this.skillsWanted = skillsWanted;
        this.rating = rating;
    }
}

