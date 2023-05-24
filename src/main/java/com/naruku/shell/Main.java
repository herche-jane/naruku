package com.naruku.shell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author herche
 * @date 2022/10/09
 */
public class Main {
    public static void main(String[] args) {
        List<String> cmd = new ArrayList<>();
        cmd.add("create");
        cmd.add("sudo cd /web");
        cmd.add("sudo ls -l");
        ShellUtils.call(cmd);
        System.out.println("=======================");
        ShellUtils.call("sudo cd /etc &&  sudo cd /web && sudo ls -l");
    }
}
