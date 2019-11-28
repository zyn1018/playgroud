package com.yinan.play.demo;

import com.yinan.play.demo.meta.algorithm.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 链表相关算法题
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkedListTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void linkedListReverse() {
        Node head = new Node(0);
        Node firstNode = new Node(1);
        Node secondNode = new Node(2);
        Node thirdNode = new Node(3);
        head.setNext(firstNode);
        firstNode.setNext(secondNode);
        secondNode.setNext(thirdNode);

        Node tmp = head;
        while (tmp != null) {
            System.out.print(tmp.getData());
            tmp = tmp.getNext();
            if (tmp != null) {
                System.out.print("->");
            }
        }

        System.out.println("\n-------------------------");

        Node reverseTmp = recursionReverseLinkedList(head);
        while (reverseTmp != null) {
            System.out.print(reverseTmp.getData());
            reverseTmp = reverseTmp.getNext();
            if (reverseTmp != null) {
                System.out.print("->");
            }
        }
    }

    /**
     * 单链表反转(遍历反转)
     *
     * @param head 链表头
     * @retur 反转后的链表头
     */
    private Node reverseLinkedList(Node head) {
        if (head == null) {
            return null;
        }
        //如果列表只有一个元素
        if (head.getNext() == null) {
            return head;
        }
        Node pre = head;
        Node currentNode = head.getNext();
        while (currentNode != null) {
            Node temp = currentNode.getNext();
            currentNode.setNext(pre);
            pre = currentNode;
            currentNode = temp;
        }
        head.setNext(null);
        return pre;
    }

    /**
     * 单链表反转（递归）
     *
     * @param head 链表头
     * @return 反转后的链表头
     */
    private Node recursionReverseLinkedList(Node head) {
        if (head == null || head.getNext() == null) {
            return head;
        }
        Node currentNode = recursionReverseLinkedList(head.getNext());
        head.getNext().setNext(head);
        head.setNext(null);

        return currentNode;
    }
}
