/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chensl@ucweb.com
 *
 * 上午10:01:09 2014-7-30
 */
public class MatcherUtil {

	/**
	 * 检查IP是否有效
	 * 
	 * @param ipaddr 输入的IP字符串
	 * @return   是否有效
	 */
	public static boolean isIPAddress(String ipaddr) {
		boolean flag = false;
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])"
				+ "\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])"
				+ "\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])"
				+ "\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher m = pattern.matcher(ipaddr);
		flag = m.matches();
		return flag;
	}
	
	/**
	 * 检查mac地址是否有效
	 * 
	 * @param addr 输入的mac字符串
	 * @return 是否有效
	 */
	public static boolean isMacAddress(String addr){
		boolean flag = false;
		Pattern pattern = Pattern.compile("([0-9A-F]{2})(:[0-9A-F]{2}){5}");
		Matcher matcher = pattern.matcher(addr);
		flag = matcher.matches();
		return flag;
	}
}
