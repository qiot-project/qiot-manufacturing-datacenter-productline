package io.qiot.manufacturing.datacenter.productline.domain.event;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author andreabattaglia
 *
 */
@RegisterForReflection
public class LatestProductLineRequestedEventDTO {
    public String machineryId;
}
