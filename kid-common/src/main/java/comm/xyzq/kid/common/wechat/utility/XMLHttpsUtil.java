package comm.xyzq.kid.common.wechat.utility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.*;

/**
 * 投递XML的HTTPS组件
 */
public class XMLHttpsUtil {
    /**
     * 套接字超时时长（毫秒）
     */
    public final static int NET_SOCKET_TIMEOUT = 10 * 1000;
    /**
     * 连接超时时长（毫秒）
     */
    public final static int NET_CONNECT_TIMEOUT = 10 * 1000;


    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger("pay");
    /**
     * HTTP请求客户端
     */
    private static CloseableHttpClient httpClient = null;


    /**
     * 隐藏构造函数
     */
    private XMLHttpsUtil() { }

    /**
     * 初始化模块
     */
    private static boolean initialize() throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, java.security.cert.CertificateException {
        String password = WechatConfig.apiPassword;
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(XMLHttpsUtil.class.getResourceAsStream("/certificate/apiclient_cert.p12"), password.toCharArray());
        SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, password.toCharArray()).build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
        return true;
    }

    /**
     * 投递内容
     *
     * @param url 请求URL
     * @return 返回字符串
     */
    public static String get(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = null;
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                if(200 != response.getStatusLine().getStatusCode()) {
                    return null;
                }
                if (null == entity) {
                    return null;
                }
                result = EntityUtils.toString(entity, "UTF-8");
            }
            finally {
                response.close();
            }
        }
        catch (Exception e) {
            logger.error("XMLHttosUtil.get() failed", e);
        }
        finally {
            try {
                httpGet.abort();
            }
            catch(Exception ex) { }
        }
        return result;
    }

    /**
     * 投递内容
     *
     * @param url 请求URL
     * @param content 投递内容
     * @return 返回字符串
     */
    public static String post(String url, String content) throws IOException {
        if(null == httpClient) {
            synchronized (XMLHttpsUtil.class) {
                if(null == httpClient) {
                    try {
                        if(!initialize()) {
                            return null;
                        }
                    }
                    catch(Exception ex) {
                        logger.error("execute XMLHttpsUtil.initialize() failed", ex);
                        return null;
                    }
                }
            }
        }
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(content, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(stringEntity);
        httpPost.setConfig(RequestConfig.custom().setSocketTimeout(NET_SOCKET_TIMEOUT).setConnectTimeout(NET_CONNECT_TIMEOUT).build());
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        }
        catch (IOException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new RuntimeException("XMLHttpsUtil.post('" + url + "', '" + content + "') failed", ex);
        }
        finally {
            httpPost.abort();
        }
        return result;
    }
}
