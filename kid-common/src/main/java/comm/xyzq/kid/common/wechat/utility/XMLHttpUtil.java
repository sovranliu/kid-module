package comm.xyzq.kid.common.wechat.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 投递XML的HTTP组件
 */
public class XMLHttpUtil {
    /**
     * 超时时长（毫秒）
     */
    public final static int NET_TIMEOUT = 10 * 1000;
    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger("pay");


    /**
     * 隐藏构造函数
     */
    private XMLHttpUtil() { }

    /**
     * 投递XML
     *
     * @param urlString 请求URL
     * @param content 投递内容
     * @return 返回字符串
     */
    public static String post(String urlString, String content) throws IOException {
        String result = null;
        //
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(NET_TIMEOUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(content.getBytes("UTF-8").length));
            connection.setUseCaches(false);
            connection.connect();
            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(content);
            outputStream.flush();
            if(HttpURLConnection.HTTP_OK != connection.getResponseCode()) {
                throw new IOException("unexpected post '" + urlString + "' response code:" + connection.getResponseCode());
            }
            inputStream = connection.getInputStream();
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            result = new String(buffer, "UTF-8");
        }
        catch(IOException ex) {
            try {
                throw ex;
            } catch (Exception e) { }
        }
        finally {
            if(null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) { }
                outputStream = null;
            }
            if(null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) { }
                inputStream = null;
            }
            if(null != connection) {
                connection.disconnect();
                connection = null;
            }
        }
        return result;
    }
}
