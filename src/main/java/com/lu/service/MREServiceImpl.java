package com.lu.service;

import com.lu.mapper.CompoundMapper;
import com.lu.mapper.ReactionMapper;
import com.lu.mapper.SpeciesMapper;
import com.lu.pojo.Compound;
import com.lu.pojo.Path;
import com.lu.pojo.Reaction;
import com.lu.pojo.SpeciesNetwork1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

//mreservice2
@Service
public class MREServiceImpl implements MREService {
    @Autowired
    private CompoundMapper compoundMapper;
    @Autowired
    private ReactionMapper reactionMapper;
    @Autowired
    private SpeciesMapper speciesMapper;

    public static String[] clusterMetabolites = {"C00001", "C00002", "C00003", "C00004", "C00005", "C00006", "C00007", "C00008", "C00009",
            "C00010", "C00011", "C00013", "C00015", "C01342", "C00019", "C00020", "C00035", "C00044", "C00080", "C00131"};//簇代谢物
    public static List<String> clusterMetabolites1 = new ArrayList<String>(Arrays.asList(clusterMetabolites));

    //
    public void setCompoundMapper(CompoundMapper compoundMapper) {
        this.compoundMapper = compoundMapper;
    }

    public void setReactionMapper(ReactionMapper reactionMapper) {
        this.reactionMapper = reactionMapper;
    }

    public void setSpeciesMapper(SpeciesMapper speciesMapper) {
        this.speciesMapper = speciesMapper;
    }
//
//    public static CompoundMapper compoundMapper;
//    public static ReactionMapper reactionMapper;
//    public static EnzymeMapper enzymeMapper;
//    public static SpeciesMapper speciesMapper;
//    public static SpeciesNetworkMapper speciesNetworkMapper;

    /*
       路径上的反应还是要分内外源的
       考虑所有底物的竞争性
        */
    @Override
    public double calculate1(Path path, SpeciesNetwork1 speciesNetwork) throws IOException {
        double scoreOfPath = 0.0;
        double sum = 0.0;
        List<String> noInSPeciesRids = new ArrayList<>();
        Map<String, Double> AStepMap = new HashMap<>();
        for (int i = 0; i < path.getReactionsOfPath().size(); i++) {
            Map<String, Double> rScoreMap = new HashMap<>();//<当前路径上的一步反应，这一步在物种内计算后的分数>
            String rId = path.getReactionsOfPath().get(i).getRId();//路径上的当前反应id
            Compound c = path.getCompoundsOfPath().get(i);  //转化前体c

            List<String> l1 = path.getReactionsOfPath().get(i).getSubstrateId();
            List<String> l2 = path.getReactionsOfPath().get(i).getProductId();
            List<String> subs = l1.contains(c.getCId()) ? l1 : l2;//subs保存所有转换底物id
            //如果当前网络不包含当前反应
            if (!speciesNetwork.getRIdList().contains(rId)) {
                noInSPeciesRids.add(rId);
            }
//            Map<String, Double> cScoreMap = new HashMap<>();
            List<Double> scoreList = new ArrayList<>();
            Map<String, Double> map2 = new HashMap<>();//同一个反应在不同化合物的竞争下的f（r）
            for (String sub : subs) {
                if (clusterMetabolites1.contains(sub))
                    continue;
                Map<String, Double> map = new HashMap<>();//<当前反应的某个底物的所有反应,fr分数>
                //获取转换前体c的一组自然反应
                List<Reaction> rn = getRN(speciesNetwork, sub);
//                //如果rn为空，即物种内无该化合物，反应为纯外源反应，不计算竞争性得分
//                if (rn.size() == 0)
//                    continue;
                //计算rn中每一个边在玻尔兹曼因子考虑下的权重
                Compound subCompound = compoundMapper.getCompoundById(sub);
                for (int j = 0; j < rn.size(); j++) {
                    double result = getfr(speciesNetwork, rn, rn.get(j), subCompound);
                    map.put(rn.get(j).getRId(), result);
                }
                //如果当前反应是外源反应，单独加到map里
                map.putIfAbsent(rId, getfr(speciesNetwork, rn, reactionMapper.getReactionById(rId), subCompound));
                //然后计算当前反应在转化该化合物的所有反应中所占排名分数
                double score = getScoreOfAStep1(map, rId);//score是当前反应在该底物（sub）下的排名分数
                scoreList.add(score);
                map2.put(sub, map.get(rId));
            }
            //当前反应在每个底物的竞争反应中的得分求和计算平均分

            double sum1 = 0.0;
            for (Double score : map2.values()) {
                sum1 += score;
            }
            double result = sum1 / map2.size();//只考虑fr的情况下一个反应的最终分数
            AStepMap.put(rId, result);
            scoreOfPath += result;

            //考虑排名分数下求和
//            sum = sum + scoreList.stream().reduce(Double::sum).get() / scoreList.size();
        }
//        if (AStepMap.size() != path.getReactionsOfPath().size()) {
//            String content = path.toString() + "\n" + speciesNetwork.getSpeciesId() + "\n";
//            writeToTxt("c.txt", "a+", content);
//        }
        return scoreOfPath;//是不比较，只fr的分数和
//        return sum;//竞争分数函数后
    }

    /*

     */
    public Double cScoreMapToRScoreMap(Map<String, Double> cScoreMap) {
        //对于簇代谢物的评分的处理
        if (cScoreMap.size() == 0)
            System.out.println("cScoreMap.size()==0");
        else if (cScoreMap.size() == 1)
            System.out.println("cScoreMap.size()==1");
        else if (clusterMetabolites1.containsAll(cScoreMap.keySet())) {
            System.out.println("clusterMetabolites1.containsAll(cScoreMap.keySet())");
        } else {
            List<String> removeKey = new ArrayList<>();
            for (String s : cScoreMap.keySet()) {
                if (clusterMetabolites1.contains(s))
                    removeKey.add(s);
            }
            for (String s : removeKey)
                cScoreMap.remove(s);
        }

        //根据策略，选择一个底物的分数作为该边的最后评分
        Double sum = 0.0;
        for (String s : cScoreMap.keySet()) {
            sum += cScoreMap.get(s);
        }
        Double result = sum / cScoreMap.size();
        return result;
    }

    /*
    计算每一步的分数，只考虑竞争程度大于等于当前反应的竞争反应（目前）
     */
    public double getScoreOfAStep(Map<String, Double> map, String rId) {
        Double curReactionFr = map.get(rId);
        Map<String, Double> otherMap = new HashMap<>();//其他比当前反应f（r）值高的<反应，对于分数>
//        for (String id : map.keySet()) {
//            if (id.equals(rId))
//                curReactionFr = map.get(id);
//        }
        for (String id : map.keySet()) {
            if (map.get(id) >= curReactionFr)
                otherMap.put(id, map.get(id));
        }
        DecimalFormat df = new DecimalFormat("0.0000");
        double sum = 0.0;
        sum += curReactionFr;
        for (String id : otherMap.keySet()) {
            sum += otherMap.get(id);
        }
        return Double.parseDouble(df.format(curReactionFr / sum));
    }

    /*
     计算每一步的分数，考虑所有
      */
    public double getScoreOfAStep1(Map<String, Double> map, String rId) {
        Map<String, Double> stringDoubleMap = dataStandard(map);
        return stringDoubleMap.get(rId);
    }
    /*
计算每一步的分数，考虑所有
            if (map.get(id) >= curReactionFr)
                otherMap.put(id, map.get(id));
        }
        DecimalFormat df = new DecimalFormat("0.0000");
        double sum = 0.0;
        sum += curReactionFr;
        for (String id : otherMap.keySet()) {
            sum += otherMap.get(id);
        }
        return Double.parseDouble(df.format(curReactionFr / sum));
    }
    public double getScoreOfPath() {
        return 1.1;
    }
    /*
    计算竞争分数
    目前跟mre一样只考虑了关键化合物的竞争关系
     */

    /**
     * 计算某个转换前体C的反应的f(r)
     *
     * @param speciesNetwork 物种网络
     * @param rn             转化前体C的所有天然反应的集合
     * @param curReaction    当前反应
     * @param c              前体C
     * @return 竞争分数
     */
    @Override
    public double getfr(SpeciesNetwork1 speciesNetwork, List<Reaction> rn, Reaction curReaction, Compound c) {
        double R = 8.314;//气体常数值是8.314J/(mol·K)
        double T = 298.15;//绝对温度 298.15K
        double mean = energyFromStringToDouble(curReaction.getEnergy());//当前反应吉布斯自由能（不包含方向）
        double energy = judgeDirectionOfReaction(c, curReaction) == 1 ? mean : -mean;//当前反应吉布斯自由能（判断方向后）
        double b1 = Math.pow(Math.E, -energy / (R * T));//e-△G/RT
        double b2 = 0;
//        for (String id : rn) {
//            if (id.equals(rId)) {
//                String fre = reactionMapper.getEnergyByRId(id);
//                b2 += Math.pow(Math.E, -energyFromStringToDouble(fre) / (R * T));
//            }
//        }
        //计算b2的值
        //要保证数据库反应表达式和自由能方向的一致性,验证没问题
        for (Reaction reaction : rn) {
            if (!reaction.getRId().equals(curReaction.getRId())) {
                double m = energyFromStringToDouble(reaction.getEnergy());
                double e = judgeDirectionOfReaction(c, reaction) == 1 ? m : -m;
                b2 += Math.pow(Math.E, -e / (R * T));
            }
        }
        DecimalFormat df = new DecimalFormat("0.0000");//将计算结果格式化,保留小数点后四位
        return Double.parseDouble(df.format(Math.log(b1 / (1 + b1 + b2))));//在反应r中转换C的每条边都具有权重 log f ( r )
    }

    /**
     * 判断反应的实际方向
     *
     * @param substrate 实际反应的某个底物
     * @param reaction  当前反应
     * @return 正向返回1, 反向返回-1
     */
    public int judgeDirectionOfReaction(Compound substrate, Reaction reaction) {
        //所谓正向，是指和kegg反应的表达式从左到右的方向
        return reaction.getSubstrateId().contains(substrate.getCId()) ? 1 : -1;
    }

    /*
    获取Rn：一组在宿主中可以转化前体c的一组天然反应id的集合
    如果某物种内没有化合物c,也就是没有对应转换反应。。。。
    感觉分三种
    一种是内源反应
    一种是外援反应，但物种内有前体化学物c
    一种是外援反应，但物种内无前体化学物c
     */
    @Override
    public List<Reaction> getRN(SpeciesNetwork1 speciesNetwork, String cId) {
        int[][] network = speciesNetwork.getNetwork();
        int index = speciesNetwork.getCIdList().indexOf(cId);
        if (index == -1) {
            System.out.println(speciesNetwork.getSpeciesId()+"未找到");
            return new ArrayList<>();
        }
        List<Reaction> rIdList = new ArrayList<>();
        for (int i = 0; i < network.length; i++) {
            if (network[i][index] != 0)
                rIdList.add(reactionMapper.getReactionById(speciesNetwork.getRIdList().get(i)));
        }
        return rIdList;
    }


    /**
     * 将reaction对象中字符串类型的energy转换成Double类型
     * 如果自由能数据库中是“nan”，记为0
     *
     * @param energy 吉布斯自由能
     * @return double；类型值
     */
    public double energyFromStringToDouble(String energy) {
//        如果自由能数据库中是“nan”或为空，记为0
        if ("nan".equals(energy) || energy == null)
            return 0;
        else
            return Double.parseDouble(energy);
    }

    public void writeToTxt(String fileName, String method, String content) throws IOException {
        FileWriter fw;
        if (method.equals("a") || method.equals("a+"))
            fw = new FileWriter(fileName, true);
        else {
            fw = new FileWriter(fileName);
        }
        BufferedWriter bf = new BufferedWriter(fw);
        bf.write(content);
        bf.flush();
        bf.close();
        fw.close();
    }

    public void test1() throws IOException {
        SpeciesNetworkServiceImpl speciesNetworkService2 = new SpeciesNetworkServiceImpl();
        String[] ss = new String[]{"aaa", "aab", "aac", "aace", "aaci", "aacn", "aaci", "aad", "aae", "aaf", "aag"};
        SpeciesNetwork1 speciesNetwork = speciesNetworkService2.getArrayFromTxt("eco");
        String path = "C00022->R00226->C06010->R05071->C04181->R04440->C04272->R04441->C00141->R01434->C00183";
        List<String> speciesIdList = speciesMapper.getAllSpeciesId();

        PathServiceImpl pathService = new PathServiceImpl();
        long l6 = System.currentTimeMillis();
        Path completePath = pathService.createCompletePath(path);
        MREServiceImpl mreService = new MREServiceImpl();
        Map<String, Double> scoreMap = new HashMap<>();
        long l1 = System.currentTimeMillis();
        for (int i = 0; i < speciesIdList.size(); i++) {
            String speciesId = speciesIdList.get(i);
            SpeciesNetwork1 speciesNetwork1 = speciesNetworkService2.getArrayFromTxt(speciesId);
            double scoreOfPath = mreService.calculate1(completePath, speciesNetwork1);
            scoreMap.put(speciesId, scoreOfPath);
        }
        dataStandard(scoreMap);
    }

    /*
    min-max标准化
     */
    @Override
    public Map<String, Double> dataStandard(Map<String, Double> map) {
        double max = 0;
        double min = 0;
        for (String key : map.keySet()) {
            max = map.get(key);
            min = map.get(key);
            break;
        }
        for (Double x : map.values()) {
            if (x > max)
                max = x;
            if (x < min)
                min = x;
        }
        Map<String, Double> newMap = new HashMap<>();
        DecimalFormat df = new DecimalFormat("0.0000");//将计算结果格式化,保留小数点后四位
        double newValue = 0;
        for (String key : map.keySet()) {
            newValue = (map.get(key) - min) / (max - min);
            if (Double.compare(newValue, Double.NaN) == 0) {
                newMap.put(key, 1.0);
            }
            else{
            newMap.put(key, Double.valueOf(df.format(newValue)));
            }
        }
        return newMap;
    }

}
