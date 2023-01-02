package ast;

import cms.util.maybe.Maybe;

import java.util.ArrayList;
import java.util.List;

public class Swap extends AbstractMutation
{

    @Override
    public boolean equals(Mutation m)
    {
        return m instanceof Swap;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node)
    {
        if (canApply(node))
        {
            node.accept(this);
            return Maybe.from(program);
        }
        return Maybe.none();
    }

    @Override
    public boolean canApply(Node n)
    {
        if (n instanceof BinaryCondition || n instanceof Relation || n instanceof BinaryOp)
        {
            return true;
        }
        else if (n instanceof ProgramImpl || n instanceof Command)
        {
            int size = n.getChildren().size();
            if (size < 2)
            {
                return false;
            }
            return !(n instanceof Command && n.getChildren().get(size - 1) instanceof Action && size == 2);
        }
        return false;
    }

    @Override
    public void visit(ProgramImpl node)
    {
        if (node.getChildren().size() >= 2)
        {
            List<Node> children = node.getChildren();
            int childPicker1 = (int) (Math.random() * children.size());
            int childPicker2 = (int) (Math.random() * children.size());
            while (childPicker1 == childPicker2)
            {
                childPicker2 = (int) (Math.random() * node.getChildren().size());
            }
            Node temp = node.getChildren().get(childPicker1);
            Node temp2 = node.getChildren().get(childPicker2);
            node.getChildren().set(childPicker1, temp2);
            node.getChildren().set(childPicker2, temp);
        }
    }

    @Override
    public void visit(Rule node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(BinaryCondition node)
    {
        Condition left = node.getLeft();
        Condition right = node.getRight();
        node.changeLeft(right);
        node.changeRight(left);
    }

    @Override
    public void visit(Relation node)
    {
        Expr left = node.getLeft();
        Expr right = node.getRight();
        node.changeLeft(right);
        node.changeRight(left);
    }

    @Override
    public void visit(BinaryOp node)
    {
        Expr left = node.getLeft();
        Expr right = node.getRight();
        node.changeLeft(right);
        node.changeRight(left);
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
        if (node.getChildren().size() >= 2)
        {
            List<Node> children = node.getChildren();
            int childPicker1;
            int childPicker2;
            if (children.get(children.size() - 1) instanceof Action)
            {
                childPicker1 = (int) (Math.random() * children.size() - 1);
                childPicker2 = (int) (Math.random() * children.size() - 1);
            }
            else
            {
                childPicker1 = (int) (Math.random() * (children.size()));
                childPicker2 = (int) (Math.random() * (children.size()));
            }
            while (childPicker1 == childPicker2)
            {
                childPicker2 = (int) (Math.random() * node.getChildren().size());
            }
            Node temp = children.get(childPicker1);
            Node temp2 = children.get(childPicker2);
            node.getChildren().set(childPicker1, temp2);
            node.getChildren().set(childPicker2, temp);
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
