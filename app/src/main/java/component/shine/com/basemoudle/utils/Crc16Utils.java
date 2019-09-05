package component.shine.com.basemoudle.utils;

import android.util.Log;

/**
 * Created by cc
 * On 2019/6/6.
 * Crc16Utils 工具类 用于校验数据
 */
public class Crc16Utils {
    /**
     * 计算CRC16校验码
     *
     * @param data 需要校验的字符串
     * @return 校验码
     */
    public static String getCRC(String data) {
        data = data.replace(" ", "");
        int len = data.length();
        if (!(len % 2 == 0)) {
            return "0000";
        }
        int num = len / 2;
        byte[] para = new byte[num];
        for (int i = 0; i < num; i++) {
            int value = Integer.valueOf(data.substring(i * 2, 2 * (i + 1)), 16);
            para[i] = (byte) value;
        }
//        Log.e("Crc16Utils",getCRC(para)+"");
        return getCRC(para);
    }


    /**
     * 计算CRC16校验码
     *
     * @param bytes 字节数组
     * @return {@link String} 校验码
     * @since 1.0
     */
    public static String getCRC(byte[] bytes) {
        //CRC寄存器全为1
        int CRC = 0x0000ffff;
        //多项式校验值
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        //结果转换为16进制
        String result = Integer.toHexString(CRC).toUpperCase();
        if (result.length() != 4) {
            StringBuffer sb = new StringBuffer("0000");
            result = sb.replace(4 - result.length(), 4, result).toString();
        }
        //交换高低位

//        return result.substring(2, 4) + result.substring(0, 2);
        return result;
    }


    public static void main(String[] args) {
        //01 03 20 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 8C 45
        //01 03 00 00 00 08 44 0C
        //01 03 10 00 8F 02 4E 00 91 02 44 00 92 02 5A 00 8B 02 47 40 D8
//        System.out.println(getCRC("01 03 20 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF"));
//        System.out.println(getCRC("01 03 00 00 00 08"));
        System.out.println(getCRC("03 02 00 00 00 20"));
//
//        String order="0101";
//
//
//        StringBuffer sb=new StringBuffer();
//
//        for (int i=0;i<order.length();i++){
//            sb.append(3);
//            sb.append(order.charAt(i));
//        }
//        String str="4D4B4A0B87020100950D0A00000000000000";
////        System.out.println(getCRC("0901"));
//        System.out.println(str.substring(12,14));
    }


    /**
     * @param data Java16进制求和
     * @return
     */
    public static String makeChecksum(String data) {
        Log.d("Crc16Utils","求和数据=="+data);
        if (data == null || data.equals("")) {
            return "00";
        }
        int total = 0;
        int len = data.length();
        int num = 0;
        while (num < len) {
            String s = data.substring(num, num + 2);
            System.out.println(s);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        /**
         * 用256求余最大是255，即16进制的FF
         */
        int mod = total % 256;
        String hex = Integer.toHexString(mod);
        len = hex.length();
        // 如果不够校验位的长度，补0,这里用的是两位校验
        if (len < 2) {
            hex = "0" + hex;
        }
        return "00"+hex;
    }


}
