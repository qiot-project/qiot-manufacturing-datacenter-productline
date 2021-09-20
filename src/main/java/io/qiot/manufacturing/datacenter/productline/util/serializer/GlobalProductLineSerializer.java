package io.qiot.manufacturing.datacenter.productline.util.serializer;

import io.qiot.manufacturing.datacenter.commons.domain.productline.GlobalProductLineDTO;
import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;

/**
 * There is an existing ObjectMapperSerializer that can be used to serialize all
 * data objects via Jackson. You may create an empty subclass if you want to use
 * Serializer/deserializer autodetection.<br>
 * <br>
 * The corresponding deserializer class needs to be subclassed.
 * 
 * @author andreabattaglia
 *
 */
public class GlobalProductLineSerializer
        extends ObjectMapperSerializer<GlobalProductLineDTO> {
}
