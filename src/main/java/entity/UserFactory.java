package entity;

/**
 * Factory for creating CommonUser objects.
 */
public class UserFactory {

    /**
     * Creates a new User.
     * @param name the name of the new user
     * @param password the password of the new user
     * @return the new user
     */
    public User create(String name, String password) {
        return new User(name, password);
    }
}
