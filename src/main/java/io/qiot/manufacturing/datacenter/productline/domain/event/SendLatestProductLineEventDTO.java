package io.qiot.manufacturing.datacenter.productline.domain.event;

import io.qiot.manufacturing.all.commons.domain.productline.ProductLineDTO;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author andreabattaglia
 *
 */
@RegisterForReflection
public class SendLatestProductLineEventDTO {
    public String machineryId;
    public ProductLineDTO productLine;
}
