package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.User;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String JSON_FILE = "src/Data/users.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Read all users from JSON
    public static List<User> loadUsers() {
        File file = new File(JSON_FILE);

        // Create file if it doesn't exist
        if (!file.exists()) {
            try {
                file.createNewFile();
                saveUsers(new ArrayList<>());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(JSON_FILE)) {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> users = gson.fromJson(reader, userListType);
            return users != null ? users : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Save users to JSON
    public static void saveUsers(List<User> users) {
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(users, writer);
            System.out.println("Users saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Validate login and return User object if successful
    public static User getUserByLogin(String email, String password) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getEmail().equals(email) &&
                    user.getPassword().equals(password)) {
                return user; // Return the user object
            }
        }
        return null; // Login failed
    }

    // Validate login (returns boolean)
    public static boolean validateLogin(String email, String password) {
        return getUserByLogin(email, password) != null;
    }

    // Get user by email only
    public static User getUserByEmail(String email) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    // Check if username exists
    public static boolean emailExists(String email) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    // Add new user
    public static boolean addUser(User newUser) {
        List<User> users = loadUsers();

        // Check if username already exists
        if (emailExists(newUser.getEmail())) {
            return false; // Username already taken
        }

        users.add(newUser);
        saveUsers(users);
        return true;
    }
}