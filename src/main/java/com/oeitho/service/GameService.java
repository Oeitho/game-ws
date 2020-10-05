package com.oeitho.service;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oeitho.exception.GameNotFoundException;
import com.oeitho.utils.RandomString;

import org.infinispan.client.hotrod.RemoteCache;

import io.quarkus.infinispan.client.Remote;

@ApplicationScoped
public class GameService {

    RandomString randomString = new RandomString();
    ObjectMapper mapper = new ObjectMapper();

    @Inject
    @Remote("GameCache")
    RemoteCache<String, String> gameCache;

    public String createGame(final Map<String, Object> gameData) throws JsonProcessingException {
        final String gameId = randomString.randomString(RandomString.UPPER_CASE_ALPHANUMERIC, 9);
        final String json = mapper.writeValueAsString(gameData);
        gameCache.put(gameId, json);
        return gameId;
    }

    public Map<String, Object> getGame(final String gameId) throws GameNotFoundException, JsonProcessingException {
        final String json = gameCache.get(gameId);
        if (json == null) {
            throw new GameNotFoundException("Game with id " + gameId + " not found!");
        }
        final Map<String, Object> game = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        return game;
    }

}
