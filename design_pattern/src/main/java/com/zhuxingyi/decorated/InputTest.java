package com.zhuxingyi.decorated;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author zhuxingyi
 * @date 2023/3/27 22:20
 */
public class InputTest {
    public static void main(String[] args) {
        int c;
        try {
            LowerCaseInputStream lowerCaseInputStream =
                    new LowerCaseInputStream(new BufferedInputStream(new ByteArrayInputStream("I AM ZHUXINGYI".getBytes(StandardCharsets.UTF_8))));
            while ((c = lowerCaseInputStream.read()) >= 0) {
                System.out.print((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
