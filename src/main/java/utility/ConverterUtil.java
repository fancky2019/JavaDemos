package utility;

public class ConverterUtil {
    public static byte[] intToBytes(int num) {
        byte[] bytes = new byte[4];
        bytes[3] = (byte) (num >> 24& 0xff);
        bytes[2] = (byte) (num >> 16& 0xff);
        bytes[1] = (byte) (num >> 8& 0xff);
        bytes[0] = (byte) (num& 0xff);

        return bytes;
    }

    public static int bytesToInt(byte[] bytes) {
        //如果不与0xff进行按位与操作，转换结果将出错，有兴趣的同学可以试一下。
        int int1 = bytes[0] & 0xff;
        int int2 = (bytes[1] & 0xff) << 8;
        int int3 = (bytes[2] & 0xff) << 16;
        int int4 = (bytes[3] & 0xff) << 24;

        return int1 | int2 | int3 | int4;
    }
}
