package com.xyzq.kid.logic.cms.entity;

public class CMSEntity {
    
    public Integer id;

    public Integer categoryid;

    public String title;

    public String imageurl;

    public String link;

    public Integer deleted;

    public String createtime;

    public String updatetime;

    @Override
    public String toString() {
        return "CMSEntity{" +
                "id=" + id +
                ", categoryid=" + categoryid +
                ", title='" + title + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", link='" + link + '\'' +
                ", deleted=" + deleted +
                ", createtime='" + createtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }
}