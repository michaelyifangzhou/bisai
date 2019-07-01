package cn.bisai.domain;

import java.time.Instant;
import java.util.Date;

public class test {
    public static void main(String[] args)throws Exception{
        Date d1=new Date();
        System.out.println(d1);
        System.out.println(d1.getTime());
        Thread.sleep(2000);
        Date d2=new Date();
        d2.setTime(System.currentTimeMillis());
        System.out.println(d2);
        System.out.println(d2.getTime());
        System.out.println(d2.getTime()-d1.getTime());
        Thread.sleep(2000);
        d1.setTime(System.currentTimeMillis());
        System.out.println(d1);
        System.out.println(d1.getTime());
        Instant is=Instant.now();
        System.out.println(is);
    }
}
