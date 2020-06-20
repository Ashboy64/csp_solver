package csp_solver;

import java.util.ArrayList;
import java.util.List;

public class Variable<T> {
    private List<T> domain;
    private List<Variable> connectedTo;

    public Variable(List<T> domain) {
        this.domain = domain;
        connectedTo = new ArrayList<>();
    }

    public List<T> getDomain() {
        return domain;
    }

    public void setDomain(List<T> domain) {
        this.domain = domain;
    }

    public void addConnection(Variable v) {
        connectedTo.add(v);
    }

    public List<Variable> getConnections() {
        return connectedTo;
    }
}
