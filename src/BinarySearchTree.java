import java.util.LinkedList;
import java.util.Stack;


/**
 * Binary Search Tree implementation with a Node inner class for representing
 * the nodes of the tree. We will turn this Binary Search Tree into a self-balancing
 * tree as part of project 1 by modifying its insert functionality.
 * In week 0 of project 1, we will start this process by implementing tree rotations.
 */
public class BinarySearchTree<T extends Comparable<T>> implements SortedCollectionInterface<T> {

    /**
     * This class represents a node holding a single value within a binary tree.
     */
    protected static class Node<T> {
        public T data;

        // up stores a reference to the node's parent
        public Node<T> up;
        // The down array stores references to the node's children:
        // - down[0] is the left child reference of the node,
        // - down[1] is the right child reference of the node.
        // The @SupressWarning("unchecked") annotation is use to supress an unchecked
        // cast warning. Java only allows us to instantiate arrays without generic
        // type parameters, so we use this cast here to avoid future casts of the
        // node type's data field.
        @SuppressWarnings("unchecked")
        public Node<T>[] down = (Node<T>[])new Node[2];
        public Node(T data) { this.data = data; }
        
        /**
         * @return true when this node has a parent and is the right child of
         * that parent, otherwise return false
         */
        public boolean isRightChild() {
            return this.up != null && this.up.down[1] == this;
        }

    }

    protected Node<T> root; // reference to root node of tree, null when empty
    protected int size = 0; // the number of values in the tree

    /**
     * Inserts a new data value into the tree.
     * This tree will not hold null references, nor duplicate data values.
     * @param data to be added into this binary search tree
     * @return true if the value was inserted, false if is was in the tree already
     * @throws NullPointerException when the provided data argument is null
     */
    public boolean insert(T data) throws NullPointerException {
        if (data == null)
            throw new NullPointerException("Cannot insert data value null into the tree.");
        return this.insertHelper(new Node<>(data));
    }

    /**
     * Performs a naive insertion into a binary search tree: adding the new node
     * in a leaf position within the tree. After this insertion, no attempt is made
     * to restructure or balance the tree.
     * @param node the new node to be inserted
     * @return true if the value was inserted, false if is was in the tree already
     * @throws NullPointerException when the provided node is null
     */
    protected boolean insertHelper(Node<T> newNode) throws NullPointerException {
        if(newNode == null) throw new NullPointerException("new node cannot be null");

        if (this.root == null) {
            // add first node to an empty tree
            root = newNode;
            size++;
            return true;
        } else {
            // insert into subtree
            Node<T> current = this.root;
            while (true) {
                int compare = newNode.data.compareTo(current.data);
                if (compare == 0) {
                    return false;
                } else if (compare < 0) {
                    // insert in left subtree
                    if (current.down[0] == null) {
                        // empty space to insert into
                        current.down[0] = newNode;
                        newNode.up = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.down[0];
                    }
                } else {
                    // insert in right subtree
                    if (current.down[1] == null) {
                        // empty space to insert into
                        current.down[1] = newNode;
                        newNode.up = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.down[1]; 
                    }
                }
            }
        }
    }

    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a
     * right child of the provided parent, this method will perform a left rotation.
     * When the provided nodes are not related in one of these ways, this method
     * will throw an IllegalArgumentException.
     * @param child is the node being rotated from child to parent position
     *      (between these two node arguments)
     * @param parent is the node being rotated from parent to child position
     *      (between these two node arguments)
     * @throws IllegalArgumentException when the provided child and parent
     *      node references are not initially (pre-rotation) related that way
     */
    protected void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {
      // Case where an IllegalArgumentException is thrown
      if(child.up != null && !(child.up.equals(parent))) {
        throw new IllegalArgumentException("The child and the parent are unrelated!");
      }
      
      // Case where a left rotation would occur
      if(child.isRightChild()) {
        if(parent.up != null) {
          if(parent.isRightChild()) {
            parent.up.down[1] = child; 
          }
          else {
            parent.up.down[0] = child;
          }
        }
        Node<T> childToSave = child.down[0];
        child.down[0] = parent;
        parent.down[1] = childToSave;
      }
      // Case where a right rotation would occur
      else {
        if(parent.up != null) {
          if(parent.isRightChild()) {
            parent.up.down[1] = child; 
          }
          else {
            parent.up.down[0] = child;
          }
        }
        Node<T> childToSave = child.down[1];
        child.down[1] = parent;
        parent.down[0] = childToSave;
      }
      
      if(parent.equals(root)) {
        root = child;
        child.up = null;
      }
      else {
        child.up = parent.up;
      }
      parent.up = child;
    }

