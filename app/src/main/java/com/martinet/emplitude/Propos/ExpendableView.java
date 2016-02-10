package com.martinet.emplitude.Propos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpendableView {

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> etienne = new ArrayList<String>();
        etienne.add("Je sais pas encore");

        List<String> florian = new ArrayList<String>();
        florian.add("Je sais pas encore");

        List<String> eric = new ArrayList<String>();
        eric.add("Je sais pas encore");

        List<String> kevin = new ArrayList<String>();
        kevin.add("Je sais pas encore");

        List<String> anthony = new ArrayList<String>();
        anthony.add("Je sais pas encore");

        List<String> arnaud = new ArrayList<String>();
        arnaud.add("Je sais pas encore");

        expandableListDetail.put("Kévin PERROT (Ne fais plus partie du groupe)", kevin);
        expandableListDetail.put("Anthony PIAUD", anthony);
        expandableListDetail.put("Etienne MARTINET", etienne);
        expandableListDetail.put("Florian GOSSELIN", florian);
        expandableListDetail.put("Arnaud REGNIER", arnaud);
        expandableListDetail.put("Eric QUILLEV (Ne fais plus partie du groupe)", eric);


        return expandableListDetail;
    }
}