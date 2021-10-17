package io.qiot.manufacturing.datacenter.productline.service.productline;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.qiot.manufacturing.all.commons.domain.productline.ColorRangesDTO;
import io.qiot.manufacturing.all.commons.domain.productline.SizeChartRangesDTO;
import io.qiot.manufacturing.datacenter.commons.domain.productline.GlobalProductLineDTO;
import io.qiot.manufacturing.datacenter.commons.domain.productline.MarginsDTO;
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
		globalProductLineDTO.active = true;		
		globalProductLineDTO.id = UUID.randomUUID();
		globalProductLineDTO.createdOn = Instant.now();
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
    
    public boolean validateProductLine(GlobalProductLineDTO pl) {
    	return isSizeChartRangesDTOValid(pl.sizeChart) &&
    			isColorRangesDTOValid(pl.color) &&
    			isPrintingRangesDTOValid(pl.print.min, pl.print.max) &&
    			isPackagingRangesDTOValid(pl.packaging.min, pl.packaging.max) &&
    			isMarginsDTOValid(pl.margins);
    }
    
    boolean isSizeChartRangesDTOValid(SizeChartRangesDTO ranges) {
        
        return validateMinMaxChartPair(ranges.chestMin, ranges.chestMax) &&
        		validateMinMaxChartPair(ranges.shoulderMin, ranges.shoulderMax) &&
        		validateMinMaxChartPair(ranges.backMin, ranges.backMax) &&
        		validateMinMaxChartPair(ranges.waistMin, ranges.waistMax) &&
        		validateMinMaxChartPair(ranges.hipMin, ranges.hipMax);
    }
    
    boolean isColorRangesDTOValid(ColorRangesDTO ranges) {
        
        return validateMinMaxColorPair(ranges.redMin, ranges.redMax) &&
        		validateMinMaxColorPair(ranges.greenMin, ranges.greenMax) &&
        		validateMinMaxColorPair(ranges.blueMin, ranges.blueMax);
    	
    }
    
	boolean isPrintingRangesDTOValid(double min, double max) {
    	return (min >= 0 && min < max && max <= 1);
	}
	
	boolean isPackagingRangesDTOValid(double min, double max) {
    	return (min >= 0 && min < max && max <= 1);
	}
	
	boolean isMarginsDTOValid(MarginsDTO margins) {
	    
	    return margins.weaving > 0 &&
	    		margins.coloring > 0 &&
	    		margins.printing > 0 &&
	    		margins.packaging > 0;
	}

    boolean validateMinMaxChartPair(double min, double max) {
    	return (min >= 0 && min < max);
    }
    
    boolean validateMinMaxColorPair(int min, int max) {
    	return (min >= 0 && min < max && max <= 255);
    }
}
