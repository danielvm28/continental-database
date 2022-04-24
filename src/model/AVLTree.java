package model;

import exception.DuplicateValueException;

import java.util.Comparator;

public class AVLTree<T extends Comparable<T>> {
    private Node<T> root;
    private Comparator<T> c;

    public AVLTree() {
        root = null;
    }

    public AVLTree(Comparator<T> c) {
        this.c = c;
        root = null;
    }

    public Node<T> getRoot() {
        return root;
    }

    public void setRoot(Node<T> root) {
        this.root = root;
    }

    public void updateHeightOfNode(Node<T> n) {
        n.setHeight(1 + Math.max(heightOfNode(n.getLeft()), heightOfNode(n.getRight())));
    }

    public int heightOfNode(Node<T> n) {
        return n == null ? -1 : n.getHeight();
    }

    public int balanceOfNode(Node<T> n) {
        return n == null ? 0 : heightOfNode(n.getRight()) - heightOfNode(n.getLeft());
    }

    public Node<T> rotateRight(Node<T> n) {
        Node<T> x = n.getLeft();
        Node<T> y = x.getRight();

        x.setRight(n);
        n.setLeft(y);

        updateHeightOfNode(n);
        updateHeightOfNode(x);

        return x; // Returns the new head of the subtree
    }

    public Node<T> rotateLeft(Node<T> n) {
        Node<T> x = n.getRight();
        Node<T> y = x.getLeft();

        x.setLeft(n);
        n.setRight(y);

        updateHeightOfNode(n);
        updateHeightOfNode(x);

        return x; // Returns the new head of the subtree
    }

    // Insert methods
    //-------------------------------------------------------------------------------------------------------

    /**
     * Inserts from the root, this is the standard insertion method that uses natural object comparison
     * @param value the value to insert
     * @throws DuplicateValueException throw if a duplicate value is encountered
     */
    public void insert(T value) throws DuplicateValueException {
        root = insert(root, value);
    }

    /**
     * Inserts from the root, this is the custom method to insert a node through a comparator method
     * @param value the value to insert
     * @param c the comparator method to compare manually
     * @throws DuplicateValueException throw if a duplicate value is encountered
     */
    public void insert(T value, Comparator<T> c) throws DuplicateValueException {
        root = insert(root, value, c);
    }

    /**
     * Inserts from the node n, mainly used for recursion
     * @param n actual node
     * @param value value to insert
     * @return a balanced node
     * @throws DuplicateValueException throw if a duplicate value is encountered
     */
    private Node<T> insert(Node<T> n, T value) throws DuplicateValueException{
        // Search the place to insert the new node,
        if (n == null) {
            n = new Node<>(value);
            return n;
        } else if(n.getValue().compareTo(value) > 0) {
            n.setLeft(insert(n.getLeft(), value));
        } else if (n.getValue().compareTo(value) < 0) {
            n.setRight(insert(n.getRight(), value));
        } else {
            throw new DuplicateValueException();
        }

        return balance(n);
    }

    /**
     * Inserts from the node n, mainly used for recursion. This method inserts in a custom way
     * @param n actual node
     * @param value value to insert
     * @param c the comparator method to compare manually
     * @return a balanced node
     * @throws DuplicateValueException throw if a duplicate value is encountered
     */
    private Node<T> insert(Node<T> n, T value, Comparator<T> c) throws DuplicateValueException{
        // Search the place to insert the new node,
        if (n == null) {
            n = new Node<>(value);
            return n;
        } else if(c.compare(n.getValue(), value) > 0) {
            n.setLeft(insert(n.getLeft(), value, c));
        } else if (c.compare(n.getValue(), value) < 0) {
            n.setRight(insert(n.getRight(), value, c));
        } else {
            throw new DuplicateValueException();
        }

        return balance(n);
    }

    //-------------------------------------------------------------------------------------------------------

    public Node<T> balance(Node<T> n) {
        updateHeightOfNode(n);

        int balanceFactor = balanceOfNode(n);

        // Checks if the weight is to the right or left

        if (balanceFactor > 1) { // Weight to the right

            if (balanceOfNode(n.getRight()) >= 0) { // Case A & B - Rotate Left
                n = rotateLeft(n);
            } else { // Case C - Rotate Right and Left
                n.setRight(rotateRight(n.getRight()));
                n = rotateLeft(n);
            }

        } else if (balanceFactor < -1) { // Weight to the left

            if (balanceOfNode(n.getLeft()) <= 0) { // Case D & E - Rotate Right
                n = rotateRight(n);
            } else { // Case F - Rotate Left and Right
                n.setLeft(rotateLeft(n.getLeft()));
                n = rotateRight(n);
            }
        }

        return n;
    }

    // Find methods
    //-------------------------------------------------------------------------------------------------------

    /**
     * Finds from the root, this is the standard method to find a node that uses natural object comparison
     * @param value the value that needs to be found
     * @return the coincident node
     */
    public Node<T> find(T value) {
        return find(root, value);
    }

