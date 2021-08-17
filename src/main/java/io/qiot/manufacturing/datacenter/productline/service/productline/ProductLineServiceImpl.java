package io.qiot.manufacturing.datacenter.productline.service.productline;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.qiot.manufacturing.datacenter.commons.domain.productline.GlobalProductLineDTO;
import io.qiot.manufacturing.datacenter.productline.domain.event.NewGlobalProductLineEventDTO;
import io.qiot.manufacturing.datacenter.productline.domain.persistence.GlobalProductLineBean;
import io.qiot.manufacturing.datacenter.productline.persistence.GlobalProductLineRepository;
import io.qiot.manufacturing.datacenter.productline.service.productline.producer.GlobalProductLineStreamProducer;
import io.qiot.manufacturing.datacenter.productline.util.converter.GlobalPLConverter;

/**
 * @author andreabattaglia
 *
 */
@ApplicationScoped
public class ProductLineServiceImpl implements ProductLineService {

    @Inject
    Logger LOGGER;

    @Inject
    ObjectMapper MAPPER;

    @Inject
    GlobalProductLineRepository globalProductLineRepository;

    @Inject
    GlobalPLConverter globalPLConverter;

    @Inject
    GlobalProductLineStreamProducer productLineStreamProducer;

    private ReadWriteLock readWriteLock;
    private final Lock readLock;
    private final Lock writeLock;

    public ProductLineServiceImpl() {
        this.readWriteLock = new ReentrantReadWriteLock();
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
    }

    void onNewProductLine(@Observes NewGlobalProductLineEventDTO event)
            throws Exception {
        GlobalProductLineDTO globalProductLineDTO = event.productLine;
        handleNewProductLine(globalProductLineDTO);
    }

    void handleNewProductLine(GlobalProductLineDTO globalProductLineDTO)
            throws Exception {
        LOGGER.debug("Handling a new product line\n{}",
                MAPPER.writeValueAsString(globalProductLineDTO));
        writeLock.lock();
        try {
            // save product line
            GlobalProductLineBean globalProductLineBean = globalPLConverter
                    .destToSource(globalProductLineDTO);
            globalProductLineRepository.persist(globalProductLineBean);
            LOGGER.debug("PERSISTED Product Line:\n{}", globalProductLineBean);

            productLineStreamProducer.process(globalProductLineDTO);
        } finally {
            writeLock.unlock();
        }

    }

}
