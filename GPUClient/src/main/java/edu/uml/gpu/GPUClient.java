package edu.uml.gpu;

/**
 * Created by pli on 8/4/14.
 */
import org.apache.spark.api.java.JavaRDD;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class GPUClient {
    public void GPUClient(){
        System.out.println("*** RMI Client Created! ***");
    }

    public void runRmiClientGetMessage(String addition) throws Exception {
        RmiServerIntf obj = (RmiServerIntf) Naming.lookup("//localhost/RmiServer");
        System.out.println(obj.getMessage(addition));
    }

    public void runRmiClientPrint(LinkedList a) throws Exception {
        RmiServerIntf lpr = (RmiServerIntf) Naming.lookup("//localhost/RmiServer");
        if (lpr.print(a) == true) System.out.println("Server got the LinkedList!");
        else System.out.println("Something is wrong!");
    }

    public List<Integer> runRmiClientSortArray(JavaRDD<Integer> array) throws Exception {
        RmiServerIntf newArray = (RmiServerIntf) Naming.lookup("//localhost/RmiServer");
        return newArray.sortArray(array);
    }

    public List runRmiClientSendRDD(List rdd) throws Exception {
        RmiServerIntf newRdd = (RmiServerIntf) Naming.lookup("//localhost/RmiServer");
        return newRdd.sendRDD(rdd);
    }

    public LinkedList runRmiClientSentLinkedList(LinkedList list) throws Exception {
        RmiServerIntf llp = (RmiServerIntf) Naming.lookup("//localhost/RmiServer");
        return llp.sentLinkedList(list);
    }
}


