package ast;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cms.util.maybe.Maybe;

public abstract class AbstractNode implements Node
{
    private Node parent;

    public Node getParent()
    {
        return parent;
    }

    @Override
    public int size()
    {
        int size = 1;
        if (this.getChildren() == null) return size;

        for(Node node : this.getChildren())
        {
            if (node != null) size += node.size();
        }
        return size;
    }

    @Override
    public Node nodeAt(int index)
    {
        if(index < 0 || index >= this.size()) throw new IndexOutOfBoundsException();
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(this);
        int count = 0;
        while (queue.peek() != null && count < index)
        {
            Node head = queue.poll();
            List<Node> children = head.getChildren();
            if (children != null)
            {
                queue.addAll(children);
            }
            count++;
        }
        count++;
        if (count < index)
        {
            throw new IndexOutOfBoundsException();
        }
        return queue.peek();
    }

    @Override
    public abstract StringBuilder prettyPrint(StringBuilder sb);

    @Override
    public abstract Node clone();

    @Override
    public abstract List<Node> getChildren();

    /**
     * You can remove this method if you don't like it.
     *
     * Sets the parent of this {@code Node}.
     *
     * @param p the node to set as this {@code Node}'s parent.
     */
    public void setParent(Node p)
    {
        parent = p;
    }

    /**
     * @return the String representation of the tree rooted at this {@code
     * Node}.
     */
    public String toString()
    {
        return this.prettyPrint(new StringBuilder()).toString();
    }

}
