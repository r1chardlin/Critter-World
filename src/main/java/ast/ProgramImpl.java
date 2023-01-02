package ast;

import cms.util.maybe.Maybe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/** A data structure representing a critter program. */
public class ProgramImpl extends AbstractNode implements Program
{
    private List<Node> rules;

    public ProgramImpl()
    {
        rules = new ArrayList<Node>();
    }

    @Override
    public Node clone()
    {
        ProgramImpl cloned = new ProgramImpl();
        for(Node rule : rules)
        {
            Rule clonedRule = (Rule) rule.clone();
            clonedRule.setParent(cloned);
            cloned.getChildren().add(clonedRule);
        }
        return cloned;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb)
    {
        List<Node> children = this.getChildren();
        for(Node child : children)
        {
            child.prettyPrint(sb);
            if (child != children.get(children.size() - 1))
            {
                sb.append(System.lineSeparator());
            }
        }
        return sb;
    }

    public void addRule(Rule rule)
    {
        rules.add(rule);
        rule.setParent(this);
    }

    public void replace(Rule rule, Rule newRule)
    {
        for (int i = 0; i < rules.size(); i++)
        {
            if (rules.get(i) == rule)
            {
                rules.set(i, newRule);
                rule.setParent(null);
                newRule.setParent(this);
                break;
            }
        }
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }

    public void remove(Rule rule)
    {
        rules.remove(rule);
        rule.setParent(null);
    }

    public List<Node> getChildren()
    {
        return rules;
    }

    @Override
    public Program mutate()
    {
        int size = this.size();
        int randomMut = (int)(Math.random() * 6);
        int randomNode = (int)(Math.random() * size);
        Mutation m;

        if(randomMut == 0)
        {
            m = MutationFactory.getRemove();
        }

        else if(randomMut == 1)
        {
            m = MutationFactory.getSwap();
        }

        else if(randomMut == 2)
        {
            m = MutationFactory.getReplace();
        }

        else if(randomMut == 3)
        {
            m = MutationFactory.getTransform();
        }

        else if(randomMut == 4)
        {
            m = MutationFactory.getInsert();
        }

        else
        {
            m = MutationFactory.getDuplicate();
        }

        int count = 0;
        while (!m.canApply(this.nodeAt(randomNode)) && count < size)
        {
            randomNode = (int)(Math.random() * size);
            count++;
        }
        m.apply(this, this.nodeAt(randomNode));
        return this;
    }

    @Override
    public Maybe<Program> mutate(int index, Mutation m)
    {
        return m.apply(this, this.nodeAt(index));
    }

    @Override
    public Maybe<Node> findNodeOfType(NodeCategory type)
    {
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(this);
        while (queue.peek() != null)
        {
            Node head = queue.poll();
            if(head.getCategory() == type) return Maybe.from(head);
            List<Node> children = head.getChildren();
            if (children != null)
            {
                queue.addAll(children);
            }
        }
        return Maybe.none();
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.PROGRAM;
    }


    public boolean classInv() {
        for(Node child : this.getChildren()){
            if(child == null || !child.classInv()) return false;
        }
        return true;
    }
}
