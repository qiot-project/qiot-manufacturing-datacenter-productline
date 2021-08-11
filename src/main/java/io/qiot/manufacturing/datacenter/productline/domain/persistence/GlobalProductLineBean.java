package io.qiot.manufacturing.datacenter.productline.domain.persistence;

import java.time.Instant;
import java.util.Objects;

import javax.persistence.Cacheable;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import io.qiot.manufacturing.commons.domain.productline.ColorRangesDTO;
import io.qiot.manufacturing.commons.domain.productline.MarginsDTO;
import io.qiot.manufacturing.commons.domain.productline.PackagingRangesDTO;
import io.qiot.manufacturing.commons.domain.productline.PrintingRangesDTO;
import io.qiot.manufacturing.commons.domain.productline.SizeChartRangesDTO;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

@MongoEntity(collection = "product_line_global")
@Cacheable
@RegisterForReflection
public class GlobalProductLineBean {
    // TODO: add UUID codec and switch to UUID type for field id
    @BsonId
    public String id;
    @BsonProperty(value = "created_on")
    public Instant createdOn;
    @BsonProperty(value = "active")
    public boolean active;
    @BsonProperty(value = "size_chart")
    public SizeChartRangesDTO sizeChart;
    @BsonProperty(value = "color")
    public ColorRangesDTO color;
    @BsonProperty(value = "print")
    public PrintingRangesDTO print;
    @BsonProperty(value = "packaging")
    public PackagingRangesDTO packaging;
    @BsonProperty(value = "margins")
    public MarginsDTO margins;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GlobalProductLineBean other = (GlobalProductLineBean) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ProductLineGlobalBean [id=");
        builder.append(id);
        builder.append(", createdOn=");
        builder.append(createdOn);
        builder.append(", active=");
        builder.append(active);
        builder.append(", sizeChart=");
        builder.append(sizeChart);
        builder.append(", color=");
        builder.append(color);
        builder.append(", print=");
        builder.append(print);
        builder.append(", packaging=");
        builder.append(packaging);
        builder.append(", margins=");
        builder.append(margins);
        builder.append("]");
        return builder.toString();
    }

}