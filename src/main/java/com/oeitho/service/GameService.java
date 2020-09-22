package com.oeitho.service;

import java.security.SecureRandom;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oeitho.exception.GameNotFoundException;

import org.infinispan.client.hotrod.RemoteCache;

import io.quarkus.infinispan.client.Remote;

@ApplicationScoped
public class GameService {

    SecureRandom random = new SecureRandom();
    ObjectMapper mapper = new ObjectMapper();

    @Inject
    @Remote("GameCache")
    RemoteCache<Integer, String> gameCache;

    public Integer createGame(final Map<String, Object> gameData) throws JsonProcessingException {
        final Integer gameId = random.nextInt(10000000);
        final String json = mapper.writeValueAsString(gameData);
        gameCache.put(gameId, json);
        return gameId;
    }

    public Map<String, Object> getGame(final Integer gameId) throws GameNotFoundException, JsonProcessingException {
        final String json = gameCache.get(gameId);
        if (json == null) {
            throw new GameNotFoundException("Game with id " + gameId + " not found!");
        }
        final Map<String, Object> game = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        return game;
    }

}
