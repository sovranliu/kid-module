package com.xyzq.kid.common.wechat.pay.protocol;

import com.xyzq.simpson.base.text.Text;
import com.xyzq.simpson.base.time.DateTime;
import com.xyzq.simpson.base.xml.XMLNode;
import com.xyzq.simpson.base.xml.core.IXMLNode;
import com.xyzq.kid.common.wechat.utility.WechatHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

/**
 * 确认收款反馈结构体
 */
public class NotifyPayResponse {
    /**
     * 日志对象
     */
    public static Logger logger = LoggerFactory.getLogger(NotifyPayResponse.class);


    /**
     * 属性
     */
    public String returnCode = null;
    public String returnMsg = null;
    public String resultCode = null;
    public String errCode = null;
    public String errCodeDes = null;
    public String appId = null;
    public String merchantId = null;
    public String deviceInfo = null;
    public String nonceString = null;
    public String sign = null;
    public String openId = null;
    public boolean isSubscribe = false;
    public String tradeType = null;
    public String bankType = null;
    public int totalFee = 0;
    public String feeType = null;
    public int cashFee = 0;
    public String cashFeeType = null;
    public int couponFee = 0;
    public int couponCount = 0;
    public String transactionId = null;
    public String tradeNo = null;
    public String attach = null;
    public DateTime endTime = null;


    /**
     * 转换为XML节点对象
     *
     * @return XML节点对象
     */
    public IXMLNode toXML() {
        XMLNode result = new XMLNode();
        result.setName("xml");
        XMLNode item = null;
        //
        if(!Text.isBlank(returnCode)) {
            item = new XMLNode();
            item.setName("return_code");
            item.setValue(returnCode);
            result.children().add(item);
        }
        else if(!Text.isBlank(returnMsg)) {
            item = new XMLNode();
            item.setName("return_msg");
            item.setValue(returnMsg);
            result.children().add(item);
        }
        else if(!Text.isBlank(resultCode)) {
            item = new XMLNode();
            item.setName("result_code");
            item.setValue(resultCode);
            result.children().add(item);
        }
        if(!Text.isBlank(errCode)) {
            item = new XMLNode();
            item.setName("err_code");
            item.setValue(errCode);
            result.children().add(item);
        }
        if(!Text.isBlank(errCodeDes)) {
            item = new XMLNode();
            item.setName("err_code_des");
            item.setValue(errCodeDes);
            result.children().add(item);
        }
        if(!Text.isBlank(appId)) {
            item = new XMLNode();
            item.setName("appid");
            item.setValue(appId);
            result.children().add(item);
        }
        if(!Text.isBlank(merchantId)) {
            item = new XMLNode();
            item.setName("mch_id");
            item.setValue(merchantId);
            result.children().add(item);
        }
        if(!Text.isBlank(deviceInfo)) {
            item = new XMLNode();
            item.setName("device_info");
            item.setValue(deviceInfo);
            result.children().add(item);
        }
        if(!Text.isBlank(nonceString)) {
            item = new XMLNode();
            item.setName("nonce_str");
            item.setValue(nonceString);
            result.children().add(item);
        }
        if(!Text.isBlank(sign)) {
            item = new XMLNode();
            item.setName("sign");
            item.setValue(sign);
            result.children().add(item);
        }
        if(!Text.isBlank(openId)) {
            item = new XMLNode();
            item.setName("openid");
            item.setValue(openId);
            result.children().add(item);
        }
        if(true) {
            item = new XMLNode();
            item.setName("is_subscribe");
            if(isSubscribe) {
                item.setValue("Y");
            }
            else {
                item.setValue("N");
            }
            result.children().add(item);
        }
        if(!Text.isBlank(tradeType)) {
            item = new XMLNode();
            item.setName("trade_type");
            item.setValue(tradeType );
            result.children().add(item);
        }
        if(!Text.isBlank(bankType)) {
            item = new XMLNode();
            item.setName("bank_type");
            item.setValue(bankType);
            result.children().add(item);
        }
        if(totalFee >= 0) {
            item = new XMLNode();
            item.setName("total_fee");
            item.setValue(String.valueOf(totalFee));
            result.children().add(item);
        }
        if(!Text.isBlank(feeType)) {
            item = new XMLNode();
            item.setName("fee_type");
            item.setValue(feeType);
            result.children().add(item);
        }
        if(cashFee >= 0) {
            item = new XMLNode();
            item.setName("cash_fee");
            item.setValue(String.valueOf(cashFee));
            result.children().add(item);
        }
        if(!Text.isBlank(cashFeeType)) {
            item = new XMLNode();
            item.setName("cash_fee_type");
            item.setValue(cashFeeType);
            result.children().add(item);
        }
        if(couponFee >= 0) {
            item = new XMLNode();
            item.setName("coupon_fee");
            item.setValue(String.valueOf(couponFee));
            result.children().add(item);
        }
        if(couponCount >= 0) {
            item = new XMLNode();
            item.setName("coupon_count");
            item.setValue(String.valueOf(couponCount));
            result.children().add(item);
        }
        if(!Text.isBlank(transactionId)) {
            item = new XMLNode();
            item.setName("transaction_id");
            item.setValue(transactionId);
            result.children().add(item);
        }
        if(!Text.isBlank(tradeNo)) {
            item = new XMLNode();
            item.setName("out_trade_no");
            item.setValue(tradeNo);
            result.children().add(item);
        }
        if(!Text.isBlank(attach)) {
            item = new XMLNode();
            item.setName("attach");
            item.setValue(attach);
            result.children().add(item);
        }
        if(null != endTime) {
            item = new XMLNode();
            item.setName("time_end");
            item.setValue(endTime.toString("yyyyMMddHHmmss"));
            result.children().add(item);
        }
        return result;
    }

