/*
 * MIT License
 *
 * Copyright (c) 2018 Andrea Proietto
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package project2100.commons.time;

import java.time.Duration;

/**
 *
 * @author Project2100
 */
public class TimeISO8601 {
    // Both strings must be of format hh:mm:ss
    
    public static boolean isBefore(String current, String reference) {
        String[] cur = current.split(":");
        String[] ref = reference.split(":");

        for (int i = 0; i < 3; i++) {
            if (Integer.parseInt(cur[i]) < Integer.parseInt(ref[i]))
                return true;
            if (Integer.parseInt(cur[i]) > Integer.parseInt(ref[i]))
                break;
        }
        return false;
    }

    public static int parse(String input) {
        String[] values = input.split(":");

        return Integer.parseInt(values[2]) + (60 * Integer.parseInt(values[1]) + (3600 * Integer.parseInt(values[0])));
    }

    public static String prettifyDuration(Duration input) {

        return String.format("%02d:%02d:%02d", input.toHours(), input.toMinutes() % 60, input.getSeconds() % 60);
    }
    
    public static String prettifyInt(int input) {
        if (input>=24*60*60 || input<0) throw new IllegalArgumentException("Given integer value cannot represent a valid daily time signature");

        return String.format("%02d:%02d:%02d", input/3600, input/60%60, input%60);
    }

    public static String sumTimes(String input1, String input2) {
        String[] values1 = input1.split(":");
        String[] values2 = input2.split(":");

        int h = Integer.parseInt(values1[0]) + Integer.parseInt(values2[0]);
        int m = Integer.parseInt(values1[1]) + Integer.parseInt(values2[1]);
        int s = Integer.parseInt(values1[2]) + Integer.parseInt(values2[2]);

        if (s > 59) {
            s -= 60;
            m++;
        }
        if (m > 59) {
            m -= 60;
            h++;
        }
        if (h > 23) h -= 24;

        return String.format("%02d:%02d:%02d", h, m, s);
    }

    public static String subtractTimes(String input1, String input2) {
        String[] values1 = input1.split(":");
        String[] values2 = input2.split(":");

        int h = Integer.parseInt(values1[0]) - Integer.parseInt(values2[0]);
        int m = Integer.parseInt(values1[1]) - Integer.parseInt(values2[1]);
        int s = Integer.parseInt(values1[2]) - Integer.parseInt(values2[2]);

        if (s < 0) {
            s += 60;
            m--;
        }
        if (m < 0) {
            m += 60;
            h--;
        }
        if (h < 0) return null;

        return String.format("%02d:%02d:%02d", h, m, s);
    }
    
}
