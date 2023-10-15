import Boruvka.Boruvka;
import Boruvka.BoruvkaFactory;
import Boruvka.BoruvkaFactoryImplementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListRankingApp {
    public static void main(String[] args) {
        ListRankingApp app = new ListRankingApp();

        // Sample usage
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;

        app.parallelListRanking(node1);

        // Output the rank of each node.
        ListNode current = node1;
        while (current != null) {
            System.out.println(current.rank);
            current = current.next;
        }
        int[] mwe = {0, 1, 2, 3, 4, 5, 6};  // Example minimum weight edges
        int numThreads = 4;  // Number of threads to run in parallel

        // Use the factory to create an instance of BoruvkaLLP
        BoruvkaFactory boruvkaFactory = new BoruvkaFactoryImplementation();
        Boruvka boruvkaLLP = boruvkaFactory.createBoruvkaInstance(mwe);

        boruvkaLLP.initializeG();  // Initialize G using the minimum weight edges
        boruvkaLLP.boruvkaLLP(numThreads);  // Run Boruvka's algorithm using LLP
    }
    public void parallelListRanking(ListNode head) {
        ParallelLLP<ListNode> pll = new ParallelLLP<>(4);  // Using 4 threads.

        List<ListNode> activeNodes = new ArrayList<>();
        ListNode current = head;
        while (current != null) {
            activeNodes.add(current);
            current = current.next;
        }

        while (!activeNodes.isEmpty()) {
            List<ListNode> nextActiveNodes = Collections.synchronizedList(new ArrayList<>());

            pll.compute(activeNodes, node -> {
                if (node.next != null) {
                    node.rank += 1 + node.next.rank;  // Adopt the next's rank
                    if (node.next.next != null) {
                        nextActiveNodes.add(node.next.next);  // Prepare for the next round
                    }
                    node.next = node.next.next;  // Jump two steps
                }
                return true;
            });

            activeNodes = new ArrayList<>(nextActiveNodes);  // Start next round with nodes ready for processing
        }
    }




}