package jmetal.runner;

import jmetal.problem.MultiConstraintsProblem;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import java.util.ArrayList;
import java.util.List;

public class MultiConstraintsOptimizationRunner {
    public static void main(String[] args) {

        MultiConstraintsProblem problem = new MultiConstraintsProblem()
                .setName("multi-constraints")
                .addVariable(0.0, 1.0)
                .addVariable(0.0, 1.0)
                .addVariable(0.0, 1.0)
                .addObjectiveFunction((x) -> -3 * x[0] + 2 * x[1] - 5 * x[2])
                .addConstraintFunction((x) -> -1 * x[0] - 2 * x[1] + x[2] + 2)
                .addConstraintFunction((x) -> -1 * x[0] - 4 * x[1] - x[2] + 4)
                .addConstraintFunction((x) -> -1 * x[0] - x[1] + 3)
                .addConstraintFunction((x) -> -4 * x[1] - x[2] + 6);

        int numberOfVariables = 3;
        CrossoverOperator<DoubleSolution> crossoverOperator = new SBXCrossover(0.9, numberOfVariables);
        MutationOperator<DoubleSolution> mutationOperator = new PolynomialMutation(1.0/numberOfVariables, numberOfVariables);
        SelectionOperator<List<DoubleSolution>, DoubleSolution> selectionOperator = new BinaryTournamentSelection<>();

        Algorithm<DoubleSolution> algorithm = new GeneticAlgorithmBuilder<>(problem, crossoverOperator, mutationOperator)
                .setPopulationSize(200)
                .setMaxEvaluations(25000)
                .setSelectionOperator(selectionOperator)
                .build();
        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
        DoubleSolution solution = algorithm.getResult();
        List<DoubleSolution> poplation = new ArrayList<>(1);
        poplation.add(solution);

        new SolutionListOutput(poplation)
                .setSeparator("\t")
                .setVarFileOutputContext(new DefaultFileOutputContext("/tmp/VAR.tsv"))
                .setFunFileOutputContext(new DefaultFileOutputContext("/tmp/FUN.tsv"))
                .print();
    }
}