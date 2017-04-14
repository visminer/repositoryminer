package org.repositoryminer.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by gustavoramos00 on 01/04/2017.
 */
public class MetricData {

    private Integer value;
    private Set<String> callers;

    public MetricData() {
        this.value = 0;
        this.callers = new HashSet<>();
    }

    public Integer getValue() {
        return value;
    }

    public Set<String> getCallers() {
        return callers;
    }

    public void incrementValue(){
        value++;
    }

    public void addAllCallers(Set<String> callers){
        this.callers.addAll(callers);
    }

    public boolean addCaller(String caller) {
        return this.callers.add(caller);
    }

    public void incrementValue(Integer increment) {
        this.value += increment;
    }
}
