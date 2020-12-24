package com.jingkai.asset.function.main.entity;

/**
 * created by YQ on 2019-05-24 10:47
 */
public class OverviewBean {

    /**
     * rentalRate : 0.47
     * heightEcRate : 0.17
     * parkCount : 48
     * acreageTotal : 8176451
     * heightEc :  3
     * normalEc : 18
     */

    private double rentalRate;//房产出租率
    private double heightEcRate;//	高新企业占比
    private double parkCount;//项目总数
    private double acreageTotal;//资产面积
    private double heightEc;//高新技术企业
    private double normalEc;//签约企业

    public double getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(double rentalRate) {
        this.rentalRate = rentalRate;
    }

    public double getHeightEcRate() {
        return heightEcRate;
    }

    public void setHeightEcRate(double heightEcRate) {
        this.heightEcRate = heightEcRate;
    }

    public double getParkCount() {
        return parkCount;
    }

    public void setParkCount(double parkCount) {
        this.parkCount = parkCount;
    }

    public double getAcreageTotal() {
        return acreageTotal;
    }

    public void setAcreageTotal(double acreageTotal) {
        this.acreageTotal = acreageTotal;
    }

    public double getHeightEc() {
        return heightEc;
    }

    public void setHeightEc(double heightEc) {
        this.heightEc = heightEc;
    }

    public double getNormalEc() {
        return normalEc;
    }

    public void setNormalEc(double normalEc) {
        this.normalEc = normalEc;
    }
}
