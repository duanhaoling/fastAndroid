package com.ldh.androidlib.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式匹配工具类
 *
 */
public class RegularExpressionUtil {

    //中文/英文字母(大写小)/数字/下划线 的混合物体
    public static final String REG_LOGIN_USERNAME = "[\u4e00-\u9fa5|a-zA-z|_|0-9]*";
    //为纯数字
    public static final String REG_NUMBER = "^[0-9]*$";
    //包含8位以上连续数字
    public static final String REG_8_CONTINUOUS_NUM = "\\d{8,}";
    //包含连续2个以上的特殊符号
    public static final String REG_2_CONTINUOUS_SPECIAL_CHAR = "[@#￥$%＠＃￥＄％]{2,}";
    //包含特殊符号
    public static final String REG_SPECIAL_CHAR = "[@#￥$%＠＃￥＄％]";
    //汉字
    public static final String REG_CHINESE = "^[\\u4e00-\\u9fa5]{0,}$";
    //座机号：[0+2-3位]+2-9开头的八位数字+1-5位的分机号；区号与分级号可以不填写
    //手机号：以13/14/15/18/17开头的11位手机号
    //400/800号码：以400/800开头的10位号码+1-6位的分机号；分级号可以不填写
    public static final String REG_WB_GJ_CONTACT_PHONE = "(^(0\\d{2,3})?-?([2-9]\\d{6,7})(-\\d{1,5})?$)|(^(13|14|15|18|17)\\d{9}$)|(^(400|800)\\d{7}(-\\d{1,6})?$)";

    /**
     * 根据正则表达式匹配
     *
     * @param reg     正则表达式
     * @param strings 待校验的字符串
     * @return 是否匹配成功
     */
    public static boolean checkByPattern(String reg, String... strings) {
        Pattern pattern = Pattern.compile(reg);
        for (String str : strings) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }
}
