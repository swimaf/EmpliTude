package com.martinet.emplitude.Propos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpendableView {

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> etienne = new ArrayList<String>();
        etienne.add("Développeur de la partie 'Emploi du temps', 'Gestion des sons', 'Base de l'application', ");

        List<String> florian = new ArrayList<String>();
        florian.add("Développeur de la partie 'TodoList'");

        List<String> eric = new ArrayList<String>();

        List<String> kevin = new ArrayList<String>();
        kevin.add("Aller le voir si vous avez des problèmes, il est première année");

        List<String> anthony = new ArrayList<String>();
        anthony.add("Développeur du site internet de liste des groupes");

        List<String> arnaud = new ArrayList<String>();
        arnaud.add("Développeur du réveil");

        expandableListDetail.put("Kévin PERROT (Ne fais plus partie du groupe)", kevin);
        expandableListDetail.put("Anthony PIAUD", anthony);
        expandableListDetail.put("Etienne MARTINET", etienne);
        expandableListDetail.put("Florian GOSSELIN", florian);
        expandableListDetail.put("Arnaud REGNIER", arnaud);
        expandableListDetail.put("Eric QUILLEV (Ne fais plus partie du groupe)", eric);


        return expandableListDetail;
    }
}