package com.yld.learningspringboot.dao;

import com.yld.learningspringboot.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

// User test for FakeDataDao
class FakeDataDaoTest {

    private FakeDataDao fakeDataDao;

    @BeforeEach
    void setUp() {
        fakeDataDao = new FakeDataDao();
    }

    @Test
    void shouldInsertUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Laksmi", "Daniella",
                User.Gender.FEMALE, 22, "laksmi@email.com");
        fakeDataDao.insertUser(userId, user);

        List<User> users = fakeDataDao.selectAllUsers();
        assertThat(users).hasSize(2);
    }

    @Test
    void shouldSelectAllUsers() {
        List<User> users = fakeDataDao.selectAllUsers();
        assertThat(users).hasSize(1);

        User user = users.get(0);

        assertThat(user.getAge()).isEqualTo(22);
        assertThat(user.getFirstName()).isEqualTo("Yayi");
        assertThat(user.getLastName()).isEqualTo("Daniella");
        assertThat(user.getGender()).isEqualTo(User.Gender.FEMALE);
        assertThat(user.getEmail()).isEqualTo("yayi@gmail.com");
        assertThat(user.getUserId()).isNotNull();

        // to check if fakeDataDao has user with Uid same as of its key
        for(User userEle : users){
            Optional<User> userByUid = fakeDataDao.selectUserByUserUid(userEle.getUserId());
            assertThat(userByUid.isPresent()).isTrue();
            userByUid.ifPresent(value -> assertThat(value.getUserId()).isEqualTo(userEle.getUserId()));
        }
    }

    @Test
    void shouldSelectUserByUserUid() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Laksmi", "Daniella",
                User.Gender.FEMALE, 22, "laksmi@email.com");
        fakeDataDao.insertUser(userId, user);

        assertThat(fakeDataDao.selectAllUsers()).hasSize(2);

        Optional<User> userOptional = fakeDataDao.selectUserByUserUid(userId);
        assertThat(userOptional.isPresent()).isTrue();
        assertThat(userOptional.get().getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    void shouldNotSelectUserByRandomUserUid() {
        Optional<User> user = fakeDataDao.selectUserByUserUid(UUID.randomUUID());
        assertThat(user.isPresent()).isFalse();
    }

    @Test
    void shouldUpdateUser() {
        UUID userId = fakeDataDao.selectAllUsers().get(0).getUserId();
        User user = new User(userId, "Laksmi", "Daniella",
                User.Gender.FEMALE, 22, "laksmi@email.com");
        fakeDataDao.updateUser(user);

        Optional<User> optionalUser = fakeDataDao.selectUserByUserUid(userId);
        assertThat(optionalUser.isPresent()).isTrue();

        assertThat(fakeDataDao.selectAllUsers()).hasSize(1);
        assertThat(optionalUser.get().getAge()).isEqualTo(22);
        assertThat(optionalUser.get().getFirstName()).isEqualTo("Laksmi");
        assertThat(optionalUser.get().getLastName()).isEqualTo("Daniella");
        assertThat(optionalUser.get().getGender()).isEqualTo(User.Gender.FEMALE);
        assertThat(optionalUser.get().getEmail()).isEqualTo("laksmi@email.com");
        assertThat(optionalUser.get().getUserId()).isNotNull();
    }

    @Test
    void shouldDeleteUserByUserUid() {
        UUID userId = fakeDataDao.selectAllUsers().get(0).getUserId();

        fakeDataDao.deleteUserByUserUid(userId);

        assertThat(fakeDataDao.selectUserByUserUid(userId).isPresent()).isFalse();
        assertThat(fakeDataDao.selectAllUsers()).isEmpty();
    }
}