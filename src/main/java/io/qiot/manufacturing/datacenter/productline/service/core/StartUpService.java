package io.qiot.manufacturing.datacenter.productline.service.core;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.plugins.providers.DocumentProvider;
import org.jboss.resteasy.plugins.providers.FormUrlEncodedProvider;
import org.jboss.resteasy.plugins.providers.IIOImageProvider;
import org.jboss.resteasy.plugins.providers.JaxrsFormProvider;
import org.jboss.resteasy.plugins.providers.SourceProvider;
import org.jboss.resteasy.plugins.providers.sse.SseEventProvider;
import org.slf4j.Logger;

import io.qiot.manufacturing.datacenter.commons.domain.productline.GlobalProductLineDTO;
import io.qiot.manufacturing.datacenter.commons.util.producer.SampleGlobalProductLineProducer;
import io.qiot.manufacturing.datacenter.productline.domain.event.NewGlobalProductLineEventDTO;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author andreabattaglia
 *
 */
@ApplicationScoped
@RegisterForReflection(targets = { IIOImageProvider.class,
        SseEventProvider.class, FormUrlEncodedProvider.class,
        SourceProvider.class, DocumentProvider.class, JaxrsFormProvider.class })

public class StartUpService {
    @Inject
    Logger LOGGER;
    
    @ConfigProperty(name = "qiot.productline.generate")
    boolean generateRandomProductline;

    @Inject
    SampleGlobalProductLineProducer sampleGlobalProductLineProducer;

    @Inject
    Event<NewGlobalProductLineEventDTO> newProductLineEvent;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.debug("The application is starting...");
        if (generateRandomProductline) {
            GlobalProductLineDTO globalProductLine = sampleGlobalProductLineProducer
                    .generate();
            NewGlobalProductLineEventDTO newProductLineEventDTO = new NewGlobalProductLineEventDTO();
            newProductLineEventDTO.productLine = globalProductLine;
            newProductLineEvent.fire(newProductLineEventDTO);
        }
        LOGGER.debug("Application bootstrap completed...");
    }
}
