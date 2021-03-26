package jmetal.runner;

import jmetal.problem.FloodProMaxPlusSXSingleDUIBIProblem;
import jmetal.problem.FloodProblem;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII45;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import until.ExcelUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static until.CalculateUtil.getQs;

public class FloodProMaxPlusSXSingleDUIBIProblemRunner {
    public static void main(String[] args) throws JMetalException, FileNotFoundException {
        Problem<DoubleSolution> problem;
        Algorithm<List<DoubleSolution>> algorithm;
        CrossoverOperator<DoubleSolution> crossover;
        MutationOperator<DoubleSolution> mutation;
        SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
        String referenceParetoFront = "";
        String problemName = "FloodProMaxPlusSXSingleDUIBIProblem";
        double[] level = {145.5};
        problem = new FloodProMaxPlusSXSingleDUIBIProblem(64, 1, level);

        double crossoverProbability = 0.9;
        double crossoverDistributionIndex = 20.0;
        crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

//        double mutationProbability = Math.pow(1.0 / problem.getNumberOfVariables(),0.8);
        double mutationProbability = 1.0 / problem.getNumberOfVariables();

        double mutationDistributionIndex = 20.0;
        // 多项式变异
        mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

        // N元锦标赛选择，传入比较器
        selection = new BinaryTournamentSelection<DoubleSolution>(new RankingAndCrowdingDistanceComparator<DoubleSolution>());

        algorithm = new NSGAII45<DoubleSolution>(problem, 1000000, 100, crossover, mutation,
                selection, new SequentialSolutionListEvaluator<DoubleSolution>());

        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute();

        List<DoubleSolution> population = algorithm.getResult();
        double[][] Q = getQs(population);


        long computingTime = algorithmRunner.getComputingTime();

        JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

        ArrayList<String> tableHead = new ArrayList<>();
        ArrayList<String> tableHeadDimension = new ArrayList<>();
        for (int i = 0; i < population.get(0).getNumberOfVariables(); i++) {
            tableHead.add("x" + (i + 1));
            tableHeadDimension.add(" ");
        }
        for (int i = 0; i < population.get(0).getNumberOfObjectives(); i++) {
            tableHead.add("obj" + (i + 1));
            tableHeadDimension.add(" ");
        }

        ArrayList<String> tableHeadQ = new ArrayList<>();
        ArrayList<String> tableHeadDimensionQ = new ArrayList<>();
        for (int i = 0; i < population.get(0).getNumberOfVariables(); i++) {
            tableHeadQ.add("Q" + (i + 1));
            tableHeadDimensionQ.add(" ");
        }
//        tableHead.add("rank");
//        tableHeadDimension.add(" ");
//        tableHead.add("crowdingDis");
//        tableHeadDimension.add(" ");

        //保存结果
        String basePath = "data/result/" + problemName + "/";
        boolean flag = ExcelUtil.exportExcelXLSX(population, "非劣前沿结果", tableHeadQ, tableHeadDimensionQ, basePath + problemName + "NSGAII.xlsx");
        if (flag) {
            System.out.println("保存文件成功！");
        } else {
            System.out.println("保存文件失败！");
        }


        //保存流量结果
        String basePathQ = "data/result/" + problemName + "/";
        boolean flagQ = ExcelUtil.exportExcelXLSX(Q, "非劣前沿结果", tableHeadQ, tableHeadDimensionQ, basePathQ + problemName + "QNSGAII.xlsx");
        if (flagQ) {
            System.out.println("保存文件成功！");
        } else {
            System.out.println("保存文件失败！");
        }
    }

}
