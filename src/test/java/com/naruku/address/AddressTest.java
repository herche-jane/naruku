package com.naruku.address;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class AddressTest {
	
	public static void main(String[] args) throws SocketException {
		
		String innerIp = getLinuxLocalIp();
		System.out.println(innerIp);
		
	}
	
	
	public static String getLinuxLocalIp() throws SocketException {
		String ip = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				String name = intf.getName();
				if (!name.contains("docker") && !name.contains("lo")) {
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							String ipaddress = inetAddress.getHostAddress().toString();
							if (!ipaddress.contains("::") && !ipaddress.contains("0:0:")
									    && !ipaddress.contains("fe80")) {
								ip = ipaddress;
							}
						}
					}
				}
			}
		} catch (SocketException ex) {
			ip = "127.0.0.1";
			ex.printStackTrace();
		}
		return ip;
	}
	
	/**
	 * 获取真实Ip
	 *
	 * @param
	 * @return
	 */
	public static String getInnerIp() {
//		String innerIp = SystemSetting.getProperty("innerIp");
//		if (!StringUtils.isEmpty(innerIp)) {
//			return innerIp;
//		}
		List<String> localip = new ArrayList<String>();// 本地IP，如果没有配置外网IP则返回它
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
				/*NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
						String ip = inetAddress.getHostAddress().toString();
						if(!ip.endsWith(".1")){
							localip.add(ip);
						}
					}
				}*/
				NetworkInterface ni = en.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();
				while (address.hasMoreElements()) {
					InetAddress ip = address.nextElement();
					if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
						String netip = ip.getHostAddress();
						if (netip != null && !"".equals(netip)) {
							localip.add(netip);
						}
					} else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
						String netip = ip.getHostAddress();
						localip.add(netip);
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		if (localip.size() == 0) {
			return "127.0.0.1";
		}
		return localip.get(0);
	}
}