    /**
     * Get the size of the tree (its number of nodes).
     * @return the number of nodes in the tree
     */
    public int size() {
        return size;
    }

    /**
     * Method to check if the tree is empty (does not contain any node).
     * @return true of this.size() returns 0, false if this.size() != 0
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Checks whether the tree contains the value *data*.
     * @param data a comparable for the data value to check for
     * @return true if *data* is in the tree, false if it is not in the tree
     */
    public boolean contains(Comparable<T> data) {
        // null references will not be stored within this tree
        if (data == null) {
            throw new NullPointerException("This tree cannot store null references.");
        } else {
            Node<T> nodeWithData = this.findNode(data);
            // return false if the node is null, true otherwise
            return (nodeWithData != null);
        }
    }

    /**
     * Removes all keys from the tree.
     */
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Helper method that will return the node in the tree that contains a specific
     * key. Returns null if there is no node that contains the key.
     * @param data the data value for which we want to find the node that contains it
     * @return the node that contains the data value or null if there is no such node
     */
    protected Node<T> findNode(Comparable<T> data) {
        Node<T> current = this.root;
        while (current != null) {
            int compare = data.compareTo(current.data);
            if (compare == 0) {
                // we found our value
                return current;
            } else if (compare < 0) {
                if (current.down[0] == null) {
                    // we have hit a null node and did not find our node
                    return null;
                }
                // keep looking in the left subtree
                current = current.down[0];
            } else {
                if (current.down[1] == null) {
                    // we have hit a null node and did not find our node
                    return null;
                }
                // keep looking in the right subtree
                current = current.down[1];
            }
        }
        return null;
    }

    /**
     * This method performs an inorder traversal of the tree. The string 
     * representations of each data value within this tree are assembled into a
     * comma separated string within brackets (similar to many implementations 
     * of java.util.Collection, like java.util.ArrayList, LinkedList, etc).
     * @return string containing the ordered values of this tree (in-order traversal)
     */
    public String toInOrderString() {
        // generate a string of all values of the tree in (ordered) in-order
        // traversal sequence
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        int nodesVisited = 0;
        if (this.root != null) {
            Stack<Node<T>> nodeStack = new Stack<>();
            Node<T> current = this.root;
            while (!nodeStack.isEmpty() || current != null) {
                if (current == null) {
                    Node<T> popped = nodeStack.pop();
                    if (++nodesVisited > this.size()) {
                        throw new RuntimeException("visited more nodes during traversal than there are keys in the tree; make sure there is no loop in the tree structure");
                    }
                    sb.append(popped.data.toString());
                    if(!nodeStack.isEmpty() || popped.down[1] != null) sb.append(", ");
                    current = popped.down[1];
                } else {
                    nodeStack.add(current);
                    current = current.down[0];
                }
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    /**
     * This method performs a level order traversal of the tree. The string
     * representations of each data value
     * within this tree are assembled into a comma separated string within
     * brackets (similar to many implementations of java.util.Collection).
     * This method will be helpful as a helper for the debugging and testing
     * of your rotation implementation.
     * @return string containing the values of this tree in level order
     */
    public String toLevelOrderString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        int nodesVisited = 0;
        if (this.root != null) {
            LinkedList<Node<T>> q = new LinkedList<>();
            q.add(this.root);
            while(!q.isEmpty()) {
                Node<T> next = q.removeFirst();
                if (++nodesVisited > this.size()) {
                    throw new RuntimeException("visited more nodes during traversal than there are keys in the tree; make sure there is no loop in the tree structure");
                }
                if(next.down[0] != null) q.add(next.down[0]);
                if(next.down[1] != null) q.add(next.down[1]);
                sb.append(next.data.toString());
                if(!q.isEmpty()) sb.append(", ");
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    public String toString() {
        return "level order: " + this.toLevelOrderString() +
                "\nin order: " + this.toInOrderString();
    }

    // Implement at least 3 tests using the methods below. You can
    // use your notes from lecture for ideas of rotation examples to test with.
    // Make sure to include rotations at the root of a tree in your test cases.
    // Give each of the methods a meaningful header comment that describes what is being
    // tested and make sure your tests have inline comments that help with reading your test code.
    // If you'd like to add additional tests, then name those methods similar to the ones given below.
    // Eg: public static boolean test4() {}
    // Do not change the method name or return type of the existing tests.
    // You can run your tests through the static main method of this class.

    /**
     * Tester method where the root node is rotated around in the tree. This tester method ends up performing a right rotation.
     *
     * Starting BST
     *          42
     *       /      \
     *     27        61
     *   /    \    /    \
     * 19     33  54    76
     * 
     * When rotating node 42 and 27, the following tree will be the result:
     *
     * Ending BST
     *          27
     *       /      \
     *     19        42
     *              /  \
     *             33  61
     *                /  \
     *               54  76
    */
    public static boolean test1() {
        // Creating the starting BST
        BinarySearchTree<Integer> bst = new BinarySearchTree<Integer>();
        bst.insert(42);
        bst.insert(27);
        bst.insert(61);
        bst.insert(19);
        bst.insert(33);
        bst.insert(54);
        bst.insert(76);

        bst.rotate(bst.root.down[0], bst.root);

        String expected = "level order: [ 27, 19, 42, 33, 61, 54, 76 ]\nin order: [ 19, 27, 33, 42, 54, 61, 76 ]";
        String actual = bst.toString();
        
        return expected.equals(actual);
    }
    
    /**
     * Tester method where the rotated node is not the root. This method should end up performing a left rotation.
     *
     * Starting BST:
     *              30
     *          /        \
     *         22        78 
     *       /    \    /    \
     *      12    27  67    88
     *
     * After performing a left rotation on 78 and 88, the following BST will result:
     *
     * Ending BST:
     *             30
     *         /        \
     *        22        88
     *      /    \     /
     *     12    27   78
     *               /
     *              67
     */
    public static boolean test2() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<Integer>();
        bst.insert(30);
        bst.insert(22);
        bst.insert(78);
        bst.insert(12);
        bst.insert(27);
        bst.insert(67);
        bst.insert(88);

        bst.rotate(bst.root.down[1].down[1], bst.root.down[1]);

        String expected = "level order: [ 30, 22, 88, 12, 27, 78, 67 ]\nin order: [ 12, 22, 27, 30, 67, 78, 88 ]";
        String actual = bst.toString();
        return expected.equals(actual);
    }

    /**
     * Tester method where the two nodes being rotated are not related. An IllegalArgumentException should be thrown and the BST should remain the same.
     *
     * BST Used:
     *        4
     *      /   \
     *     2     6
     *         /   \
     *        5     8
     */
    public static boolean test3() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<Integer>();
        bst.insert(4);
        bst.insert(2);
        bst.insert(6);
        bst.insert(5);
        bst.insert(8);

        try{
                bst.rotate(bst.root.down[1].down[1], bst.root);
        }
        catch(IllegalArgumentException e){
                // If an IllegalArgumentException is caught, make sure the BST remains unchanged. If it is, then return true.
                String expected = "level order: [ 4, 2, 6, 5, 8 ]\nin order: [ 2, 4, 5, 6, 8 ]";
                String actual = bst.toString();
                return expected.equals(actual);
        }
        // If an exception other than an IllegalArgumentException is thrown, the rotate method does not work as intended.
        catch(Exception e){
                return false;
        }
        // If no exception is caught, the rotate method does not work as intended.
        return false;
    }
    
    /**
     * Main method to run tests. If you'd like to add additional test methods, add a line for each
     * of them.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Test 1 passed: " + test1());
        System.out.println("Test 2 passed: " + test2());
        System.out.println("Test 3 passed: " + test3());
    }

}
