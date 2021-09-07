package io.qiot.manufacturing.datacenter.productline.service.productline;

import io.qiot.manufacturing.datacenter.commons.domain.productline.GlobalProductLineDTO;

/**
 * @author andreabattaglia
 *
 */
public interface ProductLineService {
	public boolean validateProductLine(GlobalProductLineDTO pl);
}
