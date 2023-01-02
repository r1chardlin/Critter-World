package ast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class SearchMutation extends AbstractMutation
{
    private Program root;

    public void setRoot(Program r)
    {
        root = r;
    }

    public List<Node> findSubtrees(Node n)
    {
        List<Node> subtrees = new ArrayList<Node>();
        LinkedList<Node> queue = new LinkedList<Node>();
        queue.add(root);
        while (queue.peek() != null)
        {
            Node head = queue.poll();
            if ((n instanceof Expr && head instanceof Expr) || (n instanceof Condition && head instanceof Condition)
                    || head.getClass() == n.getClass()) // && head != n
            {
                subtrees.add(head);
            }
            if (head.getChildren() != null)
            {
                queue.addAll(head.getChildren());
            }
        }
        return subtrees;
    }

    public int pickElement(List<Node> children)
    {
        int size = children.size();
        return (int) (Math.random() * size);
    }
}
