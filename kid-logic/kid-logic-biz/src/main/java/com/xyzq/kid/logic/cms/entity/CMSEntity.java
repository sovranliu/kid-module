package com.xyzq.kid.logic.cms.entity;

public class CMSEntity {
    
    public Integer id;
    /*
    * type=1 会员特惠
    * type=2 俱乐部介绍
    * type=3 分享资讯
     */
    public Integer categoryid;

    public String title;

    public String content;

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
                ", content='" + content + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", link='" + link + '\'' +
                ", deleted=" + deleted +
                ", createtime='" + createtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }
}