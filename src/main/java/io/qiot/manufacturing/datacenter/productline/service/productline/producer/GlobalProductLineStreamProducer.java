package io.qiot.manufacturing.datacenter.productline.service.productline.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.qiot.manufacturing.commons.domain.productline.GlobalProductLineDTO;

/**
 * @author andreabattaglia
 *
 */
@ApplicationScoped
public class GlobalProductLineStreamProducer {

    @Inject
    Logger LOGGER;
    
    @Inject
    ObjectMapper MAPPER;

    @Inject
    @Channel("productline")
    Emitter<String> productLineEmitter;

    public void process(GlobalProductLineDTO productLine) throws JsonProcessingException {
        LOGGER.debug("Sending out new PRODUCT LINE to the factories:",
                productLine);
        productLineEmitter.send(MAPPER.writeValueAsString(productLine));
    }
}
