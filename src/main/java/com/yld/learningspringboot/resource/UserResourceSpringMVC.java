package com.yld.learningspringboot.resource;

import com.yld.learningspringboot.model.User;
import com.yld.learningspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// RESTful api layer or controller layer or resource layer of n-tier architecture using Spring MVC
// This uses Spring MVC restful api, which does not follow JAX-RS spec
// Note: Check out other api like REST-Easy for JAX-RS based spec
// FIXME: Uncomment below annotations [to use]/[before using] Spring MVC for REST purpose

//@RestController
//@RequestMapping(
//        path = "/api/v1/users"
//)
public class UserResourceSpringMVC {

    private UserService userService;

    @Autowired
    public UserResourceSpringMVC(UserService userService) {
        this.userService = userService;
    }

    // TODO: Update query logic to support real sql database
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<User> fetchUsers(@QueryParam("gender") String gender) {
        return userService.getAllUsers(Optional.ofNullable(gender));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "{userUidFromPath}"
    )
    public ResponseEntity<?> fetchUser(@PathVariable("userUidFromPath") UUID userUidVar) {
        Optional<User> userOptional = userService.getUser(userUidVar);
        if(userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(("user " + userUidVar + " was not found.")));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            // to enforce client to only send json body requests
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Integer> insertNewUser(@RequestBody User user) {
        int result = userService.insertUser(user);
        return getIntegerResponseEntity(result);
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Integer> updateUser(@RequestBody User user) {
        int result = userService.updateUser(user);
        return getIntegerResponseEntity(result);
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "{userUid}"
    )
    public ResponseEntity<Integer> deleteUser(@PathVariable("userUid") UUID uuid) {
        int result = userService.removeUser(uuid);
        return getIntegerResponseEntity(result);
    }

    private static ResponseEntity<Integer> getIntegerResponseEntity(int result) {
        if(result == 1)
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }
}
