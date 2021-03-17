package com.example.event.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import com.example.event.model.FenAllTable;
import java.io.Serializable;

@Entity(tableName = "fenTable", indices = {
        @Index(value = "landOrder")
})
public class FenAllTableEntity implements FenAllTable, Serializable {
    @PrimaryKey
    private String landOrder;
    private String gpsTime;
    private String sheng;
    private String xian;
    private String address;
    private String exmainPerson;
    private String fillPerson;
    private String examineDate;
    private double latitude;
    private double longtitude;
    private double hight;
    private String poXiang;
    private String poWei;
    private String poDu;
    private String treeType;
    private String TuType;
    private String MyMZ;
    private String Area;
    private String TreeName;
    private String LinAge;
    private String avGuanFu;
    private double zhiHigh;
    private String avXiongJin;
    private String avTreeHigh;
    private String yuBiDu;
    private String miDu;
    private String linFenMainJi;
    private String avXuji;
    private String qiYuan;
    private String linZhongYuan;
    private String shuZhong;
    private String health;
    private String JieShi;
    private String detailJson;


    @Override
    public String getLandOrder() {
        return landOrder;
    }

    @Override
    public void setLandOrder(String landOrder) {
       this.landOrder=landOrder;
    }

    @Override
    public String getGpsTime() {
        return gpsTime;
    }

    @Override
    public void setGpsTime(String gpsTime) {
         this.gpsTime=gpsTime;
    }

    @Override
    public String getSheng() {
        return sheng;
    }

    @Override
    public void setSheng(String sheng) {
        this.sheng=sheng;
    }

    @Override
    public String getXian() {
        return xian;
    }

    @Override
    public void setXian(String xian) {
         this.xian=xian;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
          this.address=address;
    }

    @Override
    public String getExmainPerson() {
        return exmainPerson;
    }

    @Override
    public void setExmainPerson(String exmainPerson) {
            this.exmainPerson=exmainPerson;
    }

    @Override
    public String getFillPerson() {
        return fillPerson;
    }

    @Override
    public void setFillPerson(String fillPerson) {
           this.fillPerson=fillPerson;
    }

    @Override
    public String getExamineDate() {
        return examineDate;
    }

    @Override
    public void setExamineDate(String examineDate) {
           this.examineDate=examineDate;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(double latitude) {
     this.latitude=latitude;
    }

    @Override
    public double getLongtitude() {
        return longtitude;
    }

    @Override
    public void setLongtitude(double longtitude) {
         this.longtitude=longtitude;
    }

    @Override
    public double getHight() {
        return hight;
    }

    @Override
    public void setHight(double hight) {
         this.hight=hight;
    }

    @Override
    public String getPoXiang() {
        return poXiang;
    }

    @Override
    public void setPoXiang(String poXiang) {
         this.poXiang=poXiang;
    }

    @Override
    public String getPoWei() {
        return poWei;
    }

    @Override
    public void setPoWei(String poWei) {
           this.poWei=poWei;
    }

    @Override
    public String getPoDu() {
        return poDu;
    }

    @Override
    public void setPoDu(String poDu) {
         this.poDu=poDu;
    }

    @Override
    public String getTreeType() {
        return treeType;
    }

    @Override
    public void setTreeType(String treeType) {
            this.treeType=treeType;
    }

    @Override
    public String getTuType() {
        return TuType;
    }

    @Override
    public void setTuType(String tuType) {
            this.TuType=tuType;
    }

    @Override
    public String getMyMZ() {
        return MyMZ;
    }

    @Override
    public void setMyMZ(String myMZ) {
           this.MyMZ=myMZ;
    }

    @Override
    public String getArea() {
        return Area;
    }

    @Override
    public void setArea(String area) {
            this.Area=area;
    }

    @Override
    public String getTreeName() {
        return TreeName;
    }

    @Override
    public void setTreeName(String treeName) {
           this.TreeName=treeName;
    }

    @Override
    public String getLinAge() {
        return LinAge;
    }

    @Override
    public void setLinAge(String linAge) {
         this.LinAge=linAge;
    }

    @Override
    public String getAvGuanFu() {
        return avGuanFu;
    }

    @Override
    public void setAvGuanFu(String avGuanFu) {
             this.avGuanFu=avGuanFu;
    }

    @Override
    public double getZhiHigh() {
        return zhiHigh;
    }

    @Override
    public void setZhiHigh(double zhiHigh) {
            this.zhiHigh=zhiHigh;
    }

    @Override
    public String getAvXiongJin() {
        return avXiongJin;
    }

    @Override
    public void setAvXiongJin(String avXiongJin) {
           this.avXiongJin=avXiongJin;
    }

    @Override
    public String getAvTreeHigh() {
        return avTreeHigh;
    }

    @Override
    public void setAvTreeHigh(String avTreeHigh) {
           this.avTreeHigh=avTreeHigh;
    }

    @Override
    public String getYuBiDu() {
        return yuBiDu;
    }

    @Override
    public void setYuBiDu(String yuBiDu) {
             this.yuBiDu=yuBiDu;
    }

    @Override
    public String getMiDu() {
        return miDu;
    }

    @Override
    public void setMiDu(String miDu) {
           this.miDu=miDu;
    }

    @Override
    public String getLinFenMainJi() {
        return linFenMainJi;
    }

    @Override
    public void setLinFenMainJi(String linFenMainJi) {
              this.linFenMainJi=linFenMainJi;
    }

    @Override
    public String getAvXuji() {
        return avXuji;
    }

    @Override
    public void setAvXuji(String avXuji) {
           this.avXuji=avXuji;
    }

    @Override
    public String getQiYuan() {
        return qiYuan;
    }

    @Override
    public void setQiYuan(String qiYuan) {
            this.qiYuan=qiYuan;
    }

    @Override
    public String getLinZhongYuan() {
        return linZhongYuan;
    }

    @Override
    public void setLinZhongYuan(String linZhongYuan) {
           this.linZhongYuan=linZhongYuan;
    }

    @Override
    public String getShuZhong() {
        return shuZhong;
    }

    @Override
    public void setShuZhong(String shuZhong) {
             this.shuZhong=shuZhong;
    }

    @Override
    public String getHealth() {
        return health;
    }

    @Override
    public void setHealth(String health) {
      this.health=health;
    }

    @Override
    public String getJieShi() {
        return JieShi;
    }

    @Override
    public void setJieShi(String jieShi) {
           this.JieShi=jieShi;
    }


    @Override
    public String getDetailJson() {
        return detailJson;
    }

    @Override
    public void setDetailJson(String detailJson) {
        this.detailJson = detailJson;
    }
}
