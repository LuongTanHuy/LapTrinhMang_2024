package laptrinhmang.ltm_chat.model;

public class Friend {
    private int id;
    private int userId;
    private int friendId;
    private String name; // Thêm thuộc tính tên cho bạn bè

    public Friend(int id, int userId, String name) {
        this.id = id;
        this.userId = userId;
        this.friendId = friendId;
        this.name = name;
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

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
