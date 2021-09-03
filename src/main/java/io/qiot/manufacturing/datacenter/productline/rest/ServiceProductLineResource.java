package io.qiot.manufacturing.datacenter.productline.rest;

import java.time.Instant;
import java.util.UUID;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;

import io.qiot.manufacturing.all.commons.domain.productline.ColorRangesDTO;
import io.qiot.manufacturing.all.commons.domain.productline.PackagingRangesDTO;
import io.qiot.manufacturing.all.commons.domain.productline.PrintingRangesDTO;
import io.qiot.manufacturing.all.commons.domain.productline.SizeChartRangesDTO;
import io.qiot.manufacturing.datacenter.commons.domain.productline.GlobalProductLineDTO;
import io.qiot.manufacturing.datacenter.commons.domain.productline.MarginsDTO;
import io.qiot.manufacturing.datacenter.productline.domain.event.NewGlobalProductLineEventDTO;
import io.qiot.manufacturing.datacenter.productline.service.productline.ProductLineService;

/**
 * @author andreabattaglia
 *
 */
@Path("productline")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceProductLineResource {

    @Inject
    Logger LOGGER;

    @Inject
    ProductLineService productLineService;
    
    @Inject
    Event<NewGlobalProductLineEventDTO> event;
    
    @PUT
    @Path("/text")
    @Consumes(MediaType.TEXT_PLAIN)
    public void add(@QueryParam("cmin") Double cmin,
    		@QueryParam("cmax") Double cmax,
    		@QueryParam("smin") Double smin,
    		@QueryParam("smax") Double smax,
    		@QueryParam("bmin") Double bmin,
    		@QueryParam("bmax") Double bmax,
    		@QueryParam("wmin") Double wmin,
    		@QueryParam("wmax") Double wmax,
    		@QueryParam("hmin") Double hmin,
    		@QueryParam("hmax") Double hmax,
    		@QueryParam("redmin") int redmin,
    		@QueryParam("redmax") int redmax,
    		@QueryParam("greenmin") int greenmin,
    		@QueryParam("greenmax") int greenmax,
    		@QueryParam("bluemin") int bluemin,
    		@QueryParam("bluemax") int bluemax,
    		@QueryParam("pmin") Double pmin,
    		@QueryParam("pmax") Double pmax,
    		@QueryParam("pamin") Double pamin,
    		@QueryParam("pamax") Double pamax,
    		@QueryParam("weaving") Double weaving,
    		@QueryParam("coloring") int coloring,
    		@QueryParam("printing") Double printing,
    		@QueryParam("packaging") Double packaging   		
    		) {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("add(params) - start");
    	}
    	
    	SizeChartRangesDTO scr = new SizeChartRangesDTO();
    	scr.chestMin = cmin;
    	scr.chestMax = cmax;
    	scr.shoulderMin = smin;
    	scr.shoulderMax = smax;
    	scr.backMin = bmin;
    	scr.backMax = bmax;
    	scr.waistMin = wmin;
    	scr.waistMax = wmax;
    	scr.hipMin = hmin;
    	scr.hipMax = hmax;
    	
    	ColorRangesDTO cr = new ColorRangesDTO();
    	cr.redMin = redmin;
    	cr.redMax = redmax;
    	cr.greenMin = greenmin;
    	cr.greenMax = greenmax;
    	cr.blueMin = bluemin;
    	cr.blueMax = bluemax;
    	
    	PrintingRangesDTO pr = new PrintingRangesDTO();
    	pr.min = pmin;
    	pr.max = pmax;
    	
    	PackagingRangesDTO pa = new PackagingRangesDTO();
    	pa.min = pamin;
    	pa.max = pamax;
    	
    	MarginsDTO m = new MarginsDTO();
    	m.weaving = weaving;
    	m.coloring = coloring;
    	m.printing = printing;
    	m.packaging = packaging;
    	
    	GlobalProductLineDTO pl = new GlobalProductLineDTO();
    	
        pl.id = UUID.randomUUID();
        pl.sizeChart = scr;
        pl.color = cr;
        pl.print = pr;
        pl.packaging = pa;
        pl.createdOn = Instant.now();
        pl.margins = m;
        pl.active = true;
        
        NewGlobalProductLineEventDTO newGlobalProductLineEventDTO = new NewGlobalProductLineEventDTO();
        newGlobalProductLineEventDTO.productLine = pl;
        event.fire(newGlobalProductLineEventDTO);
    }
    
    @POST
    @Path("/object")
    @Consumes(MediaType.APPLICATION_JSON)
    public void add(GlobalProductLineDTO pl) {
    	
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("add(object) - start");
    	}

        NewGlobalProductLineEventDTO newGlobalProductLineEventDTO = new NewGlobalProductLineEventDTO();
        newGlobalProductLineEventDTO.productLine = pl;
        event.fire(newGlobalProductLineEventDTO);
    	
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("add(object) - finish");
    	}

    }
}