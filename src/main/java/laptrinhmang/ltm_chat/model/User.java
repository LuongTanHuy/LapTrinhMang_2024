package laptrinhmang.ltm_chat.model;

public class User {
    private int ID;
    private String fullName;
    private String password;
    private Long phone;
    private String receiver;

    public User() {
    }

    // Constructor for only userID and fullName
    public User(int ID, String fullName) {
        this.ID = ID;
        this.fullName = fullName;
    }

    // Full constructor
    public User(int ID, String fullName, Long phone , String password) {
        this.ID = ID;
        this.fullName = fullName;
        this.password = password;
        this.phone = phone;
    }

    // Getters and Setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

}

