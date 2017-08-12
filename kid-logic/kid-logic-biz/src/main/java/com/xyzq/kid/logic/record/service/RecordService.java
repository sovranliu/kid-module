package com.xyzq.kid.logic.record.service;

import com.xyzq.kid.CommonTool;
import com.xyzq.kid.finance.service.RefundService;
import com.xyzq.kid.finance.service.api.PayListener;
import com.xyzq.kid.logic.config.common.ConfigCommon;
import com.xyzq.kid.logic.config.entity.ConfigEntity;
import com.xyzq.kid.logic.config.service.ConfigService;
import com.xyzq.kid.logic.config.service.GoodsTypeService;
import com.xyzq.kid.logic.record.bean.RecordBean;
import com.xyzq.kid.logic.record.dao.po.RecordPO;
import com.xyzq.kid.logic.record.entity.RecordEntity;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.kid.logic.user.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Brann on 17/7/27.
 */
@Service("recordService")
public class RecordService implements PayListener {
	/**
	 * 日志对象
	 */
	public static org.slf4j.Logger log = LoggerFactory.getLogger(RecordService.class);
	@Autowired
	private RecordBean recordBean;
	/**
	 * 用户信息
	 */
	@Autowired
	private UserService userService;

	/**
	 * 票价服务
	 */
	@Autowired
	private GoodsTypeService goodsTypeService;
	/**
	 * 配置服务
	 */
	@Autowired
	private ConfigService configService;

	@Value("${file_server_upload_url}")
	private String file_server_upload_url;

	/**
	 * tag字段在购买飞行日志的时候是票流水号SerialNo，在购买飞行票的时候是拥有者的手机号码。
	 */
	@Override
	public void onPay(String orderNo, String openId, int goodsType, int fee, String tag) {
		if (goodsTypeService.isRecord(goodsType)) {
			log.info("RecordService.onPay[in]-orderNo:" + orderNo + ",openId:" + openId + ",goodsType:" + goodsType + ",fee:" + fee);
			UserEntity userEntity = userService.selectByOpenId(tag);
			if (null == userEntity || null == userEntity.openid) {
				log.error("No user by openId:" + openId);
				return;
			}
			TicketEntity ticketEntity = new TicketEntity();
			//飞行日志

			log.info("RecordService.onPay[in]-ticketEntity:" + ticketEntity.toString());
			recordBean.buyRecords(tag);
		}

	}

	/**
	 * 方法描述
	 *
	 * @return 返回值
	 */
	public RecordEntity load(Integer id) {
		return recordBean.load(id);
	}


	/**
	 * 根据飞行票ID和购买状态字段查询record
	 *
	 * @return RecordEntity
	 * @Param ticketID
	 * @Param purchased
	 */
	public List<RecordEntity> findBy(String serialNo, String purchased) {
		return recordBean.findBy(serialNo, purchased);
	}

	/**
	 * 根据飞行票ID和购买状态字段查询record
	 *
	 * @return RecordEntity
	 * @Param usedTIcketSerialNoList
	 * @Param purchased
	 */
	public List<RecordEntity> findBy(List<String> usedTIcketSerialNoList, String purchased) {
		return recordBean.findBy(usedTIcketSerialNoList, purchased);
	}

	/**
	 * 购买飞行日志
	 *
	 * @return Integer
	 * @Param RecordPO
	 */
	public Integer buyRecord(Integer id) {
		return recordBean.buyRecord(id);
	}

	/**
	 * 购买飞行日志
	 *
	 * @return Integer
	 * @Param RecordPO
	 */
	public Integer buyRecords(String serialNo) {
		return recordBean.buyRecords(serialNo);
	}


	/**
	 * 保存飞行日志
	 *
	 * @return int
	 * @Param RecordPO
	 */
	public void saveRecords(String serialNo, String[] ids) {
		List<Integer> idList = new ArrayList<>(ids.length);
		for (String id : ids) {
			idList.add(Integer.parseInt(id));
		}
		recordBean.saveRecords(serialNo, idList);
	}

	/**
	 * 删除飞行日志
	 *
	 * @return int
	 * @Param RecordPO
	 */
	public int deleteRecord(Integer id) {
		return recordBean.deleteRecord(id);
	}

	/**
	 * 方法描述
	 * /apps/data/record/2017-07/201707271830459527.mp4。
	 *
	 * @return fileName
	 */
	public Map<String, String> uploadFile(MultipartFile uploadFile) throws Exception {
		Map<String, String> resMap = new HashMap<String, String>();
		InputStream inputStream = uploadFile.getInputStream();
		byte[] b = new byte[1048576];
		Integer length = inputStream.read(b);
		// 文件流写到服务器端
		String suffixStr = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().indexOf("."));
		String filePath = genFileName(suffixStr);
		FileOutputStream outputStream = new FileOutputStream(filePath);
		outputStream.write(b, 0, length);
		inputStream.close();
		outputStream.close();
		//保存record记录，但是不关联票券号。
		RecordPO recordPO = new RecordPO();
		recordPO.setPath(filePath);
		Integer id = recordBean.addRecord(recordPO);
		resMap.put("id", String.valueOf(id));
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

	public static String getRandomStr(Integer codeCount) {

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
		Integer shuziNum = random.nextInt(2) + 1;
		Integer charNum = codeCount - shuziNum;

		// 随机产生codeCount数字的验证码。
		for (Integer i = 0; i < shuziNum; i++) {
			// 得到随机产生的验证码数字。
			String numRand = String.valueOf(codeSequenceNumber[random.nextInt(codeSequenceNumber.length)]);
			// 将产生的随机数组合在一起。
			randomCode.add(numRand);
		}
		for (Integer i = 0; i < charNum; i++) {
			// 得到随机产生的验证码字母。
			String strRand = String.valueOf(codeSequenceChar[random.nextInt(codeSequenceChar.length)]);
			// 将产生的随机数组合在一起。
			randomCode.add(strRand);
		}

		Collections.shuffle(randomCode);

		for (Integer i = 0; i < randomCode.size(); i++) {
			randomCodeRes.append(randomCode.get(i));
		}

		return randomCodeRes.toString();
	}

}
