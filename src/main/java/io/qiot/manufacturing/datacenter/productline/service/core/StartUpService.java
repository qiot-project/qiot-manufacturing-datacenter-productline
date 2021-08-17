package io.qiot.manufacturing.datacenter.productline.service.core;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;

import io.qiot.manufacturing.commons.util.producer.SampleGlobalProductLineProducer;
import io.qiot.manufacturing.datacenter.commons.domain.productline.GlobalProductLineDTO;
import io.qiot.manufacturing.datacenter.productline.domain.event.NewGlobalProductLineEventDTO;
import io.quarkus.runtime.StartupEvent;

/**
 * @author andreabattaglia
 *
 */
@ApplicationScoped
public class StartUpService {
    @Inject
    Logger LOGGER;

    @Inject
    SampleGlobalProductLineProducer sampleGlobalProductLineProducer;

    @Inject
    Event<NewGlobalProductLineEventDTO> newProductLineEvent;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.debug("The application is starting...");
        GlobalProductLineDTO globalProductLine = sampleGlobalProductLineProducer
                .generate();
        NewGlobalProductLineEventDTO newProductLineEventDTO = new NewGlobalProductLineEventDTO();
        newProductLineEventDTO.productLine = globalProductLine;
        newProductLineEvent.fire(newProductLineEventDTO);
        LOGGER.debug("Application bootstrap completed...");
    }
}
