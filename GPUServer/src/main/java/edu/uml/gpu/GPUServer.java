package edu.uml.gpu;


/**
 * Created by pli on 8/4/14.
 */
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GPUServer
        extends UnicastRemoteObject
        implements RmiServerIntf {
    public static final String MESSAGE = "Hello World";

    public GPUServer() throws RemoteException {
        super(0);    // required to avoid the 'rmic' step, see below
    }

    public String getMessage(String addition) throws RemoteException{
        int i,j;
        int root = 1;
        int value = 0;
        for(i=0; i<10000; i++)
            for(j=0; j<10000; j++)
            {
                value += root^i + root^j;
                value -= root^i + root^j;
                //System.out.println("value="+value);
            }
        System.out.println("value="+value);
        return MESSAGE+": "+addition;
    }

    public boolean print(LinkedList list) throws RemoteException{
        list.print();
        return true;
    }

    public List<Integer> sortArray(JavaRDD<Integer> array) throws RemoteException{
        java.util.List<Integer> listA =  array.collect();
        return listA;
    }

    public List sendRDD(List rddList) throws RemoteException {
        System.out.println("Enter sendRDD...");
        rddList.add("Add Extra Element to List!");
        //rdd.collect();
        //System.out.println("RDD = "+rdd.toString());
        return rddList;
    }

    public LinkedList sentLinkedList(LinkedList list) throws RemoteException {
        System.out.println("Enter sendLinkedList...");
        list.insert("This line is added on RMI server!");
        System.out.println("Linked List = " + list.toString());
        return list;
    }

    public static void main(String args[]) throws Exception {

        System.out.println("RMI server started");

        try { //special exception handler for registry creation
            LocateRegistry.createRegistry(1099);
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
            System.out.println("java RMI registry already exists.");
        }

        //Instantiate RmiServer

        GPUServer obj = new GPUServer();

        // Bind this object instance to the name "RmiServer"
        Naming.rebind("//localhost/RmiServer", obj);
        System.out.println("PeerServer bound in registry");
    }
}
