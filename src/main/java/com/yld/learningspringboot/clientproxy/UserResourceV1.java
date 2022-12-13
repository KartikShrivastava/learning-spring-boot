package com.yld.learningspringboot.clientproxy;

import com.yld.learningspringboot.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

// Resteasy Client API works this way, by creating an interface/proxy which
// mirrors out RESTful web service
// In our case currently we are mirroring Resteasy based web resource, but it
// should also work for Spring MVC based web resource
public interface UserResourceV1 {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<User> fetchUsers(@QueryParam("gender") String gender);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{userUidFromPath}")
    User fetchUser(@PathParam("userUidFromPath") UUID userUidVar);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void insertNewUser(User user);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void updateUser(User user);

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{userUid}")
    void deleteUser(@PathParam("userUid") UUID uuid);
}
