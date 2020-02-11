package com.company;

import com.company.felulet.SakkFelulet;
import com.company.logika.SakkTabla;

public class Main {

    public static void main(String[] args) {


        SakkFelulet f = new SakkFelulet();

        SakkTabla t = new SakkTabla();
        System.out.println(t);
        t.lep(6,4,4,4);
        System.out.println(t);
        t.lep(6,3,4,2); //érvénytelen lépés!!!
        System.out.println(t);
        t.lep(1,4,3,4);
        System.out.println(t);


    }
}
