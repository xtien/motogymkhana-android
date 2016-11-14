package eu.motogymkhana.competition.api.http.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.security.PublicKey;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Provider for jackson object mapper so we can inject the mapper.
 *
 * @author christine
 */
public class ObjectMapperProvider implements Provider<ObjectMapper> {

    private ObjectMapper mapper;

    @Inject
    @Singleton
    public ObjectMapperProvider() {

        mapper = new ObjectMapper() {

            private static final long serialVersionUID = 1L;

            {
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            }
        };

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Override
    public ObjectMapper get() {
        return mapper;
    }
}
