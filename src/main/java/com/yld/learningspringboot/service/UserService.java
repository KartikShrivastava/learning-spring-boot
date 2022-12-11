package com.yld.learningspringboot.service;

import com.yld.learningspringboot.dao.UserDao;
import com.yld.learningspringboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Service layer of n-tier architecture
// Handles business logic of the customer registration microservice
@Service
public class UserService {

    // To unit test UserService class we need to mock UserDao as it is dependent on the
    // state of UserDao
    private UserDao userDao;

    // Autowired annotation instantiate UserService along with FakeDataDao class object
    // What is happening below is called dependency injection of a UserDao type into service layer
    @Autowired
    public UserService(UserDao userDao) {
        // bad way of instantiating object, as this isn't singleton but
        // suppose to be
        // this.userDao = new FakeDataDao();

        // better way using Spring (dependency injection)
        this.userDao = userDao;
    }

    public int insertUser(User user) {
        // TODO: I guess we do not need to generate new UUID as user already has one assigned to it
        UUID userUid = UUID.randomUUID();
        user.setUserId(userUid);
        return userDao.insertUser(userUid, user);
    }

    public List<User> getAllUsers(Optional<String> gender) {
        List<User> users = userDao.selectAllUsers();
        if(gender.isEmpty()) {
            return users;
        }
        try{
            User.Gender genderEnum = User.Gender.valueOf(gender.get().toUpperCase());
            // TODO: Update it with real database query
            return users.stream().filter(user -> user.getGender().equals(genderEnum)).toList();
        } catch (Exception e) {
            throw new IllegalStateException("Invalid gender", e);
        }
    }

    public Optional<User> getUser(UUID userUid) {
        return userDao.selectUserByUserUid((userUid));
    }

    public int updateUser(User user) {
        Optional<User> optionalUser = getUser(user.getUserId());
        if(optionalUser.isPresent()){
            return userDao.updateUser(user);
        }
        return -1;
    }

    public int removeUser(UUID userUid) {
        Optional<User> optionalUser = getUser(userUid);
        if(optionalUser.isPresent()){
            return userDao.deleteUserByUserUid(userUid);
        }
        return -1;
    }
}
