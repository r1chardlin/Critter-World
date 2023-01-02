package ast;

import cms.util.maybe.Maybe;
import parse.TokenType;

import java.util.List;

public class Duplicate extends SearchMutation
{

    @Override
    public boolean equals(Mutation m)
    {
        return m instanceof Duplicate;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node)
    {
        if (canApply(node))
        {
            super.setRoot(program);
            node.accept(this);
            return Maybe.from(program);
        }
        return Maybe.none();
    }

    @Override
    public boolean canApply(Node n)
    {
        return n instanceof ProgramImpl || n instanceof Command;
    }

    @Override
    public void visit(ProgramImpl node)
    {
        List<Node> children = node.getChildren();
        int index = super.pickElement(children);
        Rule clone = (Rule) (children.get(index)).clone();
        clone.setParent(node);
        node.addRule(clone);
    }

    @Override
    public void visit(Rule node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(BinaryCondition node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Relation node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(BinaryOp node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Number node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Mem node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(NegativeExpr node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(NearbySensor node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(AheadSensor node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(RandomSensor node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(SmellSensor node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Command node)
    {
        List<Node> subtrees = super.findSubtrees(new Update(new Mem(new Number(0)), new Number(0)));
        List<Node> children = node.getChildren();
        int size = children.size();
        Node lastChild = children.get(size - 1);
        if (lastChild instanceof Action && subtrees.size() > 0)
        {
            int index = super.pickElement(subtrees);
            Update clone = (Update) (subtrees.get(index).clone());
            clone.setParent(node);
            children.set(size - 1, clone);
            children.add(lastChild);
        }
        else if (lastChild instanceof Update)
        {
            List<Node> actionSubtrees = super.findSubtrees(new Action(TokenType.WAIT));
            subtrees.addAll(actionSubtrees);
            int index = super.pickElement(subtrees);
            Node clone = subtrees.get(index).clone();
            ((AbstractNode) clone).setParent(node);
            children.add(clone);
        }
    }

    @Override
    public void visit(Update node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Action node)
    {
        throw new UnsupportedOperationException();
    }
}