    /**
     * 解析生成退款反馈结构体
     *
     * @param node XML节点
     * @return 退款反馈结构体
     */
    public static NotifyPayResponse parse(IXMLNode node) {
        if(null == node) {
            return null;
        }
        NotifyPayResponse result = new NotifyPayResponse();
        for(IXMLNode item : node.children()) {
            if("return_code".equalsIgnoreCase(item.getName())) {
                result.returnCode = item.getValue();
            }
            else if("return_msg".equalsIgnoreCase(item.getName())) {
                result.returnMsg = item.getValue();
            }
            else if("result_code".equalsIgnoreCase(item.getName())) {
                result.resultCode = item.getValue();
            }
            else if("err_code".equalsIgnoreCase(item.getName())) {
                result.errCode = item.getValue();
            }
            else if("err_code_des".equalsIgnoreCase(item.getName())) {
                result.errCodeDes = item.getValue();
            }
            else if("appid".equalsIgnoreCase(item.getName())) {
                result.appId = item.getValue();
            }
            else if("mch_id".equalsIgnoreCase(item.getName())) {
                result.merchantId = item.getValue();
            }
            else if("device_info".equalsIgnoreCase(item.getName())) {
                result.deviceInfo = item.getValue();
            }
            else if("nonce_str".equalsIgnoreCase(item.getName())) {
                result.nonceString = item.getValue();
            }
            else if("sign".equalsIgnoreCase(item.getName())) {
                result.sign = item.getValue();
            }
            else if("openid".equalsIgnoreCase(item.getName())) {
                result.openId = item.getValue();
            }
            else if("is_subscribe".equalsIgnoreCase(item.getName())) {
                if("Y".equals(item.getValue())) {
                    result.isSubscribe = true;
                }
                else {
                    result.isSubscribe = false;
                }
            }
            else if("trade_type".equalsIgnoreCase(item.getName())) {
                result.tradeType = item.getValue();
            }
            else if("bank_type".equalsIgnoreCase(item.getName())) {
                result.bankType = item.getValue();
            }
            else if("total_fee".equalsIgnoreCase(item.getName())) {
                result.totalFee = Integer.valueOf(item.getValue());
            }
            else if("fee_type".equalsIgnoreCase(item.getName())) {
                result.feeType = item.getValue();
            }
            else if("cash_fee".equalsIgnoreCase(item.getName())) {
                result.cashFee = Integer.valueOf(item.getValue());
            }
            else if("cash_fee_type".equalsIgnoreCase(item.getName())) {
                result.cashFeeType = item.getValue();
            }
            else if("coupon_fee".equalsIgnoreCase(item.getName())) {
                result.couponFee = Integer.valueOf(item.getValue());
            }
            else if("coupon_count".equalsIgnoreCase(item.getName())) {
                result.couponCount = Integer.valueOf(item.getValue());
            }
            else if("transaction_id".equalsIgnoreCase(item.getName())) {
                result.transactionId = item.getValue();
            }
            else if("out_trade_no".equalsIgnoreCase(item.getName())) {
                result.tradeNo = item.getValue();
            }
            else if("attach".equalsIgnoreCase(item.getName())) {
                result.attach = item.getValue();
            }
            else if("time_end".equalsIgnoreCase(item.getName())) {
                try {
                    result.endTime = DateTime.parse(item.getValue(), "yyyyMMddHHmmss");
                }
                catch (ParseException e) {
                    logger.error("parse " + item.getValue() + " failed", e);
                }
            }
            else if("no_credit".equalsIgnoreCase(item.getName())) {

            }
            else {
                logger.warn("unknown xml node in pay notify response item = " + item.toString() + "\nxml = " + node.toString());
            }
        }
        String sign = WechatHelper.makeSign(node);
        if(!sign.equals(result.sign)) {
            logger.error("pay notify check sign failed, content:\n" + node.toString());
            return null;
        }
        return result;
    }

    /**
     * 转换为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return toXML().toString();
    }
}
