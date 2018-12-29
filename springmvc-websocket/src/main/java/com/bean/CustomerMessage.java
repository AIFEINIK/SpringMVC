package com.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Feinik
 * @Discription
 * @Data 2018/12/25
 * @Version 1.0.0
 */
@Setter
@Getter
public class CustomerMessage {
    private String userName;
    private String msg;
    private String time;

    public CustomerMessage() {
    }

    public CustomerMessage(String msg, String time) {
        this.msg = msg;
        this.time = time;
    }

    public CustomerMessage(String userName, String msg, String time) {
        this.userName = userName;
        this.msg = msg;
        this.time = time;
    }
}
