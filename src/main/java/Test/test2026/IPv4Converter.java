package Test.test2026;

public class IPv4Converter {


    /**
     * IPv4转整数 - 位运算法
     * 原理：A*256^3 + B*256^2 + C*256 + D
     * 使用位移运算： (A << 24) | (B << 16) | (C << 8) | D
     */
    public static long ipToLong(String ipAddress) {
        String[] ipParts = ipAddress.split("\\.");

        long result = 0;
        for (int i = 0; i < 4; i++) {
            long part = Long.parseLong(ipParts[i]);
            // 第一个8位左移24位，第二个左移16位，第三个左移8位，第四个不移
            result |= part << (24 - (8 * i));
        }
        return result;
    }


    /**
     * 使用位运算将整数还原为IPv4地址
     * @param ipInt 32位整数表示的IP
     * @return 点分十进制字符串
     */
    public static String intToIpv4(long ipInt) {
        // 提取每8位
        long a = (ipInt >> 24) & 0xFF;
        long b = (ipInt >> 16) & 0xFF;
        long c = (ipInt >> 8) & 0xFF;
        long d = ipInt & 0xFF;

        return String.format("%d.%d.%d.%d", a, b, c, d);
    }






}
