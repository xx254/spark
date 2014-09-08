package edu.uml.gpu;

/**
 * Created by pli on 8/27/14.
 */
import org.apache.spark.api.java.JavaRDD;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RmiServerIntf extends Remote {
    public String getMessage(String addition) throws RemoteException;
    public boolean print(LinkedList list) throws RemoteException;
    public List<Integer> sortArray(JavaRDD<Integer> array) throws RemoteException;
    public List sendRDD(List rdd) throws RemoteException;
    public LinkedList sentLinkedList(LinkedList list) throws RemoteException;
}
