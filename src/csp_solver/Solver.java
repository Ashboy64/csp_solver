package csp_solver;

import java.util.*;

public class Solver {
    private final CSP csp;

    public Solver(CSP csp) {
        this.csp = csp;
    }

    public void makeNodeConsistent() {
        List<Relation> rels = csp.getRelations();

        for (Relation r : rels) {
            if (r.getVariables().size() == 1) {
                Variable var = (Variable) r.getVariables().get(0);
                Iterator i = var.getDomain().iterator();
                while (i.hasNext()) {
                    Map<Variable, Object> assignment = new HashMap<>();
                    assignment.put(var, i.next());
                    if (!r.satisfies(assignment)) {
                        i.remove();
                    }
                }
            }
        }
    }

    public void AC3() {
        Queue<Arc> q = new LinkedList<>();

        for (Relation rel : csp.getRelations()) {
            if (rel.getVariables().size() == 2) {
                q.add(new Arc(rel, (Variable) rel.getVariables().get(0), (Variable) rel.getVariables().get(1)));
                q.add(new Arc(rel, (Variable) rel.getVariables().get(1), (Variable) rel.getVariables().get(0)));
            }
        }

        while (!q.isEmpty()) {
            Arc toProcess = q.remove();
            boolean revised = revise(toProcess);

            if (revised) {
                q.addAll(getNeighbours(toProcess.getSecond()));
            }
        }
    }

    private List<Arc> getNeighbours(Variable first) {
        List<Arc> out = new ArrayList<>();

        for (Relation rel : csp.getRelations()) {
            if (rel.getVariables().contains(first) && rel.getVariables().size() == 2) {

                // Find the other variable it is linked to
                Variable second;
                if (rel.getVariables().get(0).equals(first)) {
                    second = (Variable) rel.getVariables().get(1);
                } else {
                    second = (Variable) rel.getVariables().get(0);
                }

                out.add(new Arc(rel, first, second));
            }
        }

        return out;
    }

    private boolean revise(Arc toProcess) {
        Relation rel = toProcess.getRelation();
        Variable first = toProcess.getFirst(); // First's domain will be processed
        Variable second = toProcess.getSecond();
        Iterator i = first.getDomain().iterator();

        boolean revised = false;
        while (i.hasNext()) {
            Object val1 = i.next();
            boolean toRemove = true;

            for (Object val2 : second.getDomain()) {
                Map<Variable, Object> assignment = new HashMap<>();
                assignment.put(first, val1);
                assignment.put(second, val2);

                if (rel.satisfies(assignment)) {
                    toRemove = false;
                    break;
                }
            }

            if (toRemove) {
                i.remove();
                revised = true;
            }
        }
        return revised;
    }

    private void PC2() {
        AC3();

        // Generate all possible triplets linked by relations: A -> B -> C. Make B path consistent with others

    }
}

class Path {
    
}

class Arc {
    private Relation rel;
    private Variable first;
    private Variable second;

    public Arc(Relation rel, Variable first, Variable second) {
        this.rel = rel;
        this.first = first;
        this.second = second;
    }

    public Relation getRelation() {
        return rel;
    }

    public Variable getFirst() {
        return first;
    }

    public Variable getSecond() {
        return second;
    }
}