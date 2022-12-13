package com.yld.learningspringboot.resource;

import com.yld.learningspringboot.model.User;
import com.yld.learningspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// RESTFUL api layer or controller layer or resource layer of n-tier architecture using RestEasy
// Rest-easy follows JAX-RS RESTFUL API spec
@Component
@Path("api/v1/users")
public class UserResourceResteasy {

    private UserService userService;

    @Autowired
    UserResourceResteasy(UserService userService){
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> fetchUsers(@QueryParam("gender") String gender) {
        return userService.getAllUsers(Optional.ofNullable(gender));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{userUidFromPath}")
    public Response fetchUser(@PathParam("userUidFromPath") UUID userUidVar) {
        Optional<User> userOptional = userService.getUser(userUidVar);
        if(userOptional.isPresent()) {
            return Response.ok(userOptional.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorMessage(("user " + userUidVar + " was not found.")))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertNewUser(User user) {
        int result = userService.insertUser(user);
        return getIntegerResponseEntity(result);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(User user) {
        int result = userService.updateUser(user);
        return getIntegerResponseEntity(result);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{userUid}")
    public Response deleteUser(@PathParam("userUid") UUID uuid) {
        int result = userService.removeUser(uuid);
        return getIntegerResponseEntity(result);
    }

    private static Response getIntegerResponseEntity(int result) {
        if(result == 1)
            return Response.ok().build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
