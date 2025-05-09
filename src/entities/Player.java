package entities;

public class Player {
    private int id;
    private String name;
    private int age;
    private String position;
    private int teamId;
    private String nationality;
    private int jerseyNumber;

    public Player() {
    }

    public Player(int id, String name, int age, String position, int teamId, String nationality, int jerseyNumber) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.position = position;
        this.teamId = teamId;
        this.nationality = nationality;
        this.jerseyNumber = jerseyNumber;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPosition() {
        return position;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getNationality() {
        return nationality;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", position='" + position + '\'' +
                ", teamId=" + teamId +
                ", nationality='" + nationality + '\'' +
                ", jerseyNumber=" + jerseyNumber +
                '}';
    }
}