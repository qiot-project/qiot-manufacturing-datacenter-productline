package io.qiot.manufacturing.datacenter.productline.util.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import io.qiot.manufacturing.datacenter.commons.domain.productline.GlobalProductLineDTO;
import io.qiot.manufacturing.datacenter.productline.domain.persistence.GlobalProductLineBean;
import io.qiot.ubi.all.utilities.converter.DataObjectConverter;

@ApplicationScoped
public class GlobalPLConverter implements
        DataObjectConverter<GlobalProductLineBean, GlobalProductLineDTO> {

    @Override
    public GlobalProductLineDTO sourceToDest(GlobalProductLineBean src) {
        GlobalProductLineDTO dest = new GlobalProductLineDTO();
        // TODO: add UUID codec and switch to UUID type for field id
        dest.id = UUID.fromString(src.id);
        dest.sizeChart = src.sizeChart;
        dest.color = src.color;
        dest.print = src.print;
        dest.packaging = src.packaging;
        src.createdOn = dest.createdOn;
        src.margins = dest.margins;
        src.active = dest.active;
        return dest;
    }

    @Override
    public GlobalProductLineBean destToSource(GlobalProductLineDTO dest) {
        GlobalProductLineBean src = new GlobalProductLineBean();
        // TODO: add UUID codec and switch to UUID type for field id
        src.id = dest.id.toString();
        src.sizeChart = dest.sizeChart;
        src.color = dest.color;
        src.print = dest.print;
        src.packaging = dest.packaging;
        src.createdOn = dest.createdOn;
        src.margins = dest.margins;
        src.active = dest.active;
        return src;
    }

    @Override
    public List<GlobalProductLineDTO> allSourceToDest(
            List<GlobalProductLineBean> srcs) {
        if (Objects.isNull(srcs))
            return null;
        List<GlobalProductLineDTO> dests = new ArrayList<>();
        for (GlobalProductLineBean bean : srcs)
            dests.add(sourceToDest(bean));
        return dests;
    }

    @Override
    public List<GlobalProductLineBean> allDestToSource(
            List<GlobalProductLineDTO> dests) {
        if (Objects.isNull(dests))
            return null;
        List<GlobalProductLineBean> srcs = new ArrayList<>();
        for (GlobalProductLineDTO dest : dests)
            srcs.add(destToSource(dest));
        return srcs;
    }
}
