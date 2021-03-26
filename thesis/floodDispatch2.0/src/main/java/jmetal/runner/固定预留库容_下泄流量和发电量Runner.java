package jmetal.runner;

import jmetal.problem.固定预留库容_下泄流量和发电量;
import jmetal.problem.预留库容和发电量;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII45;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import until.ExcelUtil;

import java.util.ArrayList;
import java.util.List;

public class 固定预留库容_下泄流量和发电量Runner extends AbstractAlgorithmRunner {
    public static void main(String[] args) {
        Problem<DoubleSolution> problem;
        Algorithm<List<DoubleSolution>> algorithm;
        CrossoverOperator<DoubleSolution> crossover;
        MutationOperator<DoubleSolution> mutation;
        SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
        String problemName = "固定预留库容_下泄流量和发电量";
        double[] levelStart = {560.0, 370.0};//起调水位
        double reserveStorage = 25.0;

        int[] T = {71, 85};


        problem = new 固定预留库容_下泄流量和发电量(T, 3, levelStart,reserveStorage);

        double crossoverProbability = 0.9;
        double crossoverDistributionIndex = 20.0;
        crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

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

        long computingTime = algorithmRunner.getComputingTime();

        JMetalLogger.logger.info("计算时间: " + computingTime + "ms");

        //设置表头
        ArrayList<String> tableHead = new ArrayList<>();
        ArrayList<String> tableHeadDimension = new ArrayList<>();
        for (int j = 0; j < population.get(0).getNumberOfVariables(); j++) {
            tableHead.add("x" + (j + 1));
            tableHeadDimension.add(" ");
        }
        for (int j = 0; j < population.get(0).getNumberOfObjectives(); j++) {
            tableHead.add("obj" + (j + 1));
            tableHeadDimension.add(" ");
        }

        tableHead.add("power");
        tableHeadDimension.add(" ");

        tableHead.add("Qmax");
        tableHeadDimension.add(" ");

        tableHead.add("n");
        tableHeadDimension.add(" ");

        tableHead.add("V_left_min");
        tableHeadDimension.add(" ");





        //保存结果
        String basePath = "data/result/" + problemName + "/";
        boolean flag = ExcelUtil.exportExcelXLSX(population, "非劣前沿结果", tableHead, tableHeadDimension, basePath + problemName + "NSGAII" + ".xlsx");
        if (flag) {
            System.out.println("保存文件成功！");
        } else {
            System.out.println("保存文件失败！");
        }
    }
}
