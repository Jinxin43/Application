package com.example.event.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import com.example.event.model.FenUploadId;
import org.xutils.db.annotation.Column;

@Entity(tableName = "fenUploadEntity", indices = {
        @Index(value = "Id")
})
public class FenUploadEntity implements FenUploadId {
    @Column(name = "landOrder", isId = true)
    private String landOrder;
    @Column(name = "Id")
    private String Id;
    @Column(name = "treeId")
    private String treeId;
    @Column(name = "messAgeId")
    private String messAgeId;

    @Override
    public String getLandOrder() {
        return landOrder;
    }

    @Override
    public void setLandOrder(String landOrder) {
        this.landOrder = landOrder;
    }

    @Override
    public String getId() {
        return Id;
    }

    @Override
    public void setId(String id) {
        this.Id=id;
    }

    @Override
    public String getTreeId() {
        return treeId;
    }

    @Override
    public void setTreeId(String treeId) {
          this.treeId=treeId;
    }

    @Override
    public String getMessAgeId() {
        return messAgeId;
    }

    @Override
    public void setMessAgeId(String messAgeId) {
          this.messAgeId=messAgeId;
    }
}
