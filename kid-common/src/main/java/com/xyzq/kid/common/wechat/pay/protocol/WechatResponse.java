package com.xyzq.kid.common.wechat.pay.protocol;

import com.xyzq.kid.common.wechat.pay.annotation.ProtocolField;
import com.xyzq.simpson.base.runtime.Kind;
import com.xyzq.simpson.base.text.Text;
import com.xyzq.simpson.base.type.Table;
import com.xyzq.simpson.base.xml.core.IXMLNode;

import java.lang.reflect.Field;

/**
 * 微信反馈结构体
 */
public class WechatResponse extends WechatRequest {
    /**
     * 返回状态码
     */
    @ProtocolField("return_code")
    public String returnCode = null;
    /**
     * 返回信息
     */
    @ProtocolField("return_msg")
    public String returnMsg = null;
    /**
     * 业务结果
     */
    @ProtocolField("result_code")
    public String resultCode = null;
    /**
     * 错误代码
     */
    @ProtocolField("err_code")
    public String errCode = null;
    /**
     * 错误代码描述
     */
    @ProtocolField("err_code_des")
    public String errCodeDes = null;


    /**
     * 判读反馈是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccessful() {
        if("SUCCESS".equalsIgnoreCase(returnCode) && "SUCCESS".equalsIgnoreCase(resultCode)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 解析XML节点
     *
     * @param node XML节点
     * @return 处理结果
     */
    public boolean parse(IXMLNode node) {
        if(null == node) {
            return false;
        }
        Table<String, Field> fieldTable = new Table<String, Field>();
        for(Field field : Kind.fetchFields(this.getClass())) {
            ProtocolField protocolField = field.getAnnotation(ProtocolField.class);
            if (null == protocolField) {
                continue;
            }
            fieldTable.put(protocolField.value(), field);
        }
        for(IXMLNode son : node.children()) {
            Field field = fieldTable.get(son.getName());
            if(null == field) {
                continue;
            }
            field.setAccessible(true);
            try {
                if(field.getType().equals(String.class)) {
                    field.set(this, son.getValue());
                }
                else if(field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
                    if(!Text.isBlank(son.getValue())) {
                        field.set(this, Integer.valueOf(son.getValue()));
                    }
                }
                else if(field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
                    field.set(this, son.getValue());
                }
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException("response set field failed, field = " + field.getName(), e);
            }
        }
        return true;
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
