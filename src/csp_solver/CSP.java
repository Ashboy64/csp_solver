package csp_solver;

import java.util.List;

/**
 * Represents a CSP. Will need to have methods to get variables, get domains, get relations, check relations
 */
public abstract class CSP {
    public abstract List<Variable> getVariables();
    public abstract List<Relation> getRelations();
}
