package com.yld.learningspringboot.dao;

import com.yld.learningspringboot.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// DAO layer of n-tier architecture
// responsible for performing operation on model class objects and interacts
// with database
// TODO: Find benefit of having this DAO as interface rather than a class/impl
public interface UserDao {

    // CREATE
    int insertUser(UUID userUid, User user);

    // READ
    List<User> selectAllUsers();
    Optional<User> selectUserByUserUid(UUID userUid);

    // UPDATE
    int updateUser(User user);

    // DELETE
    int deleteUserByUserUid(UUID userUid);
}
