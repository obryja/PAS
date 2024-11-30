package org.example.rest.controllers;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.rest.models.Rent;
import org.example.rest.services.RentService;

import java.util.List;

@Path("/api/rents")
public class RentController {

    @Inject
    RentService rentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRents() {
        List<Rent> rents = rentService.getAllRents();
        return Response.ok(rents).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRentById(@PathParam("id") String id) {
        Rent rent = rentService.getRentById(id);
        return Response.ok(rent).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRent(@Valid Rent rent) {
        Rent createdRent = rentService.createRent(rent);
        return Response.status(Response.Status.CREATED).entity(createdRent).build();
    }

    @GET
    @Path("/user/current/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentRentsByUser(@PathParam("id") String id) {
        List<Rent> rents = rentService.getCurrentRentsByUserId(id);
        return Response.ok(rents).build();
    }

    @GET
    @Path("/user/archive/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArchiveRentsByUser(@PathParam("id") String id) {
        List<Rent> rents = rentService.getArchiveRentsByUserId(id);
        return Response.ok(rents).build();
    }

    @GET
    @Path("/book/current/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentRentsByBook(@PathParam("id") String id) {
        List<Rent> rents = rentService.getCurrentRentsByBookId(id);
        return Response.ok(rents).build();
    }

    @GET
    @Path("/book/archive/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArchiveRentsByBook(@PathParam("id") String id) {
        List<Rent> rents = rentService.getArchiveRentsByBookId(id);
        return Response.ok(rents).build();
    }

    @POST
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response endRent(@PathParam("id") String id) {
        Rent rent = rentService.endRent(id);
        return Response.ok(rent).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRent(@PathParam("id") String id) {
        rentService.deleteRent(id);
        return Response.noContent().build();
    }
}
