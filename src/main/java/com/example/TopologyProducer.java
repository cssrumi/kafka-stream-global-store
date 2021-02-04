package com.example;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;

@ApplicationScoped
public class TopologyProducer {

    static final String STORE = "vtopic-store";
    private static final String TOPIC = "vtopic";

    @Produces
    public Topology topology() {
        StreamsBuilder builder = new StreamsBuilder();
        Serdes.StringSerde stringSerde = new Serdes.StringSerde();
        ObjectMapperSerde<Entity> entitySerde = new ObjectMapperSerde<>(Entity.class);
        KeyValueBytesStoreSupplier storeSupplier = Stores.inMemoryKeyValueStore(STORE);
//        GlobalKTable<String, Entity> entities = builder.globalTable(TOPIC, Consumed.with(stringSerde, entitySerde),
//                Materialized.<String, Entity>as(storeSupplier)
//                            .withKeySerde(stringSerde)
//                            .withValueSerde(entitySerde));
        builder.addGlobalStore(
                Stores.keyValueStoreBuilder(storeSupplier, stringSerde, entitySerde),
                TOPIC,
                Consumed.with(stringSerde, entitySerde),
                () -> new EntityProcessor(STORE)
        );

        return builder.build();
    }
}
