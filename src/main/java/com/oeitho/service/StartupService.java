package com.oeitho.service;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.configuration.XMLStringConfiguration;

import io.quarkus.runtime.StartupEvent;

public class StartupService {

    @Inject
    RemoteCacheManager remoteCacheManager;

    private final Integer MAX_LIFESPAN_MILLISECONDS = 12 * 60 * 60 * 1000;

    private final String CACHE_XML = "<infinispan><cache-container>"
                                       + "<local-cache name=\"GameCache\">"
                                           + "<encoding media-type=\"application/x-protostream\"/>"
                                           + "<expiration lifespan=\"" + MAX_LIFESPAN_MILLISECONDS + "\" interval=\"1000\"/>"
                                       + "</local-cache>"
                                   + "</cache-container></infinispan>";

    public void onStart(@Observes StartupEvent event) {            
        remoteCacheManager.administration().getOrCreateCache("GameCache", new XMLStringConfiguration(CACHE_XML));
    }

}