package edu.uml.gpu;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pli on 8/5/14.
 */
public class LinkedList implements Serializable {
    private ListNode head;
    private ListNode tail;
    public LinkedList() {
        head = null;
        tail = null;
    }

    public ListNode getHead() {
        return head;
    }

    public void setHead(ListNode head) {
        this.head = head;
    }

    public ListNode getTail() {
        return tail;
    }

    public void setTail(ListNode tail) {
        this.tail = tail;
    }

    public void insert (Object obj) {
        if (this.isEmpty())
        {
            ListNode newNode = new ListNode(obj, null);
            this.setHead(newNode);
            this.setTail(newNode);
        }
        else
        {
            ListNode newNode = new ListNode(obj, this.getHead());
            this.setHead(newNode);
        }
    }

    public void append (LinkedList list) {

    }

    public boolean isEmpty() {
        if (this.getHead() == null && this.getTail() == null) return true;
        else return false;
    }

    public void print() {
        if (this.isEmpty()) {
            System.out.println("The linked list is empty!");
        }
        else {
            ListNode tmpHead = this.getHead();
            do{
                if (tmpHead.equals(this.getHead()) )
                {
                    this.formatPrint(tmpHead.getValue());
                }
                tmpHead = tmpHead.getNext();
                this.formatPrint(tmpHead.getValue());
            }
            while( tmpHead.getNext() != null );
        }
    }

    public void formatPrint(Object input)
    {
        if (input instanceof Date){
            String value = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS").format(input);
            System.out.println(value);
        }else if (input instanceof String){
            String value = (String) input;
            System.out.println(value);
        }else {
            System.out.println("Object type not supported!");
        }
    }

}
