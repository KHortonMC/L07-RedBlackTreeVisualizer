package cs113.treeVisualizer;

// Red-Black Tree Implementation
public class RedBlackTree<E extends Comparable<E>> {
    enum Color {
        RED, BLACK
    }

    public class Node<E> {
        E element;
        Node<E> left;
        Node<E> right;
        Node<E> parent;
        Color color;

        public Node(E element) {
            this.element = element;
            this.color = Color.RED; // New nodes are always red
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        public boolean isLeftChild() {
            return parent != null && parent.left == this;
        }
    }

    private Node<E> root;
    private Node<E> NIL;

    public RedBlackTree() {
        NIL = new Node<>(null);
        NIL.color = Color.BLACK;
        root = NIL;
    }

    public boolean isEmpty() {
        return root == NIL;
    }

    public Node<E> getRoot() {
        return root;
    }

    public int getHeight() {
        return calculateHeight(root);
    }

    private int calculateHeight(Node<E> node) {
        if (node == NIL) {
            return 0;
        }
        return 1 + Math.max(calculateHeight(node.left), calculateHeight(node.right));
    }

    public void insert(E element) {
        Node<E> newNode = new Node<>(element);
        newNode.left = NIL;
        newNode.right = NIL;

        if (root == NIL) {
            root = newNode;
            fixInsert(root);
            return;
        }

        Node<E> current = root;
        Node<E> parent = null;

        // Find the position to insert
        while (current != NIL) {
            parent = current;
            int cmp = element.compareTo(current.element);

            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                // Duplicate value, do nothing
                return;
            }
        }

        // Set the parent and insert the new node
        newNode.parent = parent;

        if (element.compareTo(parent.element) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        // Fix the Red-Black Tree properties
        fixInsert(newNode);
    }

    private void fixInsert(Node<E> node) {
        // todo: implement the fixInsert method!
        // follow the instructions in readme.md
    }

    private void leftRotate(Node<E> x) {
        Node<E> y = x.right;
        x.right = y.left;

        if (y.left != NIL) {
            y.left.parent = x;
        }

        y.parent = x.parent;

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node<E> x) {
        Node<E> y = x.left;
        x.left = y.right;

        if (y.right != NIL) {
            y.right.parent = x;
        }

        y.parent = x.parent;

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }

        y.right = x;
        x.parent = y;
    }

    // Find a node with a given value
    private Node<E> findNode(E element) {
        Node<E> current = root;

        while (current != NIL) {
            int cmp = element.compareTo(current.element);

            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return null; // Node not found
    }

    // Delete a node with a given value
    public boolean delete(E element) {
        Node<E> nodeToDelete = findNode(element);

        if (nodeToDelete == null) {
            return false; // Node not found
        }

        deleteNode(nodeToDelete);
        return true;
    }

    private void deleteNode(Node<E> z) {
        Node<E> y = z;
        Node<E> x;
        Color yOriginalColor = y.color;

        if (z.left == NIL) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == NIL) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = findMinimum(z.right);
            yOriginalColor = y.color;
            x = y.right;

            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }

        if (yOriginalColor == Color.BLACK) {
            fixDelete(x);
        }
    }

    private void fixDelete(Node<E> x) {
        while (x != root && x.color == Color.BLACK) {
            if (x.isLeftChild()) {
                Node<E> w = x.parent.right;

                // Case 1: x's sibling w is red
                if (w.color == Color.RED) {
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }

                // Case 2: x's sibling w is black, and both of w's children are black
                if (w.left.color == Color.BLACK && w.right.color == Color.BLACK) {
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    // Case 3: x's sibling w is black, w's left child is red, w's right child is black
                    if (w.right.color == Color.BLACK) {
                        w.left.color = Color.BLACK;
                        w.color = Color.RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }

                    // Case 4: x's sibling w is black, and w's right child is red
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.right.color = Color.BLACK;
                    leftRotate(x.parent);
                    x = root; // This will exit the loop
                }
            } else {
                // Symmetric cases for right child
                Node<E> w = x.parent.left;

                // Case 1: x's sibling w is red
                if (w.color == Color.RED) {
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }

                // Case 2: x's sibling w is black, and both of w's children are black
                if (w.right.color == Color.BLACK && w.left.color == Color.BLACK) {
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    // Case 3: x's sibling w is black, w's right child is red, w's left child is black
                    if (w.left.color == Color.BLACK) {
                        w.right.color = Color.BLACK;
                        w.color = Color.RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }

                    // Case 4: x's sibling w is black, and w's left child is red
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.left.color = Color.BLACK;
                    rightRotate(x.parent);
                    x = root; // This will exit the loop
                }
            }
        }

        x.color = Color.BLACK;
    }

    private void transplant(Node<E> u, Node<E> v) {
        if (u.parent == null) {
            root = v;
        } else if (u.isLeftChild()) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }

        v.parent = u.parent;
    }

    private Node<E> findMinimum(Node<E> node) {
        while (node.left != NIL) {
            node = node.left;
        }
        return node;
    }
}
