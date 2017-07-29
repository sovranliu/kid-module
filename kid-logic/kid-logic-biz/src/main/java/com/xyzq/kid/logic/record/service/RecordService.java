package com.xyzq.kid.logic.record.service;

import com.xyzq.kid.logic.record.bean.RecordBean;
import com.xyzq.kid.logic.record.dao.po.RecordPO;
import com.xyzq.kid.logic.record.entity.RecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Brann on 17/7/27.
 */
@Service("recordService")
public class RecordService {
    /**
     * 范例组件
     */
    @Autowired
    private RecordBean recordBean;


    @Value("${file_server_upload_url}")
    private String file_server_upload_url;

    private static Logger log = Logger.getLogger(RecordService.class.getName());


    /**
     * 方法描述
     *
     * @return 返回值
     */
    public RecordEntity call() {
        return recordBean.call();
    }


    /**
     * 根据飞行票ID和购买状态字段查询record
     *
     * @return RecordEntity
     * @Param ticketID
     * @Param purchased
     */
    public List<RecordEntity> findBy(int ticketID, String purchased) {
        return recordBean.findBy(ticketID, purchased);
    }

    /**
     * 购买飞行日志
     *
     * @return int
     * @Param RecordPO
     */
    public int buyRecord(RecordPO recordPO) {
        return recordBean.buyRecord(recordPO);
    }

    /**
     * 方法描述
     * /apps/data/record/2017-07/201707271830459527.mp4。
     *
     * @return 返回值
     */
    public Map<String, String> uploadFile(MultipartFile uplodaFile) {
        Map<String, String> resMap = new HashMap<String, String>();
        try {
            InputStream inputStream = uplodaFile.getInputStream();
            byte[] b = new byte[1048576];
            int length = inputStream.read(b);
            // 文件流写到服务器端
            String suffixStr = uplodaFile.getOriginalFilename().substring(uplodaFile.getOriginalFilename().indexOf("."));
            String filePath = genFileName(suffixStr);
            FileOutputStream outputStream = new FileOutputStream(filePath);
            outputStream.write(b, 0, length);
            inputStream.close();
            outputStream.close();

            resMap.put("code", "0");
            resMap.put("msg", "文件上传成功!");
            resMap.put("data", "fileName:\"" + filePath + "\"");
        } catch (Exception e) {
//            log.error("上传文件失败", e);
            resMap.put("code", "2");
            resMap.put("msg", e.getMessage());
            resMap.put("data", null);
        }

        return resMap;

    }
    /**
     * 方法描述
     * /apps/data/record/2017-07/201707271830459527.mp4。
     *
     * @return 返回值
     */
    private String genFileName(String suffixStr) {

        SimpleDateFormat headFormatter = new SimpleDateFormat("yyyy-MM");
        String headStr = headFormatter.format(new Date());

        SimpleDateFormat midFormatter = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String midStr = midFormatter.format(new Date());

        String tailStr = getRandomStr(4);

        return file_server_upload_url + "/" + headStr + "/" + midStr + tailStr + suffixStr;
    }

    public static String getRandomStr(int codeCount) {

        StringBuffer randomCodeRes = new StringBuffer();

        char[] codeSequenceNumber = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        char[] codeSequenceChar = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z'
        };
        List<String> randomCode = new ArrayList<String>();

        // 创建一个随机数生成器类
        Random random = new Random();

        //随机产生，验证码由几个数字、几个字母组成
        int shuziNum = random.nextInt(2) + 1;
        int charNum = codeCount - shuziNum;

        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < shuziNum; i++) {
            // 得到随机产生的验证码数字。
            String numRand = String.valueOf(codeSequenceNumber[random.nextInt(codeSequenceNumber.length)]);
            // 将产生的随机数组合在一起。
            randomCode.add(numRand);
        }
        for (int i = 0; i < charNum; i++) {
            // 得到随机产生的验证码字母。
            String strRand = String.valueOf(codeSequenceChar[random.nextInt(codeSequenceChar.length)]);
            // 将产生的随机数组合在一起。
            randomCode.add(strRand);
        }

        Collections.shuffle(randomCode);

        for (int i = 0; i < randomCode.size(); i++) {
            randomCodeRes.append(randomCode.get(i));
        }

        return randomCodeRes.toString();
    }

}
