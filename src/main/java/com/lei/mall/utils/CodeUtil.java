package com.lei.mall.utils;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.*;
import java.io.File;

/**
 * @Description TODO
 * @Author bo.li
 * @Date 2023/11/22 19:49
 * @Version 1.0
 */
public class CodeUtil {

    public static void main(String[] args) {
        //目标：把http://www.itcast.cn写二维码里面
        QrConfig config = new QrConfig();
        config.setErrorCorrection(ErrorCorrectionLevel.H);
        config.setBackColor(Color.blue);
        config.setWidth(500);
        config.setHeight(500);

        QrCodeUtil.generate("http://www.itcast.cn",config,new File("C://Users//lei//Desktop//1.png"));
    }

}
