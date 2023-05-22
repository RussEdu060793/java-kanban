package manager;

import task.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private Node first;
    private Node last;
    private HashMap<String, Node> taskMap;

    public InMemoryHistoryManager() {
        taskMap = new HashMap<>();
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (last == null) {
            first = newNode;
            last = newNode;
        } else {
            newNode.prev = last;
            last.next = newNode;
            last = newNode;
        }
        taskMap.put(task.getId(), newNode);
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node current = last;
        var index = 0;
        while (current != null && index < 10) {
            tasks.add(current.task);
            current = current.prev;
            index += 1;
        }
        return tasks;
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            first = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            last = node.prev;
        }
        taskMap.remove(node.task.getId());
    }

    @Override
    public void add(Task task) {
        Node existingNode = taskMap.get(task.getId());
        if (existingNode != null) {
            removeNode(existingNode);
        }
        linkLast(task);
    }

    @Override
    public void remove(String id){
        if (taskMap.containsKey(id)) {
            removeNode(taskMap.get(id));
        }
    }

    class Node {
        private Task task;
        private Node prev;
        private Node next;

        public Node(Task task) {
            this.task = task;
        }
    }
}
