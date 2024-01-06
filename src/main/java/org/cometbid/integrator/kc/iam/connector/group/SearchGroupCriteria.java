/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.group;

/**
 *
 * @author samueladebowale
 */
public record SearchGroupCriteria(String search, boolean exactMatching, boolean briefRepresentation) {

    // search - search string for group
    // exact - exact match for search
    // first - index of the first element
    // max - max number of occurrences
    // briefRepresentation - if false, return groups with their attributes
}
