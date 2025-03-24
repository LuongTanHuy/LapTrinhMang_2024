package laptrinhmang.ltm_chat.service.server;
import laptrinhmang.ltm_chat.ModelChat.Friend_ListMessage;
import laptrinhmang.ltm_chat.ModelChat.Group_ListMessage;
import laptrinhmang.ltm_chat.model.*;

import java.sql.*;
import java.util.*;

public class ConnectDB {

    public ConnectDB() {
    }

    private Statement statement()
    {
        try {
            String url = "jdbc:mysql://localhost:3306/ltm_chat";
            String username = "root";
            String password = "tanhuy09560";
            // Kết nối đến cơ sở dữ liệu
            Connection connection = DriverManager.getConnection(url, username, password);
            // Tạo Statement để thực hiện truy vấn SQL
            Statement statement = connection.createStatement();
            return statement;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized boolean removeMembersFromGroup(String token, List<Integer> memberIds) {
        try {
            Statement statement = statement();

            if (statement != null) {
                StringBuilder deleteQuery = new StringBuilder("DELETE FROM room WHERE token = '" + token + "' AND userID IN (");

                for (int i = 0; i < memberIds.size(); i++) {
                    deleteQuery.append(memberIds.get(i));
                    if (i < memberIds.size() - 1) {
                        deleteQuery.append(", ");
                    }
                }
                deleteQuery.append(")");

                System.out.println("Delete query: " + deleteQuery);

                int rowsAffected = statement.executeUpdate(deleteQuery.toString());

                return rowsAffected > 0;
            } else {
                System.err.println("Database connection is null.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Trả về false nếu có lỗi
    }

    public List<User> getGroupMembersByToken(int token) {
        List<User> members = new ArrayList<>();
        String query = "SELECT u.userID, u.fullName FROM room r JOIN user u ON r.userID = u.userID WHERE r.token = " + token;

        try {
            Statement statement = statement();
            if (statement != null) {
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int userID = resultSet.getInt("userID");
                    String fullName = resultSet.getString("fullName");
                    members.add(new User(userID, fullName)); // Thêm kết quả vào danh sách
                }
            } else {
                System.err.println("Không thể tạo Statement để thực hiện truy vấn.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public boolean addUserToRoom(int userID, int receiverID, String nameRoom, int token) {
        try {
            // Kiểm tra nếu nameRoom rỗng, lấy nameRoom từ bảng dựa vào token
            if (nameRoom == null || nameRoom.isEmpty()) {
                String getNameQuery = "SELECT nameRoom FROM room WHERE token = " + token + " LIMIT 1";
                Statement getNameStmt = statement();
                if (getNameStmt != null) {
                    ResultSet rs = getNameStmt.executeQuery(getNameQuery);
                    if (rs.next()) {
                        nameRoom = rs.getString("nameRoom");
                    }
                    rs.close();
                }
            }

            // Thực hiện truy vấn chèn user vào room
            String query = "INSERT INTO room (userID, receiverID, nameRoom, token) VALUES (" +
                    userID + ", " + receiverID + ", '" + nameRoom + "', " + token + ")";
            Statement stmt = statement();
            if (stmt != null) {
                int rowsAffected = stmt.executeUpdate(query);
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getNameRoomByToken(int token) {
        String nameRoom = null;
        try {
            String query = "SELECT nameRoom FROM room WHERE token = ? LIMIT 1";
            PreparedStatement pstmt = this.statement().getConnection().prepareStatement(query);
            pstmt.setInt(1, token);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                nameRoom = rs.getString("nameRoom");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nameRoom;
    }

    public List<Integer> getAllMembersInRoom(int token) {
        List<Integer> memberIds = new ArrayList<>();
        try {
            String query = "SELECT userID FROM room WHERE token = ?";
            PreparedStatement pstmt = this.statement().getConnection().prepareStatement(query);
            pstmt.setInt(1, token);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                memberIds.add(rs.getInt("userID"));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberIds;
    }

    public Map<String, Object> updateGroupNameByToken(int token, String newGroupName) {
        String updateQuery = "UPDATE room SET nameRoom = ? WHERE token = ?";
        String selectQuery = "SELECT userID FROM room WHERE token = ?";
        Map<String, Object> result = new HashMap<>();

        try {
            Statement stmt = statement();
            if (stmt != null) {
                Connection conn = stmt.getConnection();

                // Cập nhật tên nhóm
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setString(1, newGroupName);
                updateStmt.setInt(2, token);
                int rowsUpdated = updateStmt.executeUpdate();

                // Lấy danh sách userID
                PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                selectStmt.setInt(1, token);
                ResultSet rs = selectStmt.executeQuery();

                List<Integer> userIds = new ArrayList<>();
                while (rs.next()) {
                    userIds.add(rs.getInt("userID"));
                }

                result.put("token", token);
                result.put("newGroupName", newGroupName);
                result.put("userIDs", userIds);
                result.put("success", rowsUpdated > 0);

                rs.close();
                updateStmt.close();
                selectStmt.close();
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public boolean updateReceiverID(int token) {
        String query = "UPDATE room SET receiverID = 1 WHERE token = " + token + " AND receiverID = 0";

        Statement stmt = statement();
        if (stmt != null) {
            try {
                int rowsUpdated = stmt.executeUpdate(query);

                if (rowsUpdated > 0) {
                    System.out.println("Cập nhật receiverID thành công cho token: " + token);
                    return true;
                } else {
                    System.out.println("Không có dòng nào được cập nhật. Có thể token không tồn tại hoặc receiverID không bằng 0.");
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return false;
        }
    }

    // gờ rúp
    public void addGroup(int userId, String nameRoom, int token, List<Integer> memberIds) {
        try (Connection connection = statement().getConnection()) {
            // thêm người tạo nhóm vào
            String insertGroupQuery = "INSERT INTO room (userID, receiverID, nameRoom, token) VALUES (?, 0, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertGroupQuery, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, userId);
                stmt.setString(2, nameRoom);
                stmt.setInt(3, token);
                stmt.executeUpdate();

                // lấy roomID của nhóm vừa được tạo
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int roomId = rs.getInt(1);
                        System.out.println("RoomID vừa tạo: " + roomId);

                        // thêm thành viên vào nhóm
                        String addMemberQuery = "INSERT INTO room (userID, receiverID, nameRoom, token) VALUES (?, 0, ?, ?)";
                        try (PreparedStatement memberStmt = connection.prepareStatement(addMemberQuery)) {
                            for (int memberId : memberIds) {
                                memberStmt.setInt(1, memberId);
                                memberStmt.setString(2, nameRoom);
                                memberStmt.setInt(3, token);
                                memberStmt.addBatch();
                            }
                            memberStmt.executeBatch();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //User
    public User saveUser(User user)
    {
        String query = "INSERT INTO user (fullName, phone, password) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getFullName());
            stmt.setLong(2, user.getPhone()); // Sử dụng setString cho số điện thoại
            stmt.setString(3, user.getPassword());

            // Thực hiện câu lệnh INSERT
            int affectedRows = stmt.executeUpdate();

            // Kiểm tra nếu câu lệnh INSERT thành công
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Trả về đối tượng User với ID mới
                        return new User(generatedKeys.getInt(1),
                                user.getFullName(),
                                user.getPhone(),
                                user.getPassword());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public User getUserByPhone(int phone)
    {
        String query = "SELECT * FROM user WHERE phone = ?";

        try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query)) {
            stmt.setInt(1, phone);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return new User(Integer.parseInt(resultSet.getString("userID")),
                        resultSet.getString("fullName"),
                       Long.parseLong(resultSet.getString("phone")),
                        resultSet.getString("password"));
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public User getUserByID(int userID)
    {
        String query = "SELECT * FROM user WHERE userID = ?";

        try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return new User(Integer.parseInt(resultSet.getString("userID")),
                        resultSet.getString("fullName"),
                        Long.parseLong(resultSet.getString("phone")),
                        null);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    // Friend
    public boolean saveFriend(Friend friend)
    {
        String query = "INSERT INTO friend (userID, IDFriend) VALUES (?, ?)";

        try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1,friend.getUserId());
            stmt.setInt(2,friend.getFriendId());


            // Thực hiện câu lệnh INSERT
            int affectedRows = stmt.executeUpdate();

            // Kiểm tra nếu câu lệnh INSERT thành công
            if (affectedRows > 0) {
              return true;
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }

    public List<Integer> listIDFriend(int userID)
    {
        String query = "SELECT * FROM friend WHERE userID = ?";

        try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query)) {
            stmt.setInt(1, userID);

            List<Integer> listIdFriend = new ArrayList<>();
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                if(resultSet.getInt("IDFriend") != userID){
                    listIdFriend.add(resultSet.getInt("IDFriend"));
                }

            }
            return listIdFriend;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    //Room
    public List<Room> listMemberOfRoom(int token)
    {
        String query = "SELECT * FROM room WHERE token = ? ";

        try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query)) {
            stmt.setInt(1, token);

            List<Room> listRoom = new ArrayList<>();
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                listRoom.add(new Room(0,resultSet.getInt("userID"),0,"",0));
            }
            return listRoom;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    public List<Room> listRoom(int userID) {
        String query = """
        SELECT DISTINCT r.roomID, r.userID, r.receiverID, r.nameRoom, r.token
        FROM room r
        WHERE r.receiverID = 0 AND r.userID = ?
    """;

        try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query)) {
            // Bind the userID parameter
            stmt.setInt(1, userID);

            List<Room> listRoom = new ArrayList<>();
            ResultSet resultSet = stmt.executeQuery();

            // Iterate through the results and map them to Room objects
            while (resultSet.next()) {
                listRoom.add(new Room(
                        resultSet.getInt("roomID"),          // Room ID
                        resultSet.getInt("userID"),          // User ID of room creator
                        resultSet.getInt("token"),        // Room token (String)
                        resultSet.getString("nameRoom"),     // Room name
                        resultSet.getInt("receiverID")       // Receiver ID (group chat)
                ));
            }
            return listRoom;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public int memberGroupCount(int token)
    {
        String query = "SELECT COUNT(*) FROM room WHERE token = ?";

        try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query)) {
            stmt.setInt(1, token);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public int roomID(Room room)
    {

        if(room.getToken() == 0){
            String query = "SELECT * FROM room WHERE userID = ? AND receiverID = ?";

            try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query)) {
                stmt.setInt(1, room.getUserId());
                stmt.setInt(2, room.getUserReceiveId());

                ResultSet resultSet = stmt.executeQuery();

                int roomID = 0;
                while (resultSet.next()) {
                    if(resultSet.getInt("roomID") != 0){
                        roomID = resultSet.getInt("roomID");
                        return roomID;
                    }
                }

                if (roomID == 0){
                    return this.saveRoom(new Room(0,room.getUserId(),0,"",room.getUserReceiveId()));
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }else{
            String query = "SELECT * FROM room WHERE userID = ? AND token = ?";

            try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query)) {
                stmt.setInt(1, room.getUserId());
                stmt.setInt(2, room.getToken());

                ResultSet resultSet = stmt.executeQuery();

                int roomID = 0;
                while (resultSet.next()) {
                    if(resultSet.getInt("roomID") != 0){
                        roomID = resultSet.getInt("roomID");
                        return roomID;
                    }
                }

                if (roomID == 0){
                    return this.saveRoom(new Room(0,room.getUserId(), room.getToken(), room.getName(),0));
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return 0;
    }

    public int saveRoom(Room room)
    {
        if(room.getToken() == 0){
            String query = "INSERT INTO room(userID, receiverID) VALUES (?, ?)";

            try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1,room.getUserId());
                stmt.setInt(2,room.getUserReceiveId());


                // Thực hiện câu lệnh INSERT
                int affectedRows = stmt.executeUpdate();

                // Kiểm tra nếu câu lệnh INSERT thành công
                if (affectedRows > 0) {
                    return affectedRows;
                }

            } catch (Exception e) {
                System.out.println(e);
            }

        }else {
            String query = "INSERT INTO room(userID,token,name,receiverID) VALUES (?, ?,?,?)";

            try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1,room.getUserId());
                stmt.setInt(2,room.getToken());
                stmt.setString(3,room.getName());
                stmt.setInt(4,room.getUserReceiveId());


                // Thực hiện câu lệnh INSERT
                int affectedRows = stmt.executeUpdate();

                // Kiểm tra nếu câu lệnh INSERT thành công
                if (affectedRows > 0) {
                    return affectedRows;
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return 0;
    }

    //Message
    public boolean saveMessage(Message message)
    {
        if(message.getPath() != null){
            String query = "INSERT INTO message (roomID , message,typeMessage,path) VALUES (?, ?,?,?)";

            try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1,message.getRoomId());
                stmt.setString(2, message.getMessage());
                stmt.setString(3, message.getType());
                stmt.setString(4, message.getPath());

                // Thực hiện câu lệnh INSERT
                int affectedRows = stmt.executeUpdate();

                // Kiểm tra nếu câu lệnh INSERT thành công
                if (affectedRows > 0) {
                    return true;
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }else{
            String query = "INSERT INTO message (roomID , message,typeMessage) VALUES (?, ?,?)";

            try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1,message.getRoomId());
                stmt.setString(2, message.getMessage());
                stmt.setString(3, message.getType());


                // Thực hiện câu lệnh INSERT
                int affectedRows = stmt.executeUpdate();

                // Kiểm tra nếu câu lệnh INSERT thành công
                if (affectedRows > 0) {
                    return true;
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return false;
    }

    public List<Group_ListMessage> listChatGroup(Room room) {

        int token = room.getToken();// Mã phòng

        if (token != 0) {
            String query = "SELECT user.userID, user.fullName, message.message, message.sendTime, message.typeMessage,message.path,room.token,room.nameRoom FROM user INNER JOIN room ON user.userID = room.userID INNER JOIN message ON room.roomID = message.roomID WHERE (room.token = ? ) ORDER BY message.sendTime ASC";

            try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query)) {
                stmt.setInt(1, token);

                ResultSet resultSet = stmt.executeQuery();
                List<Group_ListMessage> list = new ArrayList<>();

                while (resultSet.next()) {
                    int _userID = resultSet.getInt("userID");
                    String fullName = resultSet.getString("fullName");
                    String message = resultSet.getString("message");
                    String sendTime = resultSet.getString("sendTime");
                    String typeMessage = resultSet.getString("typeMessage");
                    String path = resultSet.getString("path");
                    String nameRoom = resultSet.getString("nameRoom");
                    int tokenRoom  =  resultSet.getInt("token");

                    list.add(new Group_ListMessage(message,sendTime,typeMessage,path,fullName,tokenRoom,nameRoom,_userID));
                }

                return list;
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return null;
    }

    public List<Friend_ListMessage> listChatFriend(Room room) {
        int userID = room.getUserId();
        int receiverID = room.getUserReceiveId();

        String query = "SELECT user.userID, user.fullName, message.message, message.sendTime, message.typeMessage,message.path,room.receiverID FROM user INNER JOIN room ON user.userID = room.userID INNER JOIN message ON room.roomID = message.roomID WHERE (room.userID = ? AND room.receiverID = ?) OR (room.userID = ? AND room.receiverID = ?) ORDER BY message.sendTime ASC";


        try (PreparedStatement stmt = this.statement().getConnection().prepareStatement(query)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, receiverID);
            stmt.setInt(3, receiverID);
            stmt.setInt(4, userID);

            ResultSet resultSet = stmt.executeQuery();

            List<Friend_ListMessage> list = new ArrayList<>();

            while (resultSet.next()) {
                String message = resultSet.getString("message");
                String sendTime = resultSet.getString("sendTime");
                String typeMessage = resultSet.getString("typeMessage");
                String path = resultSet.getString("path");
                int receiverIDD = resultSet.getInt("receiverID");

                list.add(new Friend_ListMessage(message, sendTime, typeMessage, path, receiverIDD,userID));
            }

            return list;
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public List<String> searchUsersByNameOrPhoneOrRoom(String searchText) {
        List<String> results = new ArrayList<>();
        String userQuery = "SELECT fullName, phone FROM user WHERE fullName LIKE ? OR phone LIKE ?";
        String roomQuery = "SELECT nameRoom FROM room WHERE nameRoom LIKE ?";

        try {
            // Tìm kiếm trong bảng user
            try (PreparedStatement pstmtUser = this.statement().getConnection().prepareStatement(userQuery)) {
                String searchPattern = "%" + searchText + "%";
                pstmtUser.setString(1, searchPattern);
                pstmtUser.setString(2, searchPattern);

                ResultSet rsUser = pstmtUser.executeQuery();
                while (rsUser.next()) {
                    results.add("User: " + rsUser.getString("fullName") + " - " + rsUser.getString("phone"));
                }
                rsUser.close();
            }

            // Tìm kiếm trong bảng room
            try (PreparedStatement pstmtRoom = this.statement().getConnection().prepareStatement(roomQuery)) {
                String searchPattern = "%" + searchText + "%";
                pstmtRoom.setString(1, searchPattern);

                ResultSet rsRoom = pstmtRoom.executeQuery();
                while (rsRoom.next()) {
                    results.add("Room: " + rsRoom.getString("nameRoom"));
                }
                rsRoom.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public List<User> getUserList(int currentUserId) {
        List<User> userList = new ArrayList<>();
        String query = "SELECT userID, fullName FROM user WHERE userID != ?";

        try (Connection connection = this.statement().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, currentUserId); // Gán giá trị currentUserId

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int ID = resultSet.getInt("userID");
                    String fullName = resultSet.getString("fullName");

                    // Thêm bạn vào danh sách
                    userList.add(new User(ID, fullName));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    public List<User> getUsersNotInRoomByToken(int token) {
        List<User> usersNotInRoom = new ArrayList<>();
        String query = """
        SELECT u.userID, u.fullName
        FROM user u
        WHERE u.userID NOT IN (
            SELECT r.userID 
            FROM room r 
            WHERE r.token = %d
        )
    """.formatted(token);

        try {
            Statement stmt = statement();
            if (stmt != null) {
                ResultSet resultSet = stmt.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("userID");
                    String fullName = resultSet.getString("fullName");

                    User user = new User(id, fullName);
                    usersNotInRoom.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usersNotInRoom;
    }

}
