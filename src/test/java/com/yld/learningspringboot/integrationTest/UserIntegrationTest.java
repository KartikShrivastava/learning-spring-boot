package com.yld.learningspringboot.integrationTest;

import com.yld.learningspringboot.clientproxy.UserResourceV1;
import com.yld.learningspringboot.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.ws.rs.NotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import java.util.UUID;

// Spring test by default uses -1 port for mocking tests, so we have to explicitly specify DEFINED_PORT in annotation
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserIntegrationTest {

	// -------------------------- INTEGRATION TESTS using Resteasy Client proxy api --------------------------
	// -------------------------- USES: UserResourceV1 class as proxy interface     --------------------------

	// Auto wire UserResourceV1 proxy interface bean initialized by Reasteasy Client api in ClientProxyConfig
	// TODO: Doubt: How we can autowire an interface instance which is never initialized by a class implementing it?

	@Autowired
	private UserResourceV1 userResourceV1;

	@Test
	void shouldInsertUser() {
		// BDD (Behaviour driven development) style testing

		// Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "Yayi", "Daniella",
				User.Gender.FEMALE, 22, "yayi@gmail.com");

		// When
		userResourceV1.insertNewUser(user);

		// Then
		User responseUser = userResourceV1.fetchUser(user.getUserId());
		assertUserFields(user, responseUser);
	}

	@Test
	void shouldDeleteUser() {
		// Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "Yayi", "Daniella",
				User.Gender.FEMALE, 22, "yayi@gmail.com");

		// When
		userResourceV1.insertNewUser(user);

		// Then
		User responseUser = userResourceV1.fetchUser(user.getUserId());
		assertUserFields(user, responseUser);

		// When
		userResourceV1.deleteUser(userUid);

		// Then
		assertThatThrownBy(() -> userResourceV1.fetchUser(userUid))
				.isInstanceOf(NotFoundException.class);
	}

	@Test
	void shouldUpdateUser() {
		// Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "Yayi", "Daniella",
				User.Gender.FEMALE, 22, "yayi@gmail.com");

		// When
		userResourceV1.insertNewUser(user);
		User updatedUser = new User(userUid, "Laksmi", "Daniella",
				User.Gender.FEMALE, 55, "yayi@gmail.com");
		userResourceV1.updateUser(updatedUser);

		// Then
		User responseUser = userResourceV1.fetchUser(updatedUser.getUserId());
		assertUserFields(updatedUser, responseUser);
	}

	@Test
	void shouldFetchUsersByGender() {
		// Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "Yayi", "Daniella",
				User.Gender.FEMALE, 22, "yayi@gmail.com");

		// When
		userResourceV1.insertNewUser(user);

		// Then
		List<User> males = userResourceV1.fetchUsers("male");
		assertThat(males).extracting("userId").doesNotContain(user.getUserId());
		assertThat(males).extracting("firstName").doesNotContain(user.getFirstName());
		assertThat(males).extracting("lastName").doesNotContain(user.getLastName());
		assertThat(males).extracting("age").doesNotContain(user.getAge());
		assertThat(males).extracting("email").doesNotContain(user.getEmail());

		List<User> females = userResourceV1.fetchUsers(User.Gender.FEMALE.name());
		assertThat(females).extracting("userId").contains(user.getUserId());
		assertThat(females).extracting("firstName").contains(user.getFirstName());
		assertThat(females).extracting("lastName").contains(user.getLastName());
		assertThat(females).extracting("age").contains(user.getAge());
		assertThat(females).extracting("email").contains(user.getEmail());
	}

	private static void assertUserFields(User user, User responseUser) {
		assertThat(user.getAge()).isEqualTo(responseUser.getAge());
		assertThat(user.getFirstName()).isEqualTo(responseUser.getFirstName());
		assertThat(user.getLastName()).isEqualTo(responseUser.getLastName());
		assertThat(user.getGender()).isEqualTo(responseUser.getGender());
		assertThat(user.getEmail()).isEqualTo(responseUser.getEmail());
		assertThat(user.getUserId()).isNotNull();
		assertThat(user.getUserId()).isInstanceOf(UUID.class);
		assertThat(user.getUserId()).isEqualTo(responseUser.getUserId());
	}
}
