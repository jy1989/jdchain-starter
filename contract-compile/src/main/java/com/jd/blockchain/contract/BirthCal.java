package com.jd.blockchain.contract;

import com.jd.blockchain.contract.model.*;
import com.jd.blockchain.utils.BaseConstant;

/**
 * 模拟用智能合约;
 * 根据生日计算;
 *  @author zhaogw
 * date 2019/4/23 14:52
 */
@Contract
public class BirthCal implements EventProcessingAwire {
	private final static int[] dayArr = new int[]{20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};
	private final static String[] constellationArr = new String[]{"摩羯座",
			"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};

	/**
	 * 根据出生日期计算属相和星座; 数据格式:contractArgs=20##30##1990-10-17
	 * @param eventContext
	 * @throws Exception
	 */
	@ContractEvent(name = "issue-asset")
	public void test1(ContractEventContext eventContext) throws Exception {
		byte [] args_ = eventContext.getArgs();
		if(args_ == null){
			return;
		}
		String[] args = new String(args_).split(BaseConstant.DELIMETER_DOUBLE_ALARM);
		long amount = Long.parseLong(args[0]);
		long amount1 = Long.parseLong(args[1]);
		String birth = args[2];
		String birthArr[] = birth.split("-");
		System.out.println("###test,in BirthCal,invoke test1(),amount Multi:"+(amount*amount1)+ ",birth= "+birth);
		System.out.println("属相为:" + getYear(Integer.parseInt(birthArr[0])));
		System.out.println("星座为：" + getConstellation(Integer.parseInt(birthArr[1]), Integer.parseInt(birthArr[2])));
	}

	/**
	 * Java通过生日计算星座
	 * @param month
	 * @param day
	 * @return
	 */
	public static String getConstellation(int month, int day) {
		return day < dayArr[month - 1] ? constellationArr[month - 1]
				: constellationArr[month];
	}

	/**
	 * 通过生日计算属相
	 *
	 * @param year
	 * @return
	 */
	public static String getYear(int year) {
		if (year < 1900) {
			return "未知";
		}
		int start = 1900;
		String[] years = new String[] { "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊",
				"猴", "鸡", "狗", "猪" };
		return years[(year - start) % years.length];
	}

	@Override
	public void beforeEvent(ContractEventContext contractEventContext) {
	}

	@Override
	public void postEvent() {
	}

	@Override
	public void postEvent(ContractEventContext contractEventContext, ContractException contractError) {
	}

	@Override
	public void postEvent(ContractException contractError) {
	}
}
