package ru.topjava.model;

import org.hibernate.validator.constraints.SafeHtml;
import ru.topjava.View;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;


@MappedSuperclass
@SuppressWarnings("deprecation")
public abstract class AbstractNamedDescribedEntity extends AbstractNamedEntity {

    @Size(min = 2, max = 120)
    @Column(name = "description")
    @SafeHtml(groups = {View.Web.class}, whitelistType = SafeHtml.WhiteListType.NONE)
    protected String description;

    protected AbstractNamedDescribedEntity() {
    }

    protected AbstractNamedDescribedEntity(Integer id, String name, String description) {
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