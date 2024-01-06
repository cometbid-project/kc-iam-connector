/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.group;

/**
 *
 * @author samueladebowale
 */
public record Group(String id, String name, String description) {

    static Group createGroup(String id, String groupName, String groupDesc) {
        return new Group(id, groupName, groupDesc);
    }

}
