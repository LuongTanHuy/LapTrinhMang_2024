package laptrinhmang.ltm_chat.ModelChat;

import java.net.Socket;

public class SocketUser {
    private int id;
    private Socket socket;
    private String fullName,timeIn,timeOut,status,quantityFriend,quantityGroup,ip;


    public SocketUser() {
    }

    public SocketUser(int id, Socket socket, String fullName, String timeIn, String timeOut, String status, String quantityFriend, String quantityGroup, String ip) {
        this.id = id;
        this.socket = socket;
        this.fullName = fullName;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.status = status;
        this.quantityFriend = quantityFriend;
        this.quantityGroup = quantityGroup;
        this.ip = ip;
    }

    public SocketUser(int id, Socket socket) {
        this.id = id;
        this.socket = socket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuantityFriend() {
        return quantityFriend;
    }

    public void setQuantityFriend(String quantityFriend) {
        this.quantityFriend = quantityFriend;
    }

    public String getQuantityGroup() {
        return quantityGroup;
    }

    public void setQuantityGroup(String quantityGroup) {
        this.quantityGroup = quantityGroup;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    @Override
    public String toString() {
        return "SocketUser{" +
                "id=" + id +
                ", socket=" + socket +
                ", fullName='" + fullName + '\'' +
                ", timeIn='" + timeIn + '\'' +
                ", timeOut='" + timeOut + '\'' +
                ", status='" + status + '\'' +
                ", quantityFriend='" + quantityFriend + '\'' +
                ", quantityGroup='" + quantityGroup + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
