package edu.uml.gpu;

import java.io.Serializable;

/**
 * Created by pli on 8/5/14.
 */
public class ListNode implements Serializable{
    private Object value;
    private ListNode next;

    public ListNode (Object value, ListNode next) {
        this.value = value;
        this.next = next;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
