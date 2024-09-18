import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T>{
  
  protected static class RBTNode<T> extends Node<T> {
    public boolean isBlack = false;
    public RBTNode(T data) { super(data); }
    public RBTNode<T> getUp() { return (RBTNode<T>)this.up; }
    public RBTNode<T> getDownLeft() { return (RBTNode<T>)this.down[0]; }
    public RBTNode<T> getDownRight() { return (RBTNode<T>)this.down[1]; }
  }
  
  /*
   * Helper method for the insert function. When a new red node is inserted, if it has a red parent,
   * then the tree will either need to be recolored or rotated or both. This method handles any of
   * the violations that occur
   */
  protected void enforceRBTreePropertiesAfterInsert(RBTNode<T> node) {
    // Case where there are no values in the RBT yet. There is nothing to fix in this case and the 
    // method should return.
    if(node.equals(root)) {
      return;
    }
    
    RBTNode<T> parent = node.getUp();
    RBTNode<T> aunt = null;
    if(parent.isRightChild()) {
      aunt = node.getUp().getUp().getDownLeft();
    }
    else {
      aunt = node.getUp().getUp().getDownRight();
    }
    RBTNode<T> grandparent = node.getUp().getUp();
    
    
    // Case 1 where the aunt and parent of node are red. The parent, aunt, and grandparent should
    // all swap colors. 
    if(aunt != null && !parent.isBlack && !aunt.isBlack) {
      grandparent.isBlack = false; // grandparent turns red
      // parent and aunt turn black
      parent.isBlack = true;
      aunt.isBlack = true;
    }
    
    // Case 2 and 3 happen when the parent is red and the aunt is black
    else if(!parent.isBlack) {
      // Case 2: The child and the aunt are opposite children. Ex/ The child is a left child and the 
      // aunt is a right child.
      // In this case, the grandparent and the parent should be rotated, and the two of them should
      // change color.
      if(parent.isRightChild() == node.isRightChild()) {
        rotate(parent, grandparent);
        grandparent.isBlack = false;
        parent.isBlack = true;
      }
      
      // Case 3: The child and the aunt are the same children. Ex/ The child and aunt are the left 
      // child of their respective parents
      else {
        rotate(node, parent);
        rotate(node, grandparent);
        node.isBlack = true;
        grandparent.isBlack = false;
      }
    }
    
    // There are no errors in the tree, so return
    else {
      return;
    }
    
    enforceRBTreePropertiesAfterInsert(grandparent);
  }
  
  /**
   * Inserts a new red node into the RedBlackTree. The helper method 
   * enforceRBTTreePropertiesAfterInsert handles any rbt violations that happen. The root is turned
   * to black after every insert to ensure that the root is always black. 
   * 
   * @throws NullPointerException if the inputed data is null
   */
  public boolean insert(T data) throws NullPointerException{
    try {
      RBTNode<T> node = new RBTNode<T>(data);
      node.isBlack = false; 
      insertHelper(node);
      enforceRBTreePropertiesAfterInsert(node);
      ((RBTNode<T>)root).isBlack = true;
    } catch(NullPointerException e) {
      return false;
    }
    return true;
  }
  
  /**
   * The following case tests an insertion into a red black tree when the child's aunt is red. The 
   * initial tree looks like the following:
   * 
   *                            grand(black)
   *                        /                  \
   *                 parent(red)             aunt(red)
   *              /
   *        child(red)
   *        
   * The grandparent will end up turning red and the parent and aunt will both turn black. Since
   * the grandparent also happens to be the root of this tree, it will be turned back to black. The 
   * final tree will look like the following:
   * 
   *                           grand(black)
   *                       /                  \
   *                 parent(black)          aunt(black)
   *               /
   *          child(red)
   */
  @Test
  public void testInsertRedAunt() {
    RedBlackTree<Integer> rbt = new RedBlackTree<Integer>();
    rbt.insert(3); // 3 will be the grandparent and will be black
    rbt.insert(2); // 2 will be the parent and will be red
    ((RBTNode<Integer>)rbt.root.down[0]).isBlack = true;
    rbt.insert(4); // 4 will be the aunt and will be red
    
    rbt.insert(1);
    Assertions.assertEquals("level order: [ 3, 2, 4, 1 ]\nin order: [ 1, 2, 3, 4 ]", rbt.toString(), "The red black tree is different than what is expected!"); // Test to make sure the rbt is in the right order
    Assertions.assertEquals(true, rbt.root.data == 3 && ((RBTNode<Integer>)rbt.root).isBlack, "The root node is not black!"); // Test to make sure that the root node is black
    Assertions.assertEquals(true, rbt.root.down[0].data == 2 && ((RBTNode<T>)root.down[0]).isBlack, "The parent node is not black!"); // Test to make sure the parent is black
    Assertions.assertEquals(true, rbt.root.down[1].data == 4 && ((RBTNode<T>)root.down[1]).isBlack, "The aunt node is not black!"); // Test to make sure the aunt is black
    Assertions.assertEquals(true, rbt.root.down[0].down[0].data == 1 && !((RBTNode<T>)root.down[0].down[0]).isBlack, "The child node is not red!"); // Test to make sure the child is still red
  }
  
  /**
   * The following case tests an insertion into a red black tree when the child's aunt is black.
   * This specific case looks at when the child is the left child and the aunt is the right child.
   * The intial tree looks like the following:
   * 
   *                             grand(black)
   *                        /                  \
   *                 parent(red)             aunt(black)
   *              /
   *        child(red)
   * 
   * The grandparent and the parent will end up rotating and swapping colors. The final tree should
   * look like the following:
   * 
   *                            parent(black)
   *                        /                  \
   *                   child(red)             grand(red)
   *                                                   \
   *                                                 aunt(black)
   */
  @Test
  public void testInsertBlackAuntLine() {
    RedBlackTree<Integer> rbt = new RedBlackTree<Integer>();
    // The tree will need to be constructed manually,since the aunt would remain as a red node when
    // inserted.
    rbt.root = new RBTNode<Integer>(3); // 3 will be the grandparent and will be black
    rbt.root.down[0] = new RBTNode<Integer>(2); // 2 will be the parent and will be red
    rbt.root.down[1] = new RBTNode<Integer>(4); // 4 will be the aunt and will be black
    rbt.size = 3;
    
    rbt.insert(1); // insert can be called for the child because it is automatically red when inserted
    Assertions.assertEquals("level order: [ 2, 1, 3, 4 ]\nin order: [ 1, 2, 3, 4 ]", rbt.toString(), "The red black tree is different than what is expected!"); // Test to make sure the rbt is in the right order
    Assertions.assertEquals(true, rbt.root.data == 2 && ((RBTNode<T>)root).isBlack, "The root node is not black or the root node does not have the correct data!"); // Test to make sure that the root node is black and is now the parent
    Assertions.assertEquals(true, rbt.root.down[0].data == 1 && !((RBTNode<T>)root.down[0]).isBlack, "The child node is not red or the node does not have the correct data!"); // Test to make sure the child moved to the right place when the rotation occured and that it is still red
    Assertions.assertEquals(true, rbt.root.down[1].data == 3 && !((RBTNode<T>)root.down[1]).isBlack, "The grandparent node is not red or the node does not have the correct data!"); // Test to make sure the aunt is black
    Assertions.assertEquals(true, rbt.root.down[1].down[1].data == 4 && ((RBTNode<T>)root.down[1].down[1]).isBlack, "The aunt node is not black or the node does not contain the correct data!"); // Test to make sure the child is still red
  }
  
  /**
   * The following case tests an insertion into a red black tree when the child's aunt and parent 
   * are both red. The initial tree looks like the following:
   * 
   *                            grand(black)
   *                        /                  \
   *                 parent(red)             aunt(red)
   *                             \
   *                           child(red)
   *                           
   * The only thing that should happen in this case is that the parent and the aunt will be 
   * recolored. The final tree will look like the following:
   * 
   *                            grand(black)
   *                        /                  \
   *                 parent(black)          aunt(black)
   *                             \
   *                           child(red)
   */
  @Test
  public void testInsertRedAuntRedParent() {
    RedBlackTree<Integer> rbt = new RedBlackTree<Integer>();
    rbt.insert(3); // 3 will be the grandparent and will be black
    rbt.insert(1); // 1 will be the parent and will be red
    rbt.insert(4); // 4 will be the aunt and will be red
    
    rbt.insert(2); // 2 will be the child and will be red
    Assertions.assertEquals("level order: [ 3, 1, 4, 2 ]\nin order: [ 1, 2, 3, 4 ]", rbt.toString(), "The red black tree is different than what is expected!"); // Test to make sure the rbt is in the right order
    Assertions.assertEquals(true, rbt.root.data == 3 && ((RBTNode<T>)root).isBlack, "The root node is not black or the root node does not have the correct data!"); // Test to make sure that the root node is black and is now the parent
    Assertions.assertEquals(true, rbt.root.down[0].data == 1 && ((RBTNode<T>)root.down[0]).isBlack, "The parent node is not black or the node does not have the correct data!"); // Test to make sure the child moved to the right place when the rotation occured and that it is still red
    Assertions.assertEquals(true, rbt.root.down[1].data == 4 && ((RBTNode<T>)root.down[1]).isBlack, "The aunt node is not black or the node does not have the correct data!"); // Test to make sure the aunt is black
    Assertions.assertEquals(true, rbt.root.down[0].down[1].data == 2 && !((RBTNode<T>)root.down[0].down[1]).isBlack, "The child node is not red or the node does not contain the correct data!"); // Test to make sure the child is still red
  }
}
