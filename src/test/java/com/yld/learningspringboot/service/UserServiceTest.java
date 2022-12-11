package com.yld.learningspringboot.service;

import com.yld.learningspringboot.dao.UserDao;
import com.yld.learningspringboot.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class UserServiceTest {

    // We will not instantiate object of FakeDataDao, rather we mock it
    @Mock
    private UserDao fakeDataDao;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(fakeDataDao);
    }

    @Test
    void shouldInsertUser() {
        User user = new User(null, "Laksmi", "Daniella",
                User.Gender.FEMALE, 22, "laksmi@email.com");

        // Using refEq instead of eq because we need to exclude checking userId in this test,
        // as it's initialized inside insertUser method
        given(fakeDataDao.insertUser(any(UUID.class), refEq(user, "userId"))).willReturn(1);

        ArgumentCaptor<User> captureUser = ArgumentCaptor.forClass(User.class);

        int insertResult = userService.insertUser(user);

        verify(fakeDataDao).insertUser(any(UUID.class), captureUser.capture());

        User capturedUser = captureUser.getValue();

        assertThat(insertResult).isEqualTo(1);
        assertUserFields(capturedUser);
    }

    @Test
    void shouldGetAllUsers() {
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, "Laksmi", "Daniella",
                User.Gender.FEMALE, 22, "laksmi@email.com");

        List<User> users = List.of(user);
        users = Collections.unmodifiableList(users);

        // mocking fakeDataDao with a new user
        // this will execute whenever we make a call to fakeDataDao.selectAllUsers from the object
        // of its dependent class UserService
        given(fakeDataDao.selectAllUsers()).willReturn(users);

        // finally check the values one by one
        List<User> allUsers = userService.getAllUsers(Optional.empty());
        assertThat(allUsers).hasSize(1);
        assertUserFields(allUsers.get(0));
    }

    @Test
    void shouldGetAllUsersByGender() {
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, "Laksmi", "Daniella",
                User.Gender.FEMALE, 22, "laksmi@email.com");


        UUID uuid2 = UUID.randomUUID();
        User user2 = new User(uuid2, "Yayi", "Patrik",
                User.Gender.MALE, 25, "yayi@email.com");

        List<User> users = List.of(user, user2);
        users = Collections.unmodifiableList(users);

        given(fakeDataDao.selectAllUsers()).willReturn(users);

        List<User> filteredUsers2 = userService.getAllUsers(Optional.of("MALE"));
        assertThat(filteredUsers2).hasSize(1);
        assertUser2Fields(filteredUsers2.get(0));
    }

    @Test
    void shouldThrowExceptionOnGetAllUsersInvalidGender() {
        assertThatThrownBy(() -> userService.getAllUsers(Optional.of("BlahBlah")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid gender");
    }

    @Test
    void shouldGetUser() {
        UUID userUid = UUID.randomUUID();
        User user = new User(userUid, "Laksmi", "Daniella",
                User.Gender.FEMALE, 22, "laksmi@email.com");

        given(fakeDataDao.selectUserByUserUid(userUid)).willReturn(Optional.of(user));

        Optional<User> userOptional = userService.getUser(userUid);
        assertThat(userOptional.isPresent()).isTrue();
        assertUserFields(userOptional.get());
    }

    @Test
    void shouldUpdateUser() {
        UUID userUid = UUID.randomUUID();
        User user = new User(userUid, "Laksmi", "Daniella",
                User.Gender.FEMALE, 22, "laksmi@email.com");

        // mock calls made to fakeDataDao methods by userService.updateUser(...)
        given(fakeDataDao.selectUserByUserUid(userUid)).willReturn(Optional.of(user));
        given(fakeDataDao.updateUser(user)).willReturn(1);

        // useful in capturing Object passed in mock
        // Note the init before call to userService.updateUser
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        int updateResult = userService.updateUser(user);

        // mockito "verify" checks if a mock method is called or not
        verify(fakeDataDao).selectUserByUserUid(userUid);
        // check if the user which we have passed to userService.updateUser
        // is same as what is later passed to fakeDataDao.updateUser
        verify(fakeDataDao).updateUser(captor.capture());

        User capturedUser = captor.getValue();

        assertThat(updateResult).isEqualTo(1);
        assertUserFields(capturedUser);
    }

    @Test
    void shouldRemoveUser() {
        UUID userUid = UUID.randomUUID();
        User user = new User(userUid, "Laksmi", "Daniella",
                User.Gender.FEMALE, 22, "laksmi@email.com");

        given(fakeDataDao.deleteUserByUserUid(userUid)).willReturn(1);
        given(fakeDataDao.selectUserByUserUid(userUid)).willReturn(Optional.of(user));
        ArgumentCaptor<UUID> resultUidCaptor = ArgumentCaptor.forClass(UUID.class);

        int removeResult = userService.removeUser(userUid);

        verify(fakeDataDao).selectUserByUserUid(userUid);
        // for checking if method is invoked
        verify(fakeDataDao).deleteUserByUserUid(userUid);
        // for checking if argument passed is correct/desired one
        verify(fakeDataDao).deleteUserByUserUid(resultUidCaptor.capture());

        assertThat(removeResult).isEqualTo(1);
        assertThat(resultUidCaptor.getValue()).isEqualTo(userUid);
    }

    private static void assertUserFields(User user) {
        assertThat(user.getAge()).isEqualTo(22);
        assertThat(user.getFirstName()).isEqualTo("Laksmi");
        assertThat(user.getLastName()).isEqualTo("Daniella");
        assertThat(user.getGender()).isEqualTo(User.Gender.FEMALE);
        assertThat(user.getEmail()).isEqualTo("laksmi@email.com");
        assertThat(user.getUserId()).isNotNull();
        assertThat(user.getUserId()).isInstanceOf(UUID.class);
    }

    private static void assertUser2Fields(User user) {
        assertThat(user.getAge()).isEqualTo(25);
        assertThat(user.getFirstName()).isEqualTo("Yayi");
        assertThat(user.getLastName()).isEqualTo("Patrik");
        assertThat(user.getGender()).isEqualTo(User.Gender.MALE);
        assertThat(user.getEmail()).isEqualTo("yayi@email.com");
        assertThat(user.getUserId()).isNotNull();
        assertThat(user.getUserId()).isInstanceOf(UUID.class);
    }
}