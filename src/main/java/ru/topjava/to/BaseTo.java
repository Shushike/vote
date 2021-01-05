package ru.topjava.to;

import io.swagger.annotations.ApiModelProperty;
import ru.topjava.HasId;

public abstract class BaseTo implements HasId {
    @ApiModelProperty(notes = "Object identifier (ID)")
    protected Integer id;

    public BaseTo() {
    }
    public BaseTo(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
