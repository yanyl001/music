package com.demo.music;

import java.text.DecimalFormat;

/**
 * <pre>控制台字符型进度条</pre>
 *
 * @author yanyl
 * @date 2019/1/23
 */
public class ConsoleProgressBar {
    /** 进度条起始值 */
    private long minimum = 0;
    /** 进度条最大值 */
    private long maximum = 100;
    /** 进度条长度 */
    private long barLen = 100;
    /**  用于进度条显示的字符 */
    private char showChar = '-';
    private String prefix;

    private DecimalFormat format = new DecimalFormat("#.##%");

    /**
     * 使用系统标准输出，显示字符进度条及其百分比。
     */
    public ConsoleProgressBar() {
    }

    /**
     * 使用系统标准输出，显示字符进度条及其百分比。
     *
     * @param minimum 进度条起始值
     * @param maximum 进度条最大值
     * @param barLen  进度条长度
     */
    public ConsoleProgressBar(long minimum, long maximum, long barLen) {
        this(null, minimum, maximum, barLen, '=');
    }

    /**
     * 使用系统标准输出，显示字符进度条及其百分比。
     *
     * @param prefix 前缀
     * @param minimum 进度条起始值
     * @param maximum 进度条最大值
     * @param barLen  进度条长度
     */
    public ConsoleProgressBar(String prefix, long minimum, long maximum, long barLen) {
        this(prefix, minimum, maximum, barLen, '=');
    }

    /**
     * 使用系统标准输出，显示字符进度条及其百分比。
     *
     * @param minimum  进度条起始值
     * @param maximum  进度条最大值
     * @param barLen   进度条长度
     * @param showChar 用于进度条显示的字符
     */
    public ConsoleProgressBar(String prefix, long minimum, long maximum, long barLen, char showChar) {
        this.prefix = prefix;
        this.minimum = minimum;
        this.maximum = maximum;
        this.barLen = barLen;
        this.showChar = showChar;
    }

    /**
     * 显示进度条。
     * @param value 当前进度。进度必须大于或等于起始点且小于等于结束点（start <= current <= end）。
     */
    public void show(long value) {
        if (value < minimum || value > maximum) {
            return;
        }

        reset();
        minimum = value;
        float rate = (float) (minimum * 1.0 / maximum);
        long len = (long) (rate * barLen);
        draw(len, rate);
        if (minimum == maximum) {
            afterComplete();
        }
    }

    private void draw(long len, float rate) {
        System.out.print(prefix);
        for (int i = 0; i < len; i++) {
            System.out.print(showChar);
        }
        System.out.print(' ');
        System.out.print(format(rate));
    }

    private void reset() {
        System.out.print('\r');
    }

    private void afterComplete() {
        System.out.print('\n');
    }

    private String format(float num) {
        return format.format(num);
    }
}
