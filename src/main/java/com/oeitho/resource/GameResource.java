package com.oeitho.resource;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oeitho.exception.GameNotFoundException;
import com.oeitho.service.GameService;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/game")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GameResource {

    @Inject
    GameService gameService;

    @POST
    public Response createGame(Map<String, Object> gameData) {
        try {
            Integer gameId = gameService.createGame(gameData);
            return Response
                .status(Response.Status.OK)
                .header("gameId", gameId)
                .build();
        } catch (JsonProcessingException exception) {
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
        }    
    }

    @Path("/{gameId}")
    @GET
    public Response getGame(@PathParam Integer gameId) {
        try {
            Map<String, Object> game = gameService.getGame(gameId);
            return Response
                .status(Response.Status.OK)
                .entity(game)
                .build();
        } catch (GameNotFoundException exception) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        } catch (JsonProcessingException exception) {
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
        }
    }

}
