package anjali.learning.skilshare.model;

public class Course {
    private String description, Tutor, category, language, location, imageUrl, courseName, skills;
    private int noofvideos, price;
    private String playlistlink;

    // THIS is the only extra field you add
    private String skils;

    public Course() {}

    // Getters
    public String getDescription() { return description; }
    public String getTutor() { return Tutor; }
    public String getLanguage() { return language; }
    public String getLocation() { return location; }
    public String getImageUrl() { return imageUrl; }
    public int getNoofvideos() { return noofvideos; }
    public String getCategory() { return category; }
    public String getSkills() { return skills; }
    public int getPrice() { return price; }
    public String getCourseName() { return courseName; }
    public String getPlaylistlink() { return playlistlink; }

    public String getSkils() { return skils; }

    // Setters
    public void setDescription(String description) { this.description = description; }
    public void setTutor(String Tutor) { this.Tutor = Tutor; }
    public void setLanguage(String language) { this.language = language; }
    public void setLocation(String location) { this.location = location; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setNoofvideos(int noofvideos) { this.noofvideos = noofvideos; }
    public void setCategory(String category) { this.category = category; }
    public void setSkills(String skills) { this.skills = skills; }
    public void setPrice(int price) { this.price = price; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setPlaylistlink(String playlistlink) { this.playlistlink = playlistlink; }

    //  THIS is the mapping function: anytime Firebase finds "skils", it will copy it to "skills"
    public void setSkils(String skils) {
        this.skils = skils;
        this.skills = skils;
    }
}
