package com.example.event.db.xEntity;

import com.example.event.model.FenAllTable;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import java.io.Serializable;

@Table(name = "fenTable")
public class FenAllTableEntity implements FenAllTable, Serializable {
    @Column(name = "landOrder", isId = true)
    private String landOrder;
    @Column(name = "GpsTime")
    private String gpsTime;
    @Column(name = "sheng")
    private String sheng;
    @Column(name = "xian")
    private String xian;
    @Column(name = "address")
    private String address;
    @Column(name = "exmainPerson")
    private String exmainPerson;
    @Column(name = "fillPerson")
    private String fillPerson;
    @Column(name = "examineDate")
    private String examineDate;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longtitude")
    private double longtitude;
    @Column(name = "hight")
    private double hight;
    @Column(name = "poXiang")
    private String poXiang;
    @Column(name = "poWei")
    private String poWei;
    @Column(name = "poDu")
    private String poDu;
    @Column(name = "treeType")
    private String treeType;
    @Column(name = "TuType")
    private String TuType;
    @Column(name = "MyMz")
    private String MyMZ;
    @Column(name = "Area")
    private String Area;
    @Column(name = "TreeName")
    private String TreeName;
    @Column(name = "LinAge")
    private String LinAge;
    @Column(name = "avGuanFu")
    private String avGuanFu;
    @Column(name = "zhiHigh")
    private double zhiHigh;
    @Column(name = "avXiongJin")
    private String avXiongJin;
    @Column(name = "avTreeHigh")
    private String avTreeHigh;
    @Column(name = "yuBiDu")
    private String yuBiDu;
    @Column(name = "miDu")
    private String miDu;
    @Column(name = "linFenMainJi")
    private String linFenMainJi;
    @Column(name = "avXuji")
    private String avXuji;
    @Column(name = "qiYuan")
    private String qiYuan;
    @Column(name = "linZhongYuan")
    private String linZhongYuan;
    @Column(name = "shuZhong")
    private String shuZhong;
    @Column(name = "health")
    private String health;
    @Column(name = "JieShi")
    private String JieShi;
    @Column(name = "detail")
    private String detailJson;




    @Override
    public String getLandOrder() {
        return landOrder;
    }

    @Override
    public void setLandOrder(String landOrder) {
        this.landOrder=landOrder;
    }


    public String getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
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
