package laptrinhmang.ltm_chat.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private String message;
    private int roomId;
    private String sendTime;
    private String type;
    private String path;

    public Message(String message, int roomId, String sendTime, String type) {
        this.message = message;
        this.roomId = roomId;
        this.sendTime = sendTime;
        this.type = type;
    }

    public Message(String message, int roomId, String sendTime, String type, String path) {
        this.message = message;
        this.roomId = roomId;
        this.sendTime = sendTime;
        this.type = type;
        this.path = path;
    }

    public Message(String message, String sendTime, String type, String path) {
        this.message = message;
        this.sendTime = sendTime;
        this.type = type;
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getSendTimeNotFormat(){
        return sendTime;
    }

    public String getSendTime() {
        // Giả sử chuỗi sendTime có định dạng "yyyy-MM-dd HH:mm:ss"
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(sendTime, inputFormatter);

        // Định dạng lại thành kiểu "HH:mm dd/MM/yyyy"
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return dateTime.format(outputFormatter);
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
