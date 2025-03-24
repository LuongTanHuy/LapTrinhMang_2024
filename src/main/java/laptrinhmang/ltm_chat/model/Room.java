package laptrinhmang.ltm_chat.model;

public class Room {

    private int id;
    private int userId;
    private int receiverId;
    private int token;
    private String name;
    private String memberGroupCount;


    public Room() {
    }

    public Room(int id, int userId, int token, String name,int receiverId) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.name = name;
        this.receiverId = receiverId;
    }
    public Room(int token, String name, int memberCount) {
        this.token = token;
        this.name = name;
        this.memberGroupCount = String.valueOf(memberCount);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemberGroupCount() {
        return memberGroupCount;
    }

    public void setMemberGroupCount(String memberGroupCount) {
        this.memberGroupCount = memberGroupCount;
    }

    public int getUserReceiveId() {
        return receiverId;
    }

    public void setUserReceiveId(int receiverId) {
        this.receiverId = receiverId;
    }
    public int getMemberCount() {
        try {
            return Integer.parseInt(memberGroupCount);
        } catch (NumberFormatException e) {
            return 0; // Trả về 0 nếu không thể chuyển đổi
        }
    }
}
