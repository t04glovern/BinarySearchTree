//Modified version of the BST.
//*replace all null references with a reference to a sentinel node.
//*a method that returns the number of full nodes in the BST.
//*an algorithm to remove all the nodes in a BST that are within a specified range, inclusively.
//*an implementation of an algorithm to return all the elements at a specified level in a BST
package Minor.P2.DS;

import java.util.LinkedList;
import java.util.Vector;



// BST<> provides a generic implementation of a binary search tree
//
// BST<> implementation constraints:
//   - The tree uses package access for root, and for the node type.
//   - The node type uses package access for its data members.
//   - The tree never stores two objects for which compareTo() returns 0.
//   - All tree traversals are performed recursively.
//   - Optionally, the BST<> employs a pool of deleted nodes.
//     If so, when an insertion is performed, a node from the pool is used 
//     unless the pool is empty, and when a deletion is performed, the
//     (cleaned) deleted node is added to the pool, unless the pool is
//     full.  The maximum size of the pool is set via the constructor.
//
// User data type (T) constraints:
//   - T implements compareTo() and equals() appropriately
//   - compareTo() and equals() are consistent; that is, compareTo()
//     returns 0 in exactly the same situations equals() returns true
//
/**
 * This class provides a generic implementation of a binary search tree.
 * @author Hang
 *
 * @param <T> The generic data type
 */
public class RecBST<T extends Comparable<? super T>> {
	
	/**
	 * The inner Node class, use to contain element and pointer to other nodes.
	 * @author Hang
	 *
	 */
    class BinaryNode {

        T          element;  // the data in the node
        BinaryNode parent;   // pointer to the parent
        BinaryNode left;     // pointer to the left child
        BinaryNode right;    // pointer to the right child
       
        /**
         * Initialize a childless binary node.
         * Pre:   elem is not null
         * Post:  (in the new node)
                  element == elem
                  left == right == null
         * @param elem the element to be added.
         * @param p the parent node.
         */
        public BinaryNode( T elem) {
     	   element = elem;
     	   parent = null;
     	   left = null;
     	   right = null;
        }
        
       /**
        * Initialize a childless binary node with a parent node.
        * Pre:   elem is not null
        * Post:  (in the new node)
                 element == elem
                 parent = p
                 left == right == null
        * @param elem the element to be added.
        * @param p the parent node.
        */
       public BinaryNode( T elem, BinaryNode p ) {
    	   element = elem;
    	   parent = p;
    	   left = null;
    	   right = null;
       }

       /**
        * Initialize a binary node with children.
        * Pre:   elem is not null
        * Post:  (in the new node)
        *      	 element == elem
        *        left == lt, right == rt 
        * @param elem the element to be added.
        * @param p the parent node.
        * @param lt the left child node.
        * @param rt the right child node.
        */
       public BinaryNode( T elem, BinaryNode p, BinaryNode lt, BinaryNode rt ) {
    	   element = elem;
    	   parent = p;
    	   left = lt;
    	   right = rt;
       }
    }

    BinaryNode root;        // pointer to root node, if present
    BinaryNode pool;        // pointer to first node in the pool
    int        pSize;       // size limit for node pool
    LinkedList<BinaryNode> listPool;    // List of nodes the pool has
	BinaryNode sentinel;	// Pointer to a null node.
	Boolean isTransposed;	// If the tree has been transposed
          
    /**
     * Initialize empty BST with no node pool.
     * Pre:   none
     * Post:  (in the new tree)
     * 		  root == null, pool == null, pSize = 0
     */
    public RecBST( ) {
    	root = null;
    	pool = null;
    	pSize = 0;
    	listPool = new LinkedList<BinaryNode>();
    	sentinel = new BinaryNode(null);	//HW1 Modified
		isTransposed = false;				//HW1 Modified
    }


