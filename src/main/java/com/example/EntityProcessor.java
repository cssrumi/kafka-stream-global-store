package com.example;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityProcessor implements Processor<String, Entity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityProcessor.class);

    private KeyValueStore<String, Entity> store;
    private final String storeName;

    public EntityProcessor(String storeName) {
        this.storeName = storeName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(ProcessorContext context) {
        store = (KeyValueStore) context.getStateStore(storeName);
    }

    @Override
    public void process(String key, Entity value) {
        if (key != null && value != null) {
            store.put(key, value);
            return;
        }
        if (value != null) {
            LOGGER.warn("Null key for entity with id: {} and name: {}", value.id, value.name);
            return;
        }
        LOGGER.warn("Null Key and Value");
    }

    @Override
    public void close() {
    }
}
