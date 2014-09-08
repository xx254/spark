package edu.uml.gpu;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by pli on 8/28/14.
 */
public class RunGPUExample {
    public static void main (String args[]){
        GPUClient client = new GPUClient();
        /*
        Config conf = ConfigFactory.load();
        String hostName = conf.getString("spark_env.host_name");
        String port = conf.getString("spark_env.port");
        String numCore = conf.getString("spark_env.num_core");
        String appName = conf.getString("spark_env.host_name");
        String sparkHome = conf.getString("spark_env.spark_home");
        String appAssembly = conf.getString("app_env.app_assembly");

        String sparkHostString =
        if (hostName.equals("local")) {return hostName+ "[" + numCore + "]";}
        else "spark://" + hostname + ":" + port;
        */
        SparkConf conf = new SparkConf();
        conf.setAppName("Simple Application")
        .setMaster("local")
        //.setSparkHome("//root/lib")
        .set("spark.executor.memory", "1g")
        //.set("spark.jars", "out/artifacts/GPUClient_jar/GPUClient.jar")
        .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");

        String logFile = "/home/pli/IdeaProjects/GPUClient/README.md"; // Should be some file on your system
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> logData = sc.textFile(logFile).cache();
        System.out.println("logData = "+logData.toString());
        List logDataList = logData.collect();

        /*
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        JavaRDD<Integer> distData = sc.parallelize(data);
        List<Integer> AAA = distData.collect();
        for(Integer ele: AAA)
            System.out.println("Element in AAA:"+ele);
        */

        /*
        long numAs = logData.filter(new Function<String, Boolean>() {
            public Boolean call(String s) { return s.contains("a"); }
        }).count();

        long numBs = logData.filter(new Function<String, Boolean>() {
            public Boolean call(String s) { return s.contains("b"); }
        }).count();

        System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);
        */


        try {
            client.runRmiClientGetMessage("This is RMI Client!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinkedList a = new LinkedList();
        a.insert(new Date());
        a.insert("Today is ");
        try {
            client.runRmiClientPrint(a);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int[] anArray;
        anArray =  new int[10];
        int i;
        for(i=0; i<10; i++){
            anArray[i] = i;
        }
        try {
            //java.util.List<Integer> listB = client.runRmiClientSortArray(distData);
            //System.out.println("logData = "+logData.toString());

            logDataList = client.runRmiClientSendRDD(logDataList);
            for (int j = 0; j < logDataList.size(); j++) {
                System.out.println(logDataList.get(i));
            }
            //logData.collect();
            //for(Integer ele: listB)
            //    System.out.println(ele);

            //a = client.runRmiClientSentLinkedList(a);
            //a.print();
        } catch (Exception e) {
            System.out.println("Catched the RMI exception.");
            e.printStackTrace();
        }

    }
}
