/**
 * @(#)Node.java, 2019/11/28.
 * <p>
 * Copyright 2019 NetEase, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.yinan.play.demo.meta.algorithm;

/**
 * @author Yinan Zhang (zhangyinan01@corp.netease.com)
 */
public class Node {
    private int data;

    private Node next;

    public Node(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
