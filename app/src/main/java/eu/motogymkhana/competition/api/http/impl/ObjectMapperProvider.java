package eu.motogymkhana.competition.api.http.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Provider;

/**
 * Provider for jackson object mapper so we can inject the mapper.
 *
 * @author christine
 */
public class ObjectMapperProvider implements Provider<ObjectMapper> {

    private static ObjectMapper mapper = new ObjectMapper() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
    };

    @Override
    public ObjectMapper get() {

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        return mapper;
    }
}
