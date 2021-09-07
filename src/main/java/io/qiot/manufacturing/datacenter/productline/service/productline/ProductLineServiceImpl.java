package io.qiot.manufacturing.datacenter.productline.service.productline;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.qiot.manufacturing.all.commons.domain.productline.ColorRangesDTO;
import io.qiot.manufacturing.all.commons.domain.productline.PackagingRangesDTO;
import io.qiot.manufacturing.all.commons.domain.productline.PrintingRangesDTO;
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
    			isPrintingRangesDTOValid(pl.print) &&
    			isPackagingRangesDTOValid(pl.packaging) &&
    			isMarginsDTOValid(pl.margins);
    }
    
    boolean isSizeChartRangesDTOValid(SizeChartRangesDTO ranges) {
        
        return validateMinMaxDoublePair(ranges.chestMin, ranges.chestMax) &&
        		validateMinMaxDoublePair(ranges.shoulderMin, ranges.shoulderMax) &&
        		validateMinMaxDoublePair(ranges.backMin, ranges.backMax) &&
        		validateMinMaxDoublePair(ranges.waistMin, ranges.waistMax) &&
        		validateMinMaxDoublePair(ranges.hipMin, ranges.hipMax);
    }
    
    boolean isColorRangesDTOValid(ColorRangesDTO ranges) {
        
        return validateMinMaxIntPair(ranges.redMin, ranges.redMax) &&
        		validateMinMaxIntPair(ranges.greenMin, ranges.greenMax) &&
        		validateMinMaxIntPair(ranges.blueMin, ranges.blueMax);
    	
    }
    
	boolean isPrintingRangesDTOValid(PrintingRangesDTO ranges) {
//	    public double min = 0;
//	    public double max = 1;
		return true;
	}
	
	boolean isPackagingRangesDTOValid(PackagingRangesDTO ranges) {
//	    public double min = 0;
//	    public double max = 1;
		return true;
	}
	
	boolean isMarginsDTOValid(MarginsDTO margins) {
	    
	    return margins.weaving > 0 &&
	    		margins.coloring > 0 &&
	    		margins.printing > 0 &&
	    		margins.packaging > 0;
	}

    boolean validateMinMaxDoublePair(double min, double max) {
    	return min < max &&
    			min > 0 &&
    			max > 0;
    }
    
    boolean validateMinMaxIntPair(int min, int max) {
    	return min < max &&
    			min > 0 &&
    			max > 0;
    }
}
