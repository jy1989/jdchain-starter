package com.jd.blockchain.contract;

/**
 * @author zhaogw
 * date 2019/4/23 14:52
 */
public class BirthCalTest {
    /**
     * 根据出生日期计算属相和星座
     * @param args
     */
    public static void main(String[] args) {
        BirthCal birthCal = new BirthCal();
        int year = 1990;
        int month = 10;
        int day = 17;
        //准备基础数据的年月日;
        System.out.println("星座为：" + birthCal.getConstellation(month, day));
        System.out.println("属相为:" + birthCal.getYear(year));
    }
}
