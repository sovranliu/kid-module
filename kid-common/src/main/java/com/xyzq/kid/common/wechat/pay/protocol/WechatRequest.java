package com.xyzq.kid.common.wechat.pay.protocol;

import com.xyzq.kid.common.wechat.pay.annotation.ProtocolField;
import com.xyzq.simpson.base.runtime.Kind;
import com.xyzq.simpson.base.text.Text;
import com.xyzq.simpson.base.xml.XMLNode;
import com.xyzq.simpson.base.xml.core.IXMLNode;
import com.xyzq.kid.common.wechat.utility.WechatHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * 微信请求
 */
public class WechatRequest {
    /**
     * 日志对象
     */
    protected static Logger logger = LoggerFactory.getLogger(WechatRequest.class);


    /**
     * 转换为XML节点对象
     *
     * @return XML节点对象
     */
    public IXMLNode toXML() {
        XMLNode result = new XMLNode();
        result.setName("xml");
        // 提取字段
        for(Field field : Kind.fetchFields(this.getClass())) {
            ProtocolField protocolField = field.getAnnotation(ProtocolField.class);
            if(null == protocolField) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object value = field.get(this);
                if(null == value) {
                    continue;
                }
                XMLNode item = new XMLNode();
                item.setName(protocolField.value());
                item.setValue(String.valueOf(value));
                result.children().add(item);
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException("wechat request calculate field value failed", e);
            }
        }
        return result;
    }

    /**
     * 转换为带签名的XML节点对象
     *
     * @return XML节点对象
     */
    public IXMLNode toXMLWithSign() {
        IXMLNode result = toXML();
        String sign = WechatHelper.makeSign(result);
        if(!Text.isBlank(sign)) {
            XMLNode item = new XMLNode();
            item.setName("sign");
            item.setValue(sign);
            result.children().add(item);
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
        return toXMLWithSign().toString();
    }
}
