package com.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

@Path("/store")
public class StoreResource {

    private final ReadOnlyKeyValueStore<String, Entity> store;

    public StoreResource(KafkaStreams streams) {
        this.store = streams.store(StoreQueryParameters.fromNameAndType(TopologyProducer.STORE, QueryableStoreTypes.keyValueStore()));
    }

    @GET
    public Response getAll() {
        return Response.ok(store.all()).build();
    }
}
