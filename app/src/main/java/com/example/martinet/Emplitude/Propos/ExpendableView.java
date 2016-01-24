package com.example.martinet.Emplitude.Propos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpendableView {

    public static HashMap getData() {
        HashMap expandableListDetail = new HashMap<>();

        List etienne = new ArrayList();
        etienne.add("Je sais pas encore");

        List florian = new ArrayList();
        florian.add("Je sais pas encore");

        List eric = new ArrayList();
        eric.add("Je sais pas encore");

        List kevin = new ArrayList();
        kevin.add("Je sais pas encore");

        List anthony = new ArrayList();
        anthony.add("Je sais pas encore");

        List arnaud = new ArrayList();
        arnaud.add("Je sais pas encore");


        expandableListDetail.put("Florian GOSSELIN", florian);
        expandableListDetail.put("Etienne MARTINET", etienne);
        expandableListDetail.put("Kévin PERROT", kevin);
        expandableListDetail.put("Anthony PIAUD", anthony);
        expandableListDetail.put("Eric QUILLEV", eric);
        expandableListDetail.put("Arnaud REGNIER", arnaud);
        return expandableListDetail;
    }
}