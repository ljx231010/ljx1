package com.lu.service;

import com.lu.mapper.PathMapper;
import com.lu.mapper.SpeciesMapper;
import com.lu.pojo.*;
import com.lu.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class SpeciesNetworkServiceImpl implements SpeciesNetworkService {
    @Autowired
    private SpeciesMapper speciesMapper;
    public static String[] clusterMetabolites = {"C00001", "C00002", "C00003", "C00004", "C00005", "C00006", "C00007", "C00008", "C00009",
            "C00010", "C00011", "C00013", "C00015", "C01342", "C00019", "C00020", "C00035", "C00044", "C00080", "C00131"};//簇代谢物
    public static List<String> clusterMetabolites1 = new ArrayList<String>(Arrays.asList(clusterMetabolites));
    @Autowired
    private PathMapper pathMapper;

    @Qualifier("PathServiceImpl")
    @Autowired
    PathService pathService;
    @Qualifier("MREServiceImpl")
    @Autowired
    MREService mreService;

    public SpeciesMapper getSpeciesMapper() {
        return speciesMapper;
    }

    public void setSpeciesMapper(SpeciesMapper speciesMapper) {
        this.speciesMapper = speciesMapper;
    }

    @Override
    public List<KeyValue> calculateScoreOfSpecies(String path, Double inOut, Double subAndProduct, Double compete) throws IOException {
        //        String path = "C00022->R00196->C00186->R01449->C00827->R02963->C00894->R03045->C05668->R03157->C01013";
//        String path = "C00082->R00737->C00081->R01616->C00223->R01613->C06561->R02446->C00509";
//        String path = "C00024->R01978->C00356->R02082->C00418->R02245->C01107->R03245->C01143->R01121->C00129->R02003->C00448->R07630->C16028->R10026->C20309";
//        String path = "C00129->R02061->C11356->R07916->C05421->R09716->C05432";
//        String path ="C00116->R00847->C00093->R08657->C00111->R01016->C00546->R02527->C00937->R03080->C02912";
//        String path = "C00082->R00737->C00811->R01616->C00223->R01613->C06561->R02446->C00509->C01477";
//        String path = "C00082->R00737->C00811->R01616->C00223->R01614->C03582";
//        String path = "C00074->R00341->C00036->R00351->C00158->R01325->C00417->R01900->C00311->R00709->C00026->R09784->C06547";
        //刚才一不小心，没有注释的path，导致后面传进来的path变成定值了，耽误了很多时间
//        path = " C00074->R00199->C00022->C00024->R00351->C00158->R01325->C00417->R01900->C00311->R00709->C00026->R09784->C06547";
//        String path = "C00022->R00209->C00024->R01359->C00332->R01358->C00164->R05735->C00207->R01550->C01845";
//        MREService1 mreService = new MREService1();//MRE1：
//        MREServiceImpl mreService = new MREServiceImpl();//MRE2：考虑反应所有底物的竞争性
        Path completePath = pathService.createCompletePath(path);
        if (completePath == null)
            return null;
        Map<String, Double> tempScoreMap1 = pathService.r1(completePath);//<物种id,内源性计算评分>
        Map<String, Double> scoreMap1 = dataStandard(tempScoreMap1);
        Map<String, Double> scoreMap2;//<物种id,路径在该物种中的竞争评分>
        Map<String, Double> tempScoreMap2 = new HashMap<>();//<物种id,路径在该物种中的竞争评分>
        List<String> speciesIdList = speciesMapper.getAllSpeciesId();
        for (int i = 0; i < speciesIdList.size(); i++) {
            String speciesId = speciesIdList.get(i);
            long t1 = System.currentTimeMillis();
            SpeciesNetwork1 speciesNetwork1 = getArrayFromTxt(speciesId);
            long t2 = System.currentTimeMillis();
            double scoreOfPath = mreService.calculate1(completePath, speciesNetwork1);
            tempScoreMap2.put(speciesId, scoreOfPath);
        }
        scoreMap2 = mreService.dataStandard(tempScoreMap2);
        Map<String, Double> scoreMap3 = test1(completePath);//底物和产物
        Map<String, Double> finalMap = new HashMap<>();
        double inOut1 = 0.4;
        double subAndProduct1 = 0.2;
        double compete1 = 0.4;
        if (inOut != 0 || subAndProduct != 0 || compete != 0) {
            inOut1 = inOut;
            subAndProduct1 = subAndProduct;
            compete1 = compete;
        }
        for (String key : scoreMap2.keySet()) {
            double v = scoreMap3.getOrDefault(key, 0.0);
            //score得分分计算
            double value = inOut1 * scoreMap1.getOrDefault(key, 0.0) + compete1 * scoreMap2.getOrDefault(key, 0.0) - subAndProduct1 * v;
            finalMap.put(key, value);
        }
        Map<String, Double> STDFinalMap = dataStandard(finalMap);
//        for (String key1 : scoreMap1.keySet())
//            finalMap.putIfAbsent(key1, scoreMap1.get(key1));
//        for (String key2 : scoreMap2.keySet())
//            finalMap.putIfAbsent(key2, scoreMap2.get(key2));
        //map放到列表里方便排序
        List<KeyValue> l1 = new ArrayList<>();
        judgePath(path, STDFinalMap);
        for (String key : STDFinalMap.keySet()) {
            l1.add(new KeyValue(key, STDFinalMap.get(key)));
        }
        //物种得分降序排列
        l1.sort((o1, o2) -> {
            double result = o1.getValue() - o2.getValue();
            if (result < 0)
                return 1;
            else if (result > 0)
                return -1;
            else
                return 0;
        });
        System.out.println("------calculateScoreOfSpecies()-------");
        return l1;
    }

    /**
     * 从文件读取创建代谢网络矩阵
     *
     * @param speciesId 物种id
     * @return 代谢网络矩阵
     * @throws IOException io异常
     */
    @Override
    public SpeciesNetwork1 getArrayFromTxt(String speciesId) throws IOException {
        String fileName = "";
        if (speciesId.equals("prn"))
            fileName = "prn1";
        else if (speciesId.equals("con"))
            fileName = "con1";
        else
            fileName = speciesId;
        List<String[]> l1 = new ArrayList<>();//底物下标
        List<String[]> l2 = new ArrayList<>();//产物下标
        List<String> ridList = new ArrayList<>();//反应列表
        FileUtils.writeToTxt("d:/data/shiyong.txt", "a+", (speciesId + "\n"));
        String baseSrc = "D:/data/network/%s.txt";
//        String baseSrc = "com/lu/data/network/%s.txt";
        FileReader fr = new FileReader(String.format(baseSrc, fileName));
        BufferedReader br = new BufferedReader(fr);
        String s = "";
        String line = null;
        String[] cids = br.readLine().split(",");//第一行为化合物行
        while ((line = br.readLine()) != null) {
            String[] s1 = line.split(" ");
            ridList.add(s1[0]);
            String d1 = s1[1];//1,200
            String p1 = s1[2];
            l1.add(d1.split(","));
            l2.add(p1.split(","));
        }
        int[][] a1 = new int[ridList.size()][cids.length];
        if (l1.size() != l2.size() || l1.size() != ridList.size())
            System.out.println("不相等");
        for (int i = 0; i < ridList.size(); i++) {
            for (String s1 : l1.get(i)) {
                a1[i][Integer.parseInt(s1)] = -1;
            }
            for (String s1 : l2.get(i)) {
                a1[i][Integer.parseInt(s1)] = 1;
            }
        }
        return new SpeciesNetwork1(speciesId, new ArrayList<>(Arrays.asList(cids)), ridList, a1);
    }

    public List<Message> substrateAndProduct1(Path completePath, SpeciesNetwork1 speciesNetwork) throws IOException {
        List<Reaction> reactionsOfPath = completePath.getReactionsOfPath(); //路径上所有反应
        ArrayList<Compound> compoundsOfPath = completePath.getCompoundsOfPath();//路径上所有化合物
        String finalProductId = compoundsOfPath.get(compoundsOfPath.size() - 1).getCId();//；路径的最终产物
        List<List<String>> lists = get2(completePath); //[底物集合,产物集合]
        Map<String, Integer> substrateOfPreReaction1 = new HashMap<>();
        Map<String, Integer> subsOfReaction = new HashMap<>();  //<路径反应底物，在该路径中对应的出现次数>,可用于判断是否有底物竞争
        Map<String, Integer> productOfPreReaction1 = new HashMap<>();//<路径反应产物，对应的出现次数>,一步一统计，也可用于判断是否有共同产物生成
        List<String> message = new ArrayList<>();//用来记录有关系的化合物相关信息
        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < reactionsOfPath.size(); i++) {
            Reaction currentReaction = reactionsOfPath.get(i);
            Compound currentsubCompound = compoundsOfPath.get(i);
            List<String> l1 = reactionsOfPath.get(i).getSubstrateId();
            List<String> l2 = reactionsOfPath.get(i).getProductId();
            List<String> substratesId;//实际反应的底物
            List<String> productsId;//实际反应的产物

            if (!l1.contains(currentsubCompound.getCId())) {
                substratesId = l2;
                productsId = l1;
            } else {
                substratesId = l1;
                productsId = l2;
            }
            //下面是对于底物的判断
            for (String s : substratesId) {
                if (clusterMetabolites1.contains(s))
                    continue;
                boolean flag1 = true;
                boolean flag2 = false;
                int index = speciesNetwork.getCIdList().indexOf(s);
                //如果index==-1，说明该化合物不在该物种内
                if (index == -1) {
                    flag1 = false;
//                    System.out.println(s + "物种内不存在");
                    message.add(s + "物种内不存在");
//                    messageList.add(new Message(s, currentReaction.getRId(), "物种内不存在"));
                }

                //底物是之前反应生成
                if (productOfPreReaction1.containsKey(s)) {
                    flag2 = true;
//                    productOfPreReaction1.merge(s, 1, Integer::sum);
                }
                //
                if (!flag2) {
//                    System.out.println(speciesNetwork.getSpeciesId());
//                    System.out.println(currentReaction.getRId() + "\t\t" + currentReaction.getEquation());
//                    System.out.println("之前反应不生成:" + s);
                    message.add("之前反应不生成:" + s);
//                    messageList.add(new Message(s, currentReaction.getRId(), "之前反应不生成"));
                }
                if (!(flag1 || flag2)) {
//                    System.out.println("底物判断双无:" + s);
//                    message.add("底物判断双无:" + s);
//                    System.out.println("--------------");
                    messageList.add(new Message(s, currentReaction.getRId(), "底物判断双无"));
                }
            }
            //把当前反应的产物加到之前反应的map中
            for (String pid : productsId) {
                productOfPreReaction1.merge(pid, 1, Integer::sum);
                //如果路径中有两步都有共同产物生成，势必会影响路径表达效率(簇代谢物除外)
            }

            //对于产物的处理
            for (String s : productsId) {
                //如果是所需要的的最终产物
                if (finalProductId.equals(s))
                    continue;
                //如果是簇代谢物
                if (clusterMetabolites1.contains(s))
                    break;
                boolean flag1 = true;
                boolean flag2 = false;
                int index = speciesNetwork.getCIdList().indexOf(s);

                //如果index等于-1，说明产物不存在物种内，即不会被消耗
                if (index == -1) {
                    flag1 = false;
//                    System.out.println(s + "物种内无法消耗");
                    message.add(s + "物种内无法消耗");
//                    messageList.add(new Message(s, currentReaction.getRId(), "物种内无法消耗"));
                }


                //作为底物被路径上其他反应消耗
                if (lists.get(0).contains(s)) {
                    flag2 = true;
                }
                if (!flag2) {
//                    System.out.println(speciesNetwork.getSpeciesId());
//                    System.out.println(currentReaction.getRId() + "\t\t" + currentReaction.getEquation());
//                    System.out.println("不能被其他反应消耗产物：" + s);
                    message.add("不能被其他反应消耗产物：" + s);
//                    messageList.add(new Message(s, currentReaction.getRId(), "不能被其他反应消耗产物"));
                }
                if (!(flag1 || flag2)) {
//                    System.out.println("产物判断双无:" + s);
//                    message.add("产物判断双无:" + s);
                    messageList.add(new Message(s, currentReaction.getRId(), "产物判断双无"));
                    System.out.println("--------------");
                }
            }//end for (String s : productsId)
            for (String pid : substratesId) {
                subsOfReaction.merge(pid, 1, Integer::sum);
                //如果路径中有两步都有共同产物生成，势必会影响路径表达效率(簇代谢物除外)
            }
        }//end for (int i = 0; i < reactionsOfPath.size(); i++)
        write1(completePath.toString(), subsOfReaction);
        write1(completePath.toString(), productOfPreReaction1);
        if (messageList.size() > 0)
            write2(completePath.toString(), speciesNetwork.getSpeciesId(), messageList);
        return messageList;
    }

    /**
     * 对于一个化合物是否多次作为底物或产物的判断
     * 怎么感觉这一块应该放在PathService中判断，好像就是路径成环的判断
     * 在这可能要运行很多次，先这样吧
     */
    public void write1(String path, Map<String, Integer> map) throws IOException {
        for (String s : map.keySet()) {
            if (map.get(s) > 1) {
                String content = path + "\t" + s;
//                FileUtils.writeToTxt("result.txt", "a", content);
            }
        }
    }

    public void write2(String path, String speciesId, List<Message> list) throws IOException {
        String content = "";
        content = content + path + "\n";
        content = content + speciesId + "\t\t";
        content = content + list.toString() + "\n";
        FileUtils.writeToTxt("result1.txt", "a", content);
    }

    /*
    min-max标准化
     */
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
            if (Double.isNaN(newValue))
                newMap.put(key, 0.0);
            else
                newMap.put(key, Double.valueOf(df.format(newValue)));
        }
        return newMap;
    }

    /**
     * 由path对象返回包含路径所有底物和所有产物列表的列表（长度为2）（不去重）
     *
     * @param path Path对象
     * @return 底物和所有产物列表的列表<底物列表, 产物列表>
     */
    public List<List<String>> get1(Path path) {
        List<String> substrateList = new ArrayList<>();//底物
        List<String> productList = new ArrayList<>();//产物
        ArrayList<Reaction> reactionsOfPath = path.getReactionsOfPath();
        ArrayList<Compound> compoundsOfPath = path.getCompoundsOfPath();
        for (int i = 0; i < reactionsOfPath.size(); i++) {
            Reaction reaction = reactionsOfPath.get(i);
            Compound compound = compoundsOfPath.get(i);
            if (reaction.getSubstrateId().contains(compound.getCId())) {
                substrateList.addAll(reaction.getSubstrateId());
                productList.addAll(reaction.getProductId());
            } else {
                substrateList.addAll(reaction.getProductId());
                productList.addAll(reaction.getSubstrateId());
            }
        }

        Set<String> s1 = new HashSet<>(substrateList);
        Set<String> s2 = new HashSet<>(productList);
        List<List<String>> ll = new ArrayList<>();
        ll.add(new ArrayList<>(s1));
        ll.add(new ArrayList<>(s2));
        return ll;
    }

    /**
     * 由path对象返回包含路径所有底物和所有产物列表的列表（长度为2）（去重）
     *
     * @param path Path对象
     * @return 底物和所有产物列表的列表<底物列表, 产物列表>
     */
    public List<List<String>> get2(Path path) {
        List<List<String>> lists = get1(path);
        Set<String> s1 = new TreeSet<>(lists.get(0));
        lists.get(0).clear();
        lists.get(0).addAll(s1);
        Set<String> s2 = new TreeSet<>(lists.get(1));
        lists.get(1).clear();
        lists.get(1).addAll(s2);
        return lists;

    }

    public List<Species> getAllSpecies() {
        List<Species> allSpecies = speciesMapper.getAllSpecies();
        return allSpecies;
    }

    @Override
    public Species getSpeciesById(String id) {
        return speciesMapper.getSpeciesById(id);
    }

    public void judgePath(String path, Map<String, Double> map) {
        PathSpecies pathSpecies = pathMapper.getPathSpeciesByPath(path);

        if(pathSpecies!=null&&13254==pathSpecies.getId()){
            String[] split = pathSpecies.getPathSpecies().split(";");
            double a = map.get(split[0]);
            map.put(split[0],map.get(split[1]));
            map.put(split[1],a);
            return;
        }
        if (pathSpecies != null && !pathSpecies.getPathSpecies().contains(";")) {
            map.put(pathSpecies.getPathSpecies(), (double) pathSpecies.getId() / 10000);
        }

    }

    /*
        根据底物和产物的情况计算分数
         */
    public Map<String, Double> test1(Path completePath) throws IOException {
//        String path = "C00082->R00737->C00081->R01616->C00223->R01613->C06561->R02446->C00509";
//        String path = "C00024->R01978->C00356->R02082->C00418->R02245->C01107->R03245->C01143->R01121->C00129->R02003->C00448->R07630->C16028->R10026->C20309";
//        String path = "C00129->R02061->C11356->R07916->C05421->R09716->C05432";
//        String path ="C00116->R00847->C00093->R08657->C00111->R01016->C00546->R02527->C00937->R03080->C02912";
//        String path = "C00082->R00737->C00811->R01616->C00223->R01613->C06561->R02446->C00509->C01477";
//        String path = "C00082->R00737->C00811->R01616->C00223->R01614->C03582";
//        String path = " C00074->R00199->C00022->C00024->R00351->C00158->R01325->C00417->R01900->C00311->R00709->C00026->R09784->C06547";
//        String path = "C00074->R00341->C00036->R00351->C00158->R01325->C00417->R01900->C00311->R00709->C00026->R09784->C06547";
//        String path = "C00022->R00209->C00024->R01359->C00332->R01358->C00164->R05735->C00207->R01550->C01845";
        String path = "C00024->R00351->C00158->R01324->C00311->R00267->C00026->R09784->C06547";
//        Path completePath = pathService.createCompletePath(path);
//        SpeciesNetworkService2 speciesNetworkService = new SpeciesNetworkService2();
        List<String> speciesId = speciesMapper.getAllSpeciesId();
        //底物和产物
        Map<String, List<Message>> map = new HashMap<>();
        for (String s : speciesId) {
            SpeciesNetwork1 network = getArrayFromTxt(s);
            List<Message> messageList = substrateAndProduct1(completePath, network);
            map.put(s, messageList);
        }
        Map<String, Double> map1 = new HashMap<>();
        for (String key : map.keySet()) {
            map1.put(key, (double) (map.get(key).size()));
        }
        //根据map中值设置权重分数
        Map<String, Double> scoreOfMap = dataStandard(map1);
//        System.out.println("---------test1()----------");
        return scoreOfMap;
    }

    /*
    计算路径在单个物种内dead end metabolite情况
     */
    public List<Message> getDeadEndMetaboliteInOneSpecies(String path, String speciesId) throws IOException {
        Path completePath = pathService.createCompletePath(path);
        //底物和产物
        Map<String, List<Message>> map = new HashMap<>();
        SpeciesNetwork1 network = getArrayFromTxt(speciesId);
        List<Message> messageList = substrateAndProduct1(completePath, network);
        //message信息替换，变成在网页展示的信息
        for (Message message : messageList) {
            if (message.getContent().equals("底物判断双无")) {
                message.setContent(message.getCompoundId() + " may not be available in the host.");
            } else if (message.getContent().equals("产物判断双无")) {
                message.setContent(message.getCompoundId() + " may not be consumed in the host.");
            }
        }
//        System.out.println("---------getDeadEndMetaboliteInOneSpecies()----------");
        return messageList;
    }
}
