package io.qiot.manufacturing.datacenter.productline.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import io.qiot.manufacturing.datacenter.productline.domain.persistence.GlobalProductLineBean;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;

/**
 * @author andreabattaglia
 *
 */
@ApplicationScoped
public class GlobalProductLineRepository
        implements PanacheMongoRepositoryBase<GlobalProductLineBean, String> {

    @Inject
    Logger LOGGER;
}
