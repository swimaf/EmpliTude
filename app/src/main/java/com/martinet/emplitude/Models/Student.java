package com.martinet.emplitude.Models;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Global;
import com.martinet.emplitude.Tool.Fichier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinet on 02/02/16.
 */
public class Student extends User implements Serializable {

    private List<Group> groups;
    private Group defaultGroup;

    public Student(Group group){
        super();
        this.groups = new ArrayList<>();
        this.groups.add(group);
        this.defaultGroup = group;
    }


    public String toString(){
        return "Etudiant "+defaultGroup.getName();
    }



    public Group getDefaultGroup() {
        return defaultGroup;
    }

    public void setDefaultGroup(Group defaultGroup) {
        this.defaultGroup = defaultGroup;
        Fichier.write(Constants.Files.STUDENT, Global.global, Global.global.getStudent());
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Group getGroupById(String id) {
        for(Group group:groups) {
            if(group.getIdentifiant().equals(id)) {
                return group;
            }
        }
        return getDefaultGroup();
    }

    public void save() {
        Fichier.write(Constants.Files.STUDENT, Global.global, this);
    }

    public void delete() {
        Global.global.getApplicationContext().deleteFile(Constants.Files.STUDENT);
    }
}
