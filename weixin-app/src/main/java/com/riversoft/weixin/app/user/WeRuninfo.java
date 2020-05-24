package com.riversoft.weixin.app.user;

import java.util.List;

/**
 * @author liuyijie
 */
public class WeRuninfo {
    private List<Stepinfo> stepInfoList;
    private Watermark watermark;

    public List<Stepinfo> getStepInfoList() {
        return stepInfoList;
    }

    public void setStepInfoList(List<Stepinfo> stepInfoList) {
        this.stepInfoList = stepInfoList;
    }

    public Watermark getWatermark() {
        return watermark;
    }

    public void setWatermark(Watermark watermark) {
        this.watermark = watermark;
    }
}
