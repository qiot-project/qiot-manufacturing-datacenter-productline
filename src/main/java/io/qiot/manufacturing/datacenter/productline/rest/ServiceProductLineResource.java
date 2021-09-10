package io.qiot.manufacturing.datacenter.productline.rest;

import java.time.Instant;
import java.util.UUID;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import io.qiot.manufacturing.datacenter.commons.domain.productline.GlobalProductLineDTO;
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
      
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(GlobalProductLineDTO pl) {
		
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("add(object) - start");
    	}

    	if (productLineService.validateProductLine(pl)) {
    		
	        NewGlobalProductLineEventDTO newGlobalProductLineEventDTO = new NewGlobalProductLineEventDTO();
	        newGlobalProductLineEventDTO.productLine = pl;
	        event.fire(newGlobalProductLineEventDTO);
	        
	    	if (LOGGER.isDebugEnabled()) {
	    		LOGGER.debug("add(object) - finish");
	    	}
	        
			return Response.status(201).entity(pl).build();
    	} else {
    		return Response.status(400, "Invalid Productline supplied").build();
    	}
    	

    }
}