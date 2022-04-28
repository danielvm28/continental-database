package model;

import exception.DuplicateValueException;

public interface AVLTreeI<T> {
    public void updateHeightOfNode(Node<T> n);

    public int heightOfNode(Node<T> n);

    public int balanceOfNode(Node<T> n);

    public Node<T> rotateRight(Node<T> n);

    public Node<T> rotateLeft(Node<T> n);

    public void insert(T value) throws DuplicateValueException;

    public Node<T> balance(Node<T> n);

    public Node<T> find(T value);

    public void delete(T value);

    public Node<T> mostLeftChild(Node<T> n);
}