    /**
     * Initialize empty BST with a node pool of up to pSize nodes.
     * Pre:   none
     * Post:  (in the new tree)
              root == null, pool = null, pSize == Sz 
     * @param Sz the size of the pool
     */
    public RecBST( int Sz ) {
    	root = null;
    	pool = null;
    	pSize = Sz;
    	listPool = new LinkedList<BinaryNode>();
    	sentinel = new BinaryNode(null);	//HW1 Modified
		isTransposed = false;				//HW1 Modified
    }

    
    /**
     * Determine if the BST is empty or not.
     * Pre:   none
     * Post:  the binary tree is unchanged
     * @return true if the BST is empty, false other wise.
     */
    public boolean isEmpty( ) {
    	if ( root == null){
    		return true;
    	}
    	return false;
    }
    
    /**
     * Return pointer to matching data element, or null if no matching
     * element exists in the BST.  "Matching" should be tested using the
     * data object's compareTo() method.
     * Pre:   x is null or points to a valid object of type T
     * Post:  the binary tree is unchanged
     * @param x the element to search for.
     * @return whether if the element is in the BST.
     */
    public T find( T x ) {
    	
    	sentinel.element = x;				// Set sentinel's element to the search element.
    	BinaryNode temp = recFind(x, root); // makes an temporary node to contain the node found.
    	
    	// If the tree has not been transposed, use original code to check
    	if (!isTransposed) {
    		if ( temp == null){
        		return null;
        	}
        	return temp.element;
    	} 
    	// If the tree has been transposed, check if the node found is sentinel
    	else {
    		if (temp == sentinel) {
    			return null;
    		} 
    		return temp.element;
    	}
    	
    	
//		Original code
//    	BinaryNode temp = recFind(x, root); // makes an temporary node to contain the node found.
//    	if ( temp == null){
//    		return null;
//    	}
//    	return temp.element;
    }
    
    /**
     * Recursive method to find an item in a subtree.
     * Pre:   x is null or points to a valid object of type T
     * 		  node is the root where we start to look from
     * Post:  the binary tree is unchanged
     * @param x the element to search for.
     * @param node the node that roots the subtree.
     * @return node containing the matched item, and null if not found.
     */
    private BinaryNode recFind(T x, BinaryNode node)
    {
        if (node == null || x == null)
        {
            return null;    // Not found
        }
        else if (x.compareTo(node.element) < 0)
        {
            // Search in the left subtree
            return recFind(x, node.left);
        }
        else if (x.compareTo(node.element) > 0)
        {
            // Search in the right subtree
            return recFind(x, node.right);
        }
        else
        {
            return node;    // Found
        }
    }


    /**
     * Insert element x into BST, unless it is already stored.  Return true
     * if insertion is performed and false otherwise.
     * Pre:   x is null or points to a valid object of type T
     * Post:  the binary tree contains x
     * @param x the element to be inserted.
     * @return whether if the element has been successfully inserted.
     */
    public boolean insert( T x ) {
    	if (x == null || find(x) != null){
    		return false;
    	} 
    	root = recInsert(x, root);
    	return true;
    }
    /**
     * Recursive method to insert a element into the BST.
     * Pre:   x is null or points to a valid object of type T
     * Post:  the binary tree contains x
     * @param x the element to be inserted.
     * @param node the node that roots the subtree.
     * @return the new node
     */
    private BinaryNode recInsert(T x, BinaryNode node)
    {
    	// only applies to first node added
        if (node == null){
            node = createNode(x, null);
        }
        //look left
        else if (x.compareTo(node.element) < 0){
        	if ( node.left == null){
        		node.left = createNode(x, node);
        	}
        	else{
        		recInsert(x, node.left);
        	}
        }
        //look right
        else if (x.compareTo(node.element) > 0){
        	if ( node.right == null){
        		node.right = createNode(x, node);
        	}
        	else{
        		recInsert(x, node.right);
        	}
        }
        return node;
    }
    
    /**
     * Helper method to make a new node from scratch or node form pool
     * Pre:   x is null or points to a valid object of type T
     * Post:  BST is unchanged
     * @param x the element of the new node
     * @param node the parent node
     * @return the newly created node
     */
    private BinaryNode createNode(T x, BinaryNode node){
    	if(listPool.size() == 0 ){
    		return new BinaryNode(x, node);
    	}
    	else{
    		//recycle a node from the pool
    		BinaryNode temp = listPool.removeFirst();
    		temp.element = x;
    		temp.parent = node;
    		temp.left = null;
    		temp.right = null;
    		
    		pool = listPool.peek();
    		return temp;
    	}
    }

