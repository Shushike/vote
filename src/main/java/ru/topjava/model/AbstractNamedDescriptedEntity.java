package ru.topjava.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;


@MappedSuperclass
public abstract class AbstractNamedDescriptedEntity extends AbstractNamedEntity {

    @Size(min = 2, max = 120)
    @Column(name = "description", nullable = true)
    protected String description;

    protected AbstractNamedDescriptedEntity() {
    }

    protected AbstractNamedDescriptedEntity(Integer id, String name, String description) {
        super(id, name);
        this.description = description;
    }

    public void setDescription(String name) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return super.toString() + (description!=null?" - "+description:"");
    }
}