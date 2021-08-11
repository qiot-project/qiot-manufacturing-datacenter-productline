package io.qiot.manufacturing.datacenter.productline.domain.event;

import io.qiot.manufacturing.commons.domain.productline.GlobalProductLineDTO;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author andreabattaglia
 *
 */
@RegisterForReflection
public class NewGlobalProductLineEventDTO {
    public GlobalProductLineDTO productLine;
}