    /**
     * Finds from the root, this is the custom method to find a node through a comparator method
     * @param value the value that needs to be found
     * @param c the function used to compare manually
     * @return the coincident node
     */
    public Node<T> find(T value, Comparator<T> c) {
        return find(root, value, c);
    }

    /**
     * Finds from the node n, used for recursion
     * @param n the actual node
     * @param value the value to find
     * @return the coincident node
     */
    private Node<T> find(Node<T> n, T value) {
        // Base cases
        if (n == null) {
            return null;
        }
        if (n.getValue().compareTo(value) == 0) {
            return n;
        }

        // Recursion
        if (n.getValue().compareTo(value) > 0) { // Search left
            return find(n.getLeft(), value);
        } else { // Search right
            return find(n.getRight(), value);
        }
    }

    /**
     * Finds from the node n, used for recursion. This method finds in a custom way
     * @param n the actual node
     * @param value the value to find
     * @param c the function used to compare manually
     * @return the coincident node
     */
    private Node<T> find(Node<T> n, T value, Comparator<T> c) {
        // Base cases
        if (n == null) {
            return null;
        }
        if (c.compare(n.getValue(), value) == 0) {
            return n;
        }

        // Recursion
        if (c.compare(n.getValue(), value) > 0) { // Search left
            return find(n.getLeft(), value, c);
        } else { // Search right
            return find(n.getRight(), value, c);
        }
    }

    // Delete methods
    //-------------------------------------------------------------------------------------------------------

    /**
     * Deletes from the root, this is the standard method to delete a node that uses natural object comparison
     * @param value the value to delete
     */
    public void delete(T value) {
        root = delete(root, value);
    }

    /**
     * Deletes from the root, this is the custom method to delete a node through a comparator method
     * @param value the value to delete
     * @param c the function used to compare manually
     */
    public void delete(T value, Comparator<T> c) {
        root = delete(root, value);
    }

    /**
     * Deletes from the node n, used for recursion
     * @param n the actual node
     * @param value the Person value to delete
     * @return a balanced node after deletion
     */
    private Node<T> delete(Node<T> n, T value) {
        if (n == null) {
            return null;
        }

        if (n.getValue().compareTo(value) > 0) { // Search left
            n.setLeft(delete(n.getLeft(), value));
        } else if (n.getValue().compareTo(value) < 0) { // Search right
            n.setRight(delete(n.getRight(), value));
        } else { // If the value is found

            // In case the node has none, or just one child
            if (n.getLeft() == null || n.getRight() == null) {
                n = (n.getLeft() == null) ? n.getRight() : n.getLeft();
            } else {
                // In case it has both nodes as children
                // Get the node's successor
                Node<T> mostLeftChild = mostLeftChild(n.getRight());

                // Copy the successor's value
                n.setValue(mostLeftChild.getValue());

                // Delete the successor recursively
                n.setRight(delete(n.getRight(), n.getValue()));
            }

        }

        if (n != null) {
            n = balance(n);
        }

        return n;
    }

    /**
     * Deletes from the node n, used for recursion. This method deletes in a custom way
     * @param n the actual node
     * @param value the Person value to delete
     * @param c the function used to compare manually
     * @return a balanced node after deletion
     */
    private Node<T> delete(Node<T> n, T value, Comparator<T> c) {
        if (n == null) {
            return null;
        }

        if (c.compare(n.getValue(), value) > 0) { // Search left
            n.setLeft(delete(n.getLeft(), value, c));
        } else if (c.compare(n.getValue(), value) < 0) { // Search right
            n.setRight(delete(n.getRight(), value, c));
        } else { // If the value is found

            // In case the node has none, or just one child
            if (n.getLeft() == null || n.getRight() == null) {
                n = (n.getLeft() == null) ? n.getRight() : n.getLeft();
            } else {
                // In case it has both nodes as children
                // Get the node's successor
                Node<T> mostLeftChild = mostLeftChild(n.getRight());

                // Copy the successor's value
                n.setValue(mostLeftChild.getValue());

                // Delete the successor recursively
                n.setRight(delete(n.getRight(), n.getValue(), c));
            }

        }

        if (n != null) {
            n = balance(n);
        }

        return n;
    }

    //-------------------------------------------------------------------------------------------------------

    // To get the most left child or successor
    public Node<T> mostLeftChild(Node<T> n) {
        while (n.getLeft() != null) {
            n = n.getLeft();
        }

        return n;
    }

    public final int COUNT = 10;

    private void print2DUtil(Node<T> root, int space)
    {
        // Base case
        if (root == null)
            return;

        // Increase distance between levels
        space += COUNT;

        // Process right child first
        print2DUtil(root.getRight(), space);

        // Print current node after space
        // count
        System.out.print("\n");
        for (int i = COUNT; i < space; i++)
            System.out.print(" ");
        System.out.print(root.getValue() + " " + balanceOfNode(root) + "\n");

        // Process left child
        print2DUtil(root.getLeft(), space);
    }

    // Wrapper over print2DUtil()
    public void print2D()
    {
        // Pass initial space count as 0
        print2DUtil(root, 0);
    }
}
