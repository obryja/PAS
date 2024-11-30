package org.example.rest.controllers;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.rest.dto.UserCuDTO;
import org.example.rest.dto.UserGetDTO;
import org.example.rest.mappers.UserMapper;
import org.example.rest.models.Admin;
import org.example.rest.models.Client;
import org.example.rest.models.Manager;
import org.example.rest.models.User;
import org.example.rest.services.UserService;

import java.util.List;

@Path("/api/users")
public class UserController {

    @Inject
    UserService userService;

    UserMapper userMapper = new UserMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserGetDTO> userGetDTOs = users.stream()
                .map(userMapper::userToUserGetDTO)
                .toList();
        return Response.ok(userGetDTOs).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") String id) {
        User user = userService.getUserById(id);
        return Response.ok(userMapper.userToUserGetDTO(user)).build();
    }

    @POST
    @Path("/client")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createClient(@Valid UserCuDTO user) {
        User newUser = new Client(user.getUsername(), user.getPassword(), true);
        User createdUser = userService.createUser(newUser);
        return Response.status(Response.Status.CREATED).entity(createdUser).build();
    }

    @POST
    @Path("/manager")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createManager(@Valid UserCuDTO user) {
        User newUser = new Manager(user.getUsername(), user.getPassword(), true);
        User createdUser = userService.createUser(newUser);
        return Response.status(Response.Status.CREATED).entity(createdUser).build();
    }

    @POST
    @Path("/admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAdmin(@Valid UserCuDTO user) {
        User newUser = new Admin(user.getUsername(), user.getPassword(), true);
        User createdUser = userService.createUser(newUser);
        return Response.status(Response.Status.CREATED).entity(createdUser).build();
    }

    @GET
    @Path("/username/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByUsername(@PathParam("username") String username) {
        User user = userService.getUserByUsername(username);
        return Response.ok(userMapper.userToUserGetDTO(user)).build();
    }

    @GET
    @Path("/username")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUserByUsername(@QueryParam("username") String username) {
        List<User> users = userService.getUsersByUsername(username);
        List<UserGetDTO> userGetDTOs = users.stream()
                .map(userMapper::userToUserGetDTO)
                .toList();
        return Response.ok(userGetDTOs).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") String id, @Valid UserCuDTO user) {
        User updatedUser = userService.updateUser(id, user.getUsername(), user.getPassword());
        return Response.ok(updatedUser).build();
    }

    @POST
    @Path("/{id}/activate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateUser(@PathParam("id") String id) {
        User user = userService.activateUser(id);
        return Response.ok(userMapper.userToUserGetDTO(user)).build();
    }

    @POST
    @Path("/{id}/deactivate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateUser(@PathParam("id") String id) {
        User user = userService.deactivateUser(id);
        return Response.ok(userMapper.userToUserGetDTO(user)).build();
    }
}
