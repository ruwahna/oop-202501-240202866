package com.upb.agripos.dao.interfaces;
import com.upb.agripos.model.User;
import java.util.List;

public interface UserDAO {
    User findByUsername(String username);
    void save(User user);
    List<User> findAll();
    void update(User user);
    void delete(String username);
    boolean authenticate(String username, String password);
    boolean usernameExists(String username);
    void updatePassword(String username, String newPassword);
    User findByUsernameAndRole(String username, String role);
    List<User> findByRole(String role);
}