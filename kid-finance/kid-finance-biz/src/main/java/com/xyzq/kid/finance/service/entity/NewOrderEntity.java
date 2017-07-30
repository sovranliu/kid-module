package com.xyzq.kid.finance.service.entity;

import com.xyzq.simpson.base.etc.Serial;
import com.xyzq.simpson.base.time.DateTime;
import com.xyzq.simpson.base.type.Table;
import com.xyzq.kid.common.wechat.utility.WechatConfig;
import com.xyzq.kid.common.wechat.utility.WechatHelper;

/**
 * 新创建的订单信息
 */
public class NewOrderEntity extends OrderEntity {
    /**
     * 随机字符串
     */
    public String nonceString = null;
    /**
     * 签名方式
     */
    public String signType = null;
    /**
     * 时间戳
     */
    public long timestamp = 0;
    /**
     * 签名
     */
    public String signature = null;


    /**
     * 构造函数
     */
    public NewOrderEntity() {
        this.nonceString = Serial.makeRandomString(32).toUpperCase();
        this.signType = "MD5";
        this.timestamp = DateTime.now().toLong();
    }

    /**
     * 生成签名
     */
    public void makeSign() {
        Table<String, Object> signTable = new Table<String, Object>();
        signTable.put("appId", WechatConfig.appId);
        signTable.put("timeStamp", this.timestamp);
        signTable.put("nonceStr", this.nonceString);
        signTable.put("package", "prepay_id=" + this.prepayId);
        signTable.put("signType", signType);
        this.signature = WechatHelper.makeSign(signTable);
    }
}
