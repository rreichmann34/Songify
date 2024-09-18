import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
//import BinarySearchTree.Node;
import org.junit.jupiter.api.Assertions;

public class IterableRedBlackTree<T extends Comparable<T>>
    extends RedBlackTree<T> implements IterableSortedCollection<T> {

    /**
     * The starting point for this IterableRedBlackTree. It is initialized to be less than any
     * value in any RBT, meaning that it's compare to method returns -1.
     */
    private Comparable<T> start = new Comparable<T>(){
      @Override
      public int compareTo(T o) {
        return -1;
      }
    };
  
    /**
     * Sets the start point for this IterableRedBlackTree. Sets the start variable above to an 
     * actual starting node. If it's passed a null start, then start is reset to be the lowest 
     * possible value in the tree.
     */
    public void setIterationStartPoint(Comparable<T> startPoint) {
      if(startPoint == null) {
        start = new Comparable<T>(){
          @Override
          public int compareTo(T o) {
            return -1;
          }
        };
      } else {
        start = startPoint;
      }
    }

    /**
     * Returns the iterator for this class
     */
    public Iterator<T> iterator() {
      return new RBTIterator<T>(root, start);
    }

    private static class RBTIterator<R> implements Iterator<R> {
      
      /**
       * The start node's data for this RBTIterator
       */
      private Comparable<R> start;
      
      /**
       * The stack of nodes for this iterator
       */
      private Stack<Node<R>> stack;
      
      /**
       * Constructor for an RBTIterator. Creates an iterator based off of a starting point in the 
       * tree and a root.
       * 
       * @param root the root of the tree
       * @param startPoint the node data that this iterator starts at
       */
      public RBTIterator(Node<R> root, Comparable<R> startPoint) {
        start = startPoint;
        stack = new Stack<Node<R>>();
        buildStackHelper(root);
      }

      /**
       * Builds the stack that primarily helps with the next() method, defined below.
       * 
       * @param node the node to build the stack from.
       */
      private void buildStackHelper(Node<R> node) {
        // Base case: When the parameter node is null. The method should do nothing and return.
        if(node == null) {
          return;
        }
        
        // Recursive case 1: When the parameter node is smaller than start value. This method will 
        // recurse down node's right subtree
        if(start.compareTo(node.data) > 0) {
          buildStackHelper(node.down[1]);
        }
        
        // Recursive case 2: When the parameter node is greater than or equal to the start value. 
        // This method will add this node to the stack and recurse down the node's left subtree.
        if(start.compareTo(node.data) <= 0) {
          buildStackHelper(node.down[1]);
          stack.push(node);
          buildStackHelper(node.down[0]);
        }
      }

      /**
       * If there are still nodes left in the iterator, this method will return true.
       * 
       * @return true if not empty and false if empty.
       */
      public boolean hasNext() {
        return !stack.isEmpty();
      }

      /**
       * Returns the value of the next node in the stack.
       */
      public R next() {
        if(!hasNext()) { // If no elements left in iterator, throw exception.
          throw new NoSuchElementException("There are no more values in the stack!");
        }
        return stack.pop().data;
      }
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
              /*if (compare == 0) {
                  return false;
              }*/ if (compare <= 0) {
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
     * Tests the iterator when traversing through a tree filled with string nodes
     */
    @Test
    public void testStringTreeIterator() {
      IterableRedBlackTree<String> tree = new IterableRedBlackTree<String>();
      tree.insert("c");
      tree.insert("b");
      tree.insert("d");
      tree.insert("a");
      
      Iterator<String> it = tree.iterator();
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals("a", it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals("b", it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals("c", it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals("d", it.next(), "The next value in the iterator is not what it should be!");
    }
    
    /**
     * Tests the iterator when traversing through a tree filled with integer nodes
     */
    @Test
    public void testIntTreeIterator() {
      IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<Integer>();
      tree.insert(3);
      tree.insert(2);
      tree.insert(4);
      tree.insert(1);
      
      Iterator<Integer> it = tree.iterator();
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals(1, it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals(2, it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals(3, it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals(4, it.next(), "The next value in the iterator is not what it should be!");
    }
    
    /**
     * Tests the iterator when traversing through a tree filled with string nodes where there is one duplicate node
     */
    @Test
    public void testDuplicateStringTreeIterator() {
      IterableRedBlackTree<String> tree = new IterableRedBlackTree<String>();
      tree.insert("c");
      tree.insert("d");
      tree.insert("d");
      tree.insert("b");
      
      Iterator<String> it = tree.iterator();
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals("b", it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals("c", it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals("d", it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals("d", it.next(), "The next value in the iterator is not what it should be!");
    }
    
    /**
     * Tests the iterator when traversing through a tree filled with integer nodes where there is one duplicate node
     */
    @Test
    public void testDuplicateIntTreeIterator() {
      IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<Integer>();
      tree.insert(3);
      tree.insert(2);
      tree.insert(4);
      tree.insert(4);
      
      
      Iterator<Integer> it = tree.iterator();
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals(2, it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals(3, it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals(4, it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals(4, it.next(), "The next value in the iterator is not what it should be!");
    }
    
    /**
     * Tests the iterator when traversing through a tree that has a different starting point than the root of the original tree. 
     * The tree is filled with string objects.
     */
    @Test
    public void testStartPointStringTreeIterator() {
      IterableRedBlackTree<String> tree = new IterableRedBlackTree<String>();
      tree.insert("d");
      tree.insert("e");
      tree.insert("b");
      tree.insert("a");
      tree.insert("c");
      
      
      Iterator<String> it = tree.iterator();
      setIterationStartPoint((Comparable<T>)tree.root.down[0].data);
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals("a", it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals("b", it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals("c", it.next(), "The next value in the iterator is not what it should be!");
    }
    
    /**
     * Tests the iterator when traversing through a tree that has a different starting point than the root of the original tree. 
     * The tree is filled with integer objects.
     */
    @Test
    public void testStartPointIntTreeIterator() {
      IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<Integer>();
      tree.insert(4);
      tree.insert(5);
      tree.insert(2);
      tree.insert(1);
      tree.insert(3);
      
      
      Iterator<Integer> it = tree.iterator();
      setIterationStartPoint((Comparable<T>)tree.root.down[0].data);
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals(1, it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals(2, it.next(), "The next value in the iterator is not what it should be!");
      
      Assertions.assertEquals(true, it.hasNext(), "The iterator does not have another value when it should!");
      Assertions.assertEquals(3, it.next(), "The next value in the iterator is not what it should be!"); 
    }
}
