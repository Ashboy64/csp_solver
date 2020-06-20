package csp_solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Relation<T> {

    protected List<Variable<T>> variables;

    public Relation() {
        variables = new ArrayList<>();
    }

    protected void addVariables(List<Variable<T>> vars) {
        for (Variable<T> v : vars) {
            List<Variable<T>> others = getOthers(vars, v);
            for (Variable<T> o : others) {
                v.addConnection(o);
            }
            variables.add(v);
        }
    }

    private List<Variable<T>> getOthers(List<Variable<T>> vars, Variable<T> var) {
        List<Variable<T>> out = new ArrayList<>();
        for (Variable<T> v : vars) {
            if (v != var) {
                out.add(v);
            }
        }
        return out;
    }

    public abstract boolean satisfies(Map<Variable<T>, T> assignments);
    public List<Variable<T>> getVariables() {
        return variables;
    }
}
