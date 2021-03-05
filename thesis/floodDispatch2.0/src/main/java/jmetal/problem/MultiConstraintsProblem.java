package jmetal.problem;

import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class MultiConstraintsProblem implements DoubleProblem {
    private List<Double> lowerBounds;
    private List<Double> upperBounds;
    private List<Function<Double[], Double>> objectiveFunction;
    private List<Function<Double[], Double>> constraints;

    private String name;
    private OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    private NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;

    public MultiConstraintsProblem() {
        this.lowerBounds = new ArrayList<>();
        this.upperBounds = new ArrayList<>();
        this.objectiveFunction = new ArrayList<>();
        this.constraints = new ArrayList<>();

        this.name = "";
        this.overallConstraintViolationDegree = new OverallConstraintViolation<>();
        this.numberOfViolatedConstraints = new NumberOfViolatedConstraints<>();
    }

    public MultiConstraintsProblem addObjectiveFunction(Function<Double[], Double> objective) {
        this.objectiveFunction.add(objective);
        return this;
    }

    public MultiConstraintsProblem addConstraintFunction(Function<Double[], Double> constraint) {
        this.constraints.add(constraint);
        return this;
    }

    public MultiConstraintsProblem addVariable(double lowerBound, double upperBound) {
        this.lowerBounds.add(lowerBound);
        this.upperBounds.add(upperBound);
        return this;
    }

    public MultiConstraintsProblem setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public int getNumberOfVariables() {
        return this.lowerBounds.size();
    }

    @Override
    public int getNumberOfObjectives() {
        return this.objectiveFunction.size();
    }

    @Override
    public int getNumberOfConstraints() {
        return this.constraints.size();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void evaluate(DoubleSolution solution) {

        Double[] x = new Double[solution.getNumberOfVariables()];
        for (int i = 0; i < x.length; i++) {
            x[i] = solution.getVariableValue(i);
        }

        IntStream.range(0, getNumberOfObjectives())
                .forEach(i -> solution.setObjective(i, this.objectiveFunction.get(i).apply(x)));

        if(getNumberOfConstraints() > 0) {
            double overAllConstraintViolation = 0.0;
            int violatedConstraints = 0;
            for(int i=0;i<getNumberOfConstraints();i++) {
                double violateDegree = this.constraints.get(i).apply(x);
                if(violateDegree >= 0) {
                    overAllConstraintViolation += violateDegree;
                    violatedConstraints += 1;
                }
            }
            this.overallConstraintViolationDegree.setAttribute(solution, overAllConstraintViolation);
            this.numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
        }
    }

    @Override
    public DoubleSolution createSolution() {
        return new DefaultDoubleSolution(this);
    }

    @Override
    public Double getLowerBound(int i) {
        return this.lowerBounds.get(i);
    }

    @Override
    public Double getUpperBound(int i) {
        return this.upperBounds.get(i);
    }
}