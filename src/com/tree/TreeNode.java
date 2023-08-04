package com.tree;

import com.shape.Rectangle;

public class TreeNode {

    // The tree itself
    private Rectangle rectangle;

    // --- Children ---
    // If 1 child, then only child1 will be populated
    // If 2 children, then both child1 and child2 are populated
    public TreeNode child1;
    public TreeNode child2;

    public TreeNode getChild1() {
        return child1;
    }

    public void setChild1(TreeNode child1) {
        this.child1 = child1;
    }

    public TreeNode getChild2() {
        return child2;
    }

    public void setChild2(TreeNode child2) {
        this.child2 = child2;
    }

    public TreeNode(String text, String value, boolean literal) {
        if (literal) {
            rectangle = new Rectangle(text, value);
        } else {

        }
    }

}