    /**
     * Delete element matching x from the BST, if present.  Return true if
     * matching element is removed from the tree and false otherwise.
     * Pre:   x is null or points to a valid object of type T
     * Post:  the binary tree does not contain x
     * @param x the element to be removed.
     * @return whether if the element has been successfully removed
     */
    public boolean remove( T x ) {
    	if ( x == null || find(x) == null){
    		return false;
    	}
    	root = recRemove(x, root, null);
    	return true;
    	
    }
    /**
     * Recursive method to remove a specified element from a subtree.
     * Pre:   x is null or points to a valid object of type T
     * Post:  the binary tree does not contain x
     * @param x the element to remove.
     * @param node the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode recRemove(T x, BinaryNode node, BinaryNode pNode)
    {
        // if value should be to the left of the root
        if (x.compareTo(node.element) < 0){
            node.left = recRemove(x, node.left, node);
            return node;
        }
        // if value should be to the right of the root
        else if (x.compareTo(node.element) > 0){
            node.right = recRemove(x, node.right, node);
            return node;
        }
        // If value is on the current node
        else if (x.compareTo(node.element) == 0){
        	// tempNode is the node that's going to be replacing the removed node.
            BinaryNode tempNode;
            
            // If there are two children
            if (node.left != null && node.right != null){
            	//If the right child does not have a left child
                if ( node.right.left == null){
                	node.right.left = node.left;
                	node.left.parent = node.right;
                	node.right.parent = node.parent;
                	tempNode = node.right;
                	
                	// add the node to the pool if possible
                	if ( listPool.size() < pSize){
                		listPool.add(setNull(node));
                		pool = listPool.peek();
                	}
                	return tempNode;
                }
                // find smallest element on right subtree
                tempNode = findMin(node.right);
                
                // If the replacing node has a right child, it has to be reconnected.
                if( tempNode.right != null){
                	tempNode.parent.left = tempNode.right;
                	tempNode.right.parent = tempNode.parent;
                }
                
                // remove pointers from old location
                if ( tempNode.right == null){
                    tempNode.parent.left = null;
                }
                
                //Copy over pointers from the node thats being remove to the tempNode.
                tempNode.left = node.left;
                tempNode.right = node.right;
                tempNode.parent = node.parent;
                //Change the pointers of the child node of the removed node to the tempNode. 
                node.left.parent = tempNode;
                node.right.parent = tempNode;
                
                
                // add the node to the pool if possible
            	if ( listPool.size() < pSize){
            		listPool.add(setNull(node));
            		pool = listPool.peek();
            	}
                return tempNode;
                
            }
            // If there is only one child on the left.
            else if (node.left != null){
            	node.left.parent = node.parent;
            	tempNode = node.left;
                
            	// add the node to the pool if possible
            	if ( listPool.size() < pSize){
            		listPool.add(setNull(node));
            		pool = listPool.peek();
            	}
            	return tempNode;
            }
            // If there is only one child on the right.
            else if ( node.right != null){
            	node.right.parent = node.parent;
            	tempNode = node.right;
                
            	// add the node to the pool if possible
            	if ( listPool.size() < pSize){
            		listPool.add(setNull(node));
            		pool = listPool.peek();
            	}
            	return tempNode;
            }
            // If there is no child.
            else{
            	// add the node to the pool if possible
            	if ( listPool.size() < pSize){
            		listPool.add(setNull(node));
            		pool = listPool.peek();
            	}
            	return null;
            }
        }
        return null;
    }
    /**
     * Recursive method to find the smallest item in a subtree.
     * Pre:   node is a valid pointer
     * Post:  the binary tree is unchanged
     * @param node the node that roots the tree.
     * @return node containing the smallest element.
     */
    private BinaryNode findMin(BinaryNode node)
    {
        if (node == null){
            return null;
        }
        else if (node.left == null){
            return node;
        }
        else{
            return findMin(node.left);
        }
    }
    
    /**
     * A helper method to set everything of the given node to null.
     * Pre:   node is a valid pointer
     * @param node the node to null
     * @return a node with everything inside null.
     */
    private BinaryNode setNull(BinaryNode node){
    	node.element = null;
    	node.parent = null;
    	node.left = null;
    	node.right = null;
    	return node;
    }


    /**
     * Delete the subtree, if any, whose root contains an element matching x.
     * Pre:   x is null or points to a valid object of type T
     * Post:  if the tree contained x, then it the subtree rooted at that
               node has been removed
     * @param x the element to prune from
     * @return whether if the prune is successful.
     */
    public boolean prune( T x) {
    	BinaryNode temp = recFind(x, root);
    	if ( temp == null){
    		return false;
    	}
    	recPrune(temp);
    	return true;
    } 
    
    /**
     * Recusive method to remove all node from the given starter node.
     * Pre:   node is a valid pointer
     * Post:  the subtree rooted at the node has been removed
     * @param node the node to begin the removal
     */
    private void recPrune(BinaryNode node){
    	if (node.left != null){
    		recPrune(node.left);
    	}
    	if(node.right != null){
    		recPrune(node.right);
    	}
    	if( node.left == null && node.right == null){
    		remove(node.element);
    	}
    }


    /**
     * Return the tree to an empty state.
     * Pre:   none
     * Post:  the binary tree contains no elements
     */
    public void clear( ) {
    	recPrune(root);
    }


	/**
	 * Return true iff other is a BST that has the same physical structure
     * and stores equal data values in corresponding nodes.  "Equal" should
     * be tested using the data object's equals() method.
     * Pre:   other is null or points to a valid BST<> object, instantiated
     *        on the same data type as the tree on which equals() is invoked
     * Post:  both binary trees are unchanged
     * @param other the object that's going to be compared to
     * @return whether if other is equal to the current BST
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object other) {
		if( other == null){
			return false;
		}
		
    	if ( !other.getClass().equals(this.getClass())){
    		return false;
    	}

    	LinkedList<T> list1 = new LinkedList<T>(); //makes a LinkedList to contain element of tree
    	LinkedList<T> list2 = new LinkedList<T>(); //makes a LinkedList to contain element of other
    	
    	recTraverse(root, list1);
    	recTraverse((BinaryNode)this.getClass().cast(other).root, list2);
    	
    	if ( list1.equals(list2)){
    		return true;
    	}
    	return false;
    	
    }
    
	/**
	 * Recursive method to add one element at a time to a LinkedList using pre-order traverse.
	 * Pre:   list is a valid container
     * Post:  the binary tree is unchanged, list is changed
	 * @param node the node that roots the subtree.
	 * @param list the LinkedList thats keeping track of the 
	 */
    private void recTraverse(BinaryNode node, LinkedList<T> list){
    	if ( node == null){
    		return;
    	}
    	list.add(node.element);
    	recTraverse(node.left, list);
    	recTraverse(node.right, list);
    } 
    
    public LinkedList<T> string(){
    	LinkedList<T> list = new LinkedList<T>(); //makes a LinkedList to contain element of other
    	recTraverse(root, list);
    	return list;
    }
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    /**
	 * Transposes a null terminated BST into a sentinel terminated BST.
	 * Pre: BST is null terminated. isTransposed == false.
	 * Post: BST is sentinel terminated. isTransposed = true.
	 */
	public void sentinelTree() {
		recSentinelTree(root);
		isTransposed = true;
	}
	
	/**
	 * Recursive method to change a null terminated BST into a sentinalBST function.
	 * @param node the node that roots the subtree.
	 */
	private void recSentinelTree(BinaryNode node) {
		// uses in-order traversal and replace any null pointer with an sentinel node
		
		//check left leaf node
		if (node.left == null) {
			node.left = sentinel;
		} 
		else {
			recSentinelTree(node.left);
		}
		
		//check right leaf node
		if (node.right == null) {
			node.right = sentinel;
		} 
		else {
			recSentinelTree(node.right);
		}
	}
	
	/**
	 * Counts the number of full nodes in the tree.
	 * @return Number of full nodes.
	 */
	public int countFull() {
		return recCountFull(root);
	}

	/**
	 * Recursive method to count the number of full nodes in the tree
	 * @param node the node that roots the subtree.
	 * @return the number of full nodes in the subtree.
	 */
	private int recCountFull(BinaryNode node) {
		// if the node has a left and right child, count + 1
		if (node.left != null && node.right != null) {
			return 1 + recCountFull(node.left) + recCountFull(node.right);
		} 
		// not a full node
		return 0;
	}
	
	/**
	 * Removes all the nodes in the BST that are within a specified range,
	 * inclusively.
	 * 
	 * Pre: lower and upper are valid objects of type T, such that 
	 * lower <= upper, according to type T's compareTo().
	 * 
	 * Post: The resulting BST contains no element, e, such that 
	 * lower <= e <= upper.
	 * 
	 * @param lower the lower bound element.
	 * @param upper the upper bound element.
	 */
	public void rangePrune(T lower, T upper) {
		recRangePrune(lower, upper, root);
	}

	/**
	 * Recursive method to removes all the nodes in the BST that are within a specified range,
	 * inclusively.

	 * @param lower the lower bound element.
	 * @param upper the upper bound element.
	 * @param node the node that roots the subtree.
	 */
	private void recRangePrune(T lower, T upper, BinaryNode node) {

		// checks if the node is within range
		if (node.element.compareTo(lower) < 0 || node.element.compareTo(upper) > 0) { // not in range
			// traverse the left subtree if it's not null
			if (node.left != null) {
				recRangePrune(lower, upper, node.left);
			}
			// traverse the right subtree if it's not null
			if (node.right != null) {
				recRangePrune(lower, upper, node.right);
			}
		} 
		
		else { // the node is within range.

			// traverse the subtree before removing the node. This way, node at bottom gets remove 1st.
			if (node.left != null) {
				recRangePrune(lower, upper, node.left);
			}
			if (node.right != null) {
				recRangePrune(lower, upper, node.right);
			}
			
			remove(node.element);
		}
	}
	
	/**
	 * Returns all the elements at a specified level in a BST.
	 * 
	 * Pre: L >= 0.
	 * @param L the specified level in a BST
	 * @return A vector object containing all the elements X found in the BST at LV L
	 */
	public Vector<T> elementsAtLevel(int L) {
		// creates a new vector object
		Vector<T> elemsL = new Vector<T>();
		recElementsAtlevel(root, elemsL, L);
		return elemsL;
	}

	/**
	 * Recursive method that performs a post-order traversal and when the specified level is reached,
	 * add the data into the vector
	 * 
	 * @param node the node that roots the subtree.
	 * @param elems A vector to store potential elements.
	 * @param L how many level to traverse
	 */
	private void recElementsAtlevel(BinaryNode node, Vector<T> elemsL, int L) {
		// go down a level if specified level not reached
		if (L != 0) {
			if (node.left != null) {
				recElementsAtlevel(node.left, elemsL, L - 1);
			}
			if (node.right != null) {
				recElementsAtlevel(node.right, elemsL, L - 1);
			}
		} 
		else {
			// Add the current node's element to the vector.
			elemsL.addElement(node.element);
		}
	}
}
//On my honor:
//
//- I have not discussed the Java language code in my program with
//anyone other than my instructor or the teaching assistants
//assigned to this course.
//
//- I have not used Java language code obtained from another student,
//or any other unauthorized source, either modified or unmodified.
//
//- If any Java language code or documentation used in my program
//was obtained from another source, such as a text book or course
//notes, that has been clearly noted with a proper citation in
//the comments of my program.
//
//- I have not designed this program in such a way as to defeat or
//interfere with the normal operation of the Automated Grader.
//
//Pledge: On my honor, I have neither given nor received unauthorized
//aid on this assignment.
//
//<Hang Lin>
