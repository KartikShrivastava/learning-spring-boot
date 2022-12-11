package com.yld.learningspringboot.dao;

import com.yld.learningspringboot.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FakeDataDao implements UserDao {

    // BAM! this is our database
    // TODO: Create an implementation with actual PostgreSQL database
    // TODO: Add this application package and database instance into a docker container and run it
    private Map<UUID, User> database;

    public FakeDataDao() {
        database = new HashMap<>();

        // mock existing data in database
        UUID yayiUserUid = UUID.randomUUID();
        database.put(yayiUserUid, new User(yayiUserUid, "Yayi", "Daniella",
                User.Gender.FEMALE, 22, "yayi@gmail.com"));
    }

    // One interesting important thing to note here is that,
    // we will not actually implement any logic to create userUid here
    // as that is not the job of DAO, it should be handled in service layer
    // DAO only handles the CRUD database operations request
    @Override
    public int insertUser(UUID userUid, User user) {
        database.put(userUid, user);
        return 1;
    }

    @Override
    public List<User> selectAllUsers() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Optional<User> selectUserByUserUid(UUID userUid) {
        return Optional.ofNullable(database.get(userUid));
    }

    @Override
    public int updateUser(User user) {
        database.put(user.getUserId(), user);
        return 1;
    }

    @Override
    public int deleteUserByUserUid(UUID userUid) {
        database.remove(userUid);
        return 1;
    }
}
