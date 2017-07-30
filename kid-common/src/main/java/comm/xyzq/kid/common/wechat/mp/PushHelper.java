package comm.xyzq.kid.common.wechat.mp;

import com.xyzq.simpson.base.async.Operator;
import com.xyzq.simpson.base.async.core.IOperation;
import com.xyzq.simpson.base.json.JSONNumber;
import com.xyzq.simpson.base.json.JSONObject;
import com.xyzq.simpson.base.json.JSONString;
import com.xyzq.simpson.base.type.core.ILink;
import com.xyzq.simpson.base.type.core.ITable;
import comm.xyzq.kid.common.wechat.utility.XMLHttpsUtil;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息推送中心
 */
public class PushHelper {
    /**
     * 消息对象
     */
    private static class Message {
        /**
         * 消息内容
         */
        public String content = null;


        /**
         * 用户开放ID
         */
        public String openId = null;
        /**
         * 模板ID
         */
        public String templateId = null;
        /**
         * 消息数据
         */
        public ITable<String, String> data = null;
        /**
         * 消息落地页
         */
        public String url = null;
    }

    /**
     * 获取凭据URL
     */
    private final static String URL_SEND = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=[ACCESS_TOKEN]";
    /**
     * 日志对象
     */
    public static Logger logger = Logger.getLogger(PushHelper.class);
    /**
     * 投递执行者
     */
    private static Operator<Void> operator = null;
    /**
     * 消息队列
     */
    private static BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>(1024 * 10);


    /**
     * 隐藏构造函数
     */
    private PushHelper() { }

    /**
     * 发送消息
     *
     * @param openId 用户开放ID
     * @param templateId 模板ID
     * @param data 消息数据
     * @param url 消息落地页
     */
    public static void send(String openId, String templateId, ITable<String, String> data, String url) {
        JSONObject content = new JSONObject();
        content.put("touser", new JSONString(openId));
        content.put("template_id", new JSONString(templateId));
        content.put("url", new JSONString(url));
        for(ILink<String, String> link : data) {
            JSONObject keyword = new JSONObject();
            keyword.put("value", new JSONString(link.destination()));
            keyword.put("color", new JSONString("#173177"));
            //
            content.put(link.origin(), keyword);
        }
        send(content.toString());
    }

    /**
     * 发送消息
     *
     * @param content 消息数据
     */
    public static void send(String content) {
        String token = TokenHelper.getToken();
        if(null == token) {
            logger.error("send cancel because of no token");
            return;
        }
        String sendUrl = URL_SEND.replace("[ACCESS_TOKEN]", token);
        try {
            String result = XMLHttpsUtil.post(sendUrl, content.toString());
            JSONObject json = JSONObject.convert(result);
            if(null == json) {
                logger.error("call send(" + sendUrl + ") failed");
                return;
            }
            JSONNumber number = (JSONNumber) json.get("errcode");
            if(null != number && 0 == number.toInteger()) {
                return;
            }
            logger.error("call XMLHttpUtil.post(" + sendUrl + ", " + content.toString() + ") failed, return " + result);
        }
        catch(Exception ex) {
            logger.error("call XMLHttpUtil.post(" + sendUrl + ", " + content.toString() + ") failed", ex);
        }
    }

    /**
     * 投递消息
     *
     * @param content 消息数据
     */
    public static void push(String content) {
        if(null == operator) {
            synchronized (PushHelper.class) {
                if(null == operator) {
                    operator = new Operator<Void>(new IOperation<Void>() {
                        /**
                         * 操作结束回调
                         *
                         * @return 操作结果
                         */
                        @Override
                        public Void onExecute() {
                            while(null != queue) {
                                try {
                                    Message message = queue.take();
                                    if(null == message) {
                                        continue;
                                    }
                                    if(null != message.content) {
                                        send(message.content);
                                    }
                                    else {
                                        send(message.openId, message.templateId, message.data, message.url);
                                    }
                                }
                                catch(Exception ex) {
                                    logger.error("PushHelper.IOperation.onExecute() failed", ex);
                                }
                            }
                            return null;
                        }
                    });
                }
            }
        }
        Message message = new Message();
        message.content = content;
        try {
            queue.put(message);
        }
        catch (InterruptedException ex) {
            logger.error("PushHelper.post.Queue.put failed", ex);
        }
    }

    /**
     * 投递消息
     *
     * @param openId 用户开放ID
     * @param templateId 模板ID
     * @param data 消息数据
     * @param url 消息落地页
     */
    public static void push(String openId, String templateId, ITable<String, String> data, String url) {
        if(null == operator) {
            synchronized (PushHelper.class) {
                if(null == operator) {
                    operator = new Operator<Void>(new IOperation<Void>() {
                        /**
                         * 操作结束回调
                         *
                         * @return 操作结果
                         */
                        @Override
                        public Void onExecute() {
                            while(null != queue) {
                                try {
                                    Message message = queue.take();
                                    if(null == message) {
                                        continue;
                                    }
                                    if(null != message.content) {
                                        send(message.content);
                                    }
                                    else {
                                        send(message.openId, message.templateId, message.data, message.url);
                                    }
                                }
                                catch(Exception ex) {
                                    logger.error("PushHelper.IOperation.onExecute() failed", ex);
                                }
                            }
                            return null;
                        }
                    });
                }
            }
        }
        Message message = new Message();
        message.openId = openId;
        message.templateId = templateId;
        message.data = data;
        message.url = url;
        try {
            queue.put(message);
        }
        catch (InterruptedException ex) {
            logger.error("PushHelper.post.Queue.put failed", ex);
        }
    }
}
