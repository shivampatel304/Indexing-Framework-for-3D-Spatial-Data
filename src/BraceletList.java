import java.util.Scanner;

public class BraceletList {
    private Node head;

    public BraceletList() {
        head = null;
    }

    public static void main(String[] args) {
        BraceletList braceletList = new BraceletList();
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        String[] temp;
        if (n > 0) {
            temp = scanner.nextLine().trim().split(" ");
            for (int i = 0; i < 2 * n - 1; i = i + 2) {
                braceletList.addAtEnd(Integer.parseInt(temp[i]), Integer.parseInt(temp[i + 1]));
            }
        }
        braceletList.showContent();

    }

    public void addAtEnd(int x, int y) {
        Node tempNode = new Node(y, null, null);
        Node newNode = new Node(x, tempNode, null);

        if (head == null) {
            head = newNode;
            newNode.secondary = head;
        } else {
            Node temp = head;
            while (temp.secondary != head) {
                temp = temp.secondary;
            }
            newNode.secondary = head;
            temp.secondary = newNode;
        }
    }

    public void addToStart(int x, int y) {
        Node tempNode = new Node(y, null, null);
        Node newNode = new Node(x, tempNode, null);

        if (head == null) {
            head = newNode;
            newNode.secondary = head;
        } else {
            newNode.secondary = head;
            Node temp = head;
            while (temp.secondary != head) {
                temp = temp.secondary;
            }
            temp.secondary = newNode;
            head = newNode;
        }
    }

    public void deleteOuter(int y) {
        if (head == null) {
            return;
        }

        // Case when head needs to be deleted
        while (head.primary.value == y) {
            if (head.secondary == head) {
                // Only one element in the list
                head = null;
                return;
            } else {
                // Update the head and the tail's secondary pointer
                head = head.secondary;
                Node temp = head;
                while (temp.secondary.secondary != head) {
                    temp = temp.secondary;
                }
                temp.secondary = head;
            }
        }

        Node temp = head;
        while (temp.secondary != head) {
            if (temp.secondary.primary.value == y) {
                temp.secondary = temp.secondary.secondary;
            } else {
                temp = temp.secondary;
            }
        }

        showContent();
    }

    public BraceletList createHigherBlocksList() {
        BraceletList higherBlocksList = new BraceletList();
        if (head == null) {
            return higherBlocksList;
        }

        Node temp = head;
        int prevSum = -1;
        do {
            int currentSum = temp.value + temp.primary.value;
            if (currentSum > prevSum) {
                higherBlocksList.addAtEnd(temp.value, temp.primary.value);
                prevSum = currentSum;
            }
            temp = temp.secondary;
        } while (temp != head);

        return higherBlocksList;
    }

    public void accumulate3(int x) {
        if (head == null || head.secondary == null || head.secondary.secondary == null) {
            return;
        }

        Node temp = head;
        Node prev = null;
        while (temp != head || prev == null) {
            if (temp.value == x && temp.secondary.secondary.secondary != head) {
                int sumValue = temp.value + temp.secondary.value + temp.secondary.secondary.value;
                int sumPrimary = temp.primary.value + temp.secondary.primary.value + temp.secondary.secondary.primary.value;
                Node tempNode = new Node(sumPrimary, null, null);

                if (prev == null) {
                    head = new Node(sumValue, tempNode, temp.secondary.secondary.secondary);
                    prev = head;
                } else {
                    prev.secondary = new Node(sumValue, tempNode, temp.secondary.secondary.secondary);
                    prev = prev.secondary;
                }

                temp = temp.secondary.secondary.secondary;
            } else {
                if (prev == null) {
                    prev = temp;
                } else {
                    prev.secondary = new Node(temp.value, temp.primary, temp.secondary);
                    prev = prev.secondary;
                }
                temp = temp.secondary;
            }
        }

        showContent();
    }


    public void insertBeforeLastOccurrence(int x, int y1, int y2) {
        if (head == null) {
            return;
        }

        Node temp = head;
        Node prevOfLastOccurrence = null;
        Node prev = null;

        do {
            if (temp.value == x) {
                prevOfLastOccurrence = prev;
            }
            prev = temp;
            temp = temp.secondary;
        } while (temp != head);

        // If there's no occurrence of x, do nothing
        if (prevOfLastOccurrence == null) {
            return;
        }

        // If the last occurrence is the head, add the new node before the head
        if (prevOfLastOccurrence == prev) {
            addToStart(y1, y2);
        } else {
            Node tempNode = new Node(y2, null, null);
            prevOfLastOccurrence.secondary = new Node(y1, tempNode, prevOfLastOccurrence.secondary);
        }
        showContent();
    }


    public void showContent() {
        if (head == null) {
            System.out.println();
            return;
        }

        Node temp = head;
        String st = "";

        do {
            st += temp.value + " " + temp.primary.value + " ";
            temp = temp.secondary;
        } while (temp != head);

        System.out.println(st.trim());
    }

    // Implement other functions as needed

    static class Node {
        int value;
        Node primary;
        Node secondary;

        public Node() {
            value = 0;
            primary = null;
            secondary = null;
        }

        public Node(int i, Node primary, Node secondary) {
            value = i;
            this.primary = primary;
            this.secondary = secondary;
        }
    }
}
