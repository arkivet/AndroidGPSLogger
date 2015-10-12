package com.example.gpslogger;

/**
 * Created by Alex on 2015-10-07.
 */
public class Queue
{
    private class Node{
        public GPSData data;
        public Node next;

        public Node(){
            this.data = null;
            this.next = null;
        }

        public Node(GPSData data){
            this.data = data;
            this.next = null;
        }
    }
    Node first;
    Node current;
    Node walker;
    int size;

    public Queue(){
        first = null;
        current = null;
        walker = null;
        size = 0;
    }

    public void clearQueue(){
        size = 0;
        walker = first;
        while(first != null){
            walker = first;
            first = first.next;
            walker = null;
        }
        first = null;
        current = null;
    }

    public boolean isEmpty(){
        boolean empty = false;

        if(size == 0){
            empty = true;
        }

        return empty;
    }

    public void enQueue(GPSData data){
        if(size == 0){
            first = new Node(data);
            size++;
        }
        else if(size == 1){
            first.next = new Node(data);
            size++;
        }
        else {
            walker = first;
            while(walker.next != null){
                walker = walker.next;
            }
            walker.next = new Node(data);
            size++;
        }
    }

    public GPSData deQueue(){
        GPSData firstPos;
        Node temp;

        if(size == 0){
            firstPos = null;
        }
        else {
            size--;
            firstPos = this.first.data;
            temp = first.next;
            first = null;
            first = temp;
        }
        return firstPos;
    }

    public int size(){
        return this.size;
    }
}
