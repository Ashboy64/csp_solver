package csp_solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestCSP extends CSP {

    private List<Variable> vars;
    private List<Relation> rels;

    public static void main(String[] args) {
        TestCSP csp = new TestCSP();
        Solver solver = new Solver(csp);

        solver.makeNodeConsistent();
        solver.AC3();

        System.out.println();
        System.out.println(csp.getVariables().get(0).getDomain());
        System.out.println(csp.getVariables().get(1).getDomain());
    }

    public TestCSP() {
        initCSP();
    }

    public void initCSP() {
        vars = new ArrayList<>();
        rels = new ArrayList<>();

        List<Integer> domain1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> domain2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Variable<Integer> var1 = new Variable<>(domain1);
        Variable<Integer> var2 = new Variable<>(domain2);
        vars.add(var1);
        vars.add(var2);

        rels.add(new Relation<Integer>() {

            {
                variables = new ArrayList<>();
                addVariables(Arrays.asList(var1));
            }

            @Override
            public boolean satisfies(Map assignments) {
                return (((Integer) assignments.get(var1)) == 2) || (((Integer) assignments.get(var1)) == 3);
            }
        });

        rels.add(new Relation<Integer>() {

            {
                variables = new ArrayList<>();
                addVariables(Arrays.asList(var1, var2));
            }

            @Override
            public boolean satisfies(Map assignments) {
                int val1 = (Integer) assignments.get(var1);
                int val2 = (Integer) assignments.get(var2);
                return val2 < val1;
            }
        });
    }

    @Override
    public List<Variable> getVariables() {
        return vars;
    }

    @Override
    public List<Relation> getRelations() {
        return rels;
    }
}
