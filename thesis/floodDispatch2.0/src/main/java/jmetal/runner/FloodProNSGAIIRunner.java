package jmetal.runner;

import com.sun.org.apache.regexp.internal.RE;
import jmetal.problem.FloodProProblem;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.jcp.xml.dsig.internal.dom.DOMUtils;
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
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import until.EasyExcelUtil;
import until.ExcelUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FloodProNSGAIIRunner extends AbstractAlgorithmRunner {
    public static void main(String[] args) throws JMetalException, FileNotFoundException {
        Problem<DoubleSolution> problem;
        Algorithm<List<DoubleSolution>> algorithm;
        CrossoverOperator<DoubleSolution> crossover;
        MutationOperator<DoubleSolution> mutation;
        SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
        String problemName = "floodProThreeStation";
        double[] levelStart = {570.0, 370.0, 145.0};//三个库的起调水位
        double reserveStorage = 20.0;//亿m³
        HashMap<String, List<Double>> levelCombination = new HashMap<>();
        int[] levelMaxMin = {588, 593};
        levelCombination = getLevelCombination(reserveStorage, 3, "XLD", levelMaxMin, 1);

        int[] period = {88, 100};


        for (int i = 0; i < levelCombination.get("XLD").size(); i++) {
            HashMap<String, Double> constrains = new HashMap<>();
            constrains.put("XLD", levelCombination.get("XLD").get(i));
            constrains.put("XJB", levelCombination.get("XJB").get(i));

            problem = new FloodProProblem(122, 3, levelStart, constrains, period);

            double crossoverProbability = 0.9;
            double crossoverDistributionIndex = 20.0;
            crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

            double mutationProbability = 1.0 / problem.getNumberOfVariables();
            double mutationDistributionIndex = 20.0;
            // 多项式变异
            mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

            // N元锦标赛选择，传入比较器
            selection = new BinaryTournamentSelection<DoubleSolution>(new RankingAndCrowdingDistanceComparator<DoubleSolution>());

            algorithm = new NSGAII45<DoubleSolution>(problem, 50000, 50000, crossover, mutation,
                    selection, new SequentialSolutionListEvaluator<DoubleSolution>());


            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                    .execute();

            List<DoubleSolution> population = algorithm.getResult();

            long computingTime = algorithmRunner.getComputingTime();

            JMetalLogger.logger.info("第" + i + "次计算时间: " + computingTime + "ms");

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

            //保存结果
            String basePath = "data/result/" + problemName + "/";
            boolean flag = ExcelUtil.exportExcelXLSX(population, "非劣前沿结果", tableHead, tableHeadDimension, basePath + problemName + "NSGAII第" + i + "次.xlsx");
            if (flag) {
                System.out.println("保存文件成功！");
            } else {
                System.out.println("保存文件失败！");
            }

            break;
        }
    }

    /**
     * @param reserveStorage
     * @param T
     * @return
     */
    private static HashMap<String, List<Double>> getLevelCombination(double reserveStorage, int T, String stationName, int[] levelMaxMin, int step) {
        SplineInterpolator splineInterpolator = new SplineInterpolator();//样条差值

        double[][] ZVCurvedOfxld = EasyExcelUtil.readTable("2溪洛渡基本特征曲线", 0);
        PolynomialSplineFunction ZVCurvedOfXLD = splineInterpolator.interpolate(ZVCurvedOfxld[0], ZVCurvedOfxld[1]);
        PolynomialSplineFunction VZCurvedOfXLD = splineInterpolator.interpolate(ZVCurvedOfxld[1], ZVCurvedOfxld[0]);

        double[][] ZVCurvedOfxjb = EasyExcelUtil.readTable("3向家坝基本特征曲线", 0);
        PolynomialSplineFunction ZVCurvedOfXJB = splineInterpolator.interpolate(ZVCurvedOfxjb[0], ZVCurvedOfxjb[1]);
        PolynomialSplineFunction VZCurvedOfXJB = splineInterpolator.interpolate(ZVCurvedOfxjb[1], ZVCurvedOfxjb[0]);

        double V_XLD_FHG = ZVCurvedOfXLD.value(600.0);
        double V_XLD_XX = ZVCurvedOfXLD.value(560.0);
        double V_XJB_FHG = ZVCurvedOfXJB.value(380.0);
        double V_XJB_XX = ZVCurvedOfXJB.value(370.0);

        HashMap<String, List<Double>> ans = new HashMap<>();
        List<Double> Z_XLD = new ArrayList<>();
        List<Double> Z_XJB = new ArrayList<>();
        if (stationName.equals("XLD")) {
            double Z_XLDNow = levelMaxMin[0];
            while (Double.doubleToLongBits(Z_XLDNow) <= Double.doubleToLongBits(levelMaxMin[1])) {
                Z_XLDNow = Z_XLDNow + step;
                Z_XLD.add(Z_XLDNow);

                double V_XLD = ZVCurvedOfXLD.value(Z_XLDNow);
                double V_left_XLD = V_XLD_FHG - V_XLD;

                if (Double.doubleToLongBits(V_left_XLD) >= reserveStorage) {
                    Z_XJB.add(380.0);
                } else {
                    double V_left_XJB = reserveStorage - V_left_XLD;
                    double V_XJB = V_XJB_FHG - V_left_XJB;
                    Double Z_XJBNow = VZCurvedOfXJB.value(V_XJB);
                    if (Double.doubleToLongBits(Z_XJBNow) < 370.0) {
                        Z_XJB.remove(Z_XJB.size() - 1);
                        break;
                    }
                    Z_XJB.add(Z_XJBNow);
                }
            }
        }

        if (stationName.equals("XJB")) {
            double Z_XJBNow = levelMaxMin[0];
            while (Double.doubleToLongBits(Z_XJBNow) < Double.doubleToLongBits(levelMaxMin[1])) {
                Z_XJBNow = Z_XJBNow + step;
                Z_XJB.add(Z_XJBNow);

                double V_XJB = ZVCurvedOfXJB.value(Z_XJBNow);
                double V_left_XJB = V_XJB_FHG - V_XJB;

                if (Double.doubleToLongBits(V_left_XJB) >= Double.doubleToLongBits(reserveStorage)) {
                    Z_XLD.add(600.0);
                } else {
                    double V_left_XLD = reserveStorage - V_left_XJB;
                    double V_XLD = V_XLD_FHG - V_left_XLD;
                    Double Z_XLDNow = VZCurvedOfXLD.value(V_XLD);
                    if (Double.doubleToLongBits(Z_XLDNow) < 560.0) {
                        Z_XLD.remove(Z_XJB.size() - 1);
                        break;
                    }
                    Z_XLD.add(Z_XLDNow);
                }
            }
            Z_XJB.add((double) levelMaxMin[1]);
        }

        ans.put("XLD", Z_XLD);
        ans.put("XJB", Z_XJB);
        return ans;
    }
}
