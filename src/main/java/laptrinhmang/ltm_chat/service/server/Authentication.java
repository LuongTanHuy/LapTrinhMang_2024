package laptrinhmang.ltm_chat.service.server;

import laptrinhmang.ltm_chat.model.User;

public class Authentication {
    private HashCode hashCode = new HashCode();
    private ConnectDB connectDB = new ConnectDB();
    private final String KEY = "1234567890123456";

    public Authentication() {
    }

    public User SignUp(String fullName,Long phone,String password){
        try {
            return connectDB.saveUser(new User(0,fullName,phone,this.hashCode.encrypt(password,KEY)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User Login(int phone,String password){
        try {
            User user = this.connectDB.getUserByPhone(phone);
            if (user != null) {
                if (this.hashCode.decrypt(user.getPassword(), KEY).equals(password)) {
                    return user;
                }
            }

            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
