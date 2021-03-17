package com.example.event.db.xEntity;
import com.example.event.model.FenUploadId;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
@Table(name = "fenUploadEntity")
public class FenUploadEntity implements FenUploadId {
    @Column(name = "Id",isId = true)
    private String Id;
    @Column(name = "landOrder" )
    private String landOrder;
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
        this.landOrder=landOrder;
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

    public String getMessAgeId() {
        return messAgeId;
    }

    public void setMessAgeId(String messAgeId) {
        this.messAgeId = messAgeId;
    }
}
