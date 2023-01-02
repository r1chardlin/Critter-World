package ast;

import cms.util.maybe.Maybe;
import parse.TokenType;

import java.util.Random;

public class Remove extends AbstractMutation
{

    @Override
    public boolean equals(Mutation m)
    {
        return m instanceof Remove;
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
        if (n instanceof ProgramImpl || n instanceof Relation || n instanceof Number
                || n instanceof SmellSensor || n instanceof Command)
        {
            return false;
        }
        else if (n instanceof Rule)
        {
            return ((Rule) n).getParent().size() > 1;
        }
        else if (n instanceof Update || n instanceof Action)
        {
            Node parent = ((AbstractNode) n).getParent();
            return ((Command) parent).getChildren().size() > 1;
        }
        return true;
    }

    @Override
    public void visit(ProgramImpl node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Rule node)
    {
        ProgramImpl parent = (ProgramImpl) node.getParent();
        if (parent.size() > 1)
        {
            parent.remove(node);
        }
    }

    @Override
    public void visit(BinaryCondition node)
    {
        Node parent = node.getParent();
        int childPicker = (int) (Math.random() * 2);
        if (parent instanceof Rule)
        {
            Condition child = childPicker == 0 ? node.getLeft() : node.getRight();
            ((Rule) parent).changeCondition(child);
        }
        if (parent instanceof BinaryCondition)
        {
            Condition child = childPicker == 0 ? node.getLeft() : node.getRight();
            if (node == ((BinaryCondition) parent).getLeft())
            {
                ((BinaryCondition) parent).changeLeft(child);
            }
            else
            {
                ((BinaryCondition) parent).changeRight(child);
            }
        }
    }

    @Override
    public void visit(Relation node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(BinaryOp node)
    {
        Node parent = node.getParent();
        int childPicker = (int) (Math.random() * 2);
        if (parent instanceof BinaryOp)
        {
            Expr child = childPicker == 0 ? node.getLeft() : node.getRight();
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(child);
            }
            else
            {
                ((BinaryOp) parent).changeRight(child);
            }
        }
        if (parent instanceof Relation)
        {
            Expr child = childPicker == 0 ? node.getLeft() : node.getRight();
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(child);
            }
            else
            {
                ((Relation) parent).changeRight(child);
            }
        }
        else if (parent instanceof Update)
        {
            Expr child = childPicker == 0 ? node.getLeft() : node.getRight();
            ((Update) parent).changeExpr(child);
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                Expr child = childPicker == 0 ? node.getLeft() : node.getRight();
                ((Action) parent).changeExpr(child);
            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
        }
        else if (parent instanceof Mem)
        {
            Expr child = childPicker == 0 ? node.getLeft() : node.getRight();
            ((Mem) parent).changeExpr(child);
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                Expr child = childPicker == 0 ? node.getLeft() : node.getRight();
                ((NearbySensor) parent).changeExpr(child);
            }
            else if (parent instanceof AheadSensor)
            {
                Expr child = childPicker == 0 ? node.getLeft() : node.getRight();
                ((AheadSensor) parent).changeExpr(child);;
            }
            else if (parent instanceof RandomSensor)
            {
                Expr child = childPicker == 0 ? node.getLeft() : node.getRight();
                ((RandomSensor) parent).changeExpr(child);
            }
        }
    }

    @Override
    public void visit(Number node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Mem node)
    {
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            Expr child = node.getExpr();
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(child);
            }
            else
            {
                ((BinaryOp) parent).changeRight(child);
            }
        }
        if (parent instanceof Relation)
        {
            Expr child = node.getExpr();
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(child);
            }
            else
            {
                ((Relation) parent).changeRight(child);
            }
        }
        else if (parent instanceof Update)
        {
            Expr child = node.getExpr();
            if (node == ((Update) parent).getMemType() && child instanceof Mem)
            {
                ((Update) parent).changeMemType((Mem) child);
            }
            else
            {
                ((Update) parent).changeExpr(child);
            }
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                Expr child = node.getExpr();
                ((Action) parent).changeExpr(child);
            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
        }
        else if (parent instanceof Mem)
        {
            Expr child = node.getExpr();
            ((Mem) parent).changeExpr(child);
        }
        else if (parent instanceof NegativeExpr)
        {
            Expr child = node.getExpr();
            ((NegativeExpr) parent).changeRight(child);
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                Expr child = node.getExpr();
                ((NearbySensor) parent).changeExpr(child);
            }
            else if (parent instanceof AheadSensor)
            {
                Expr child = node.getExpr();
                ((AheadSensor) parent).changeExpr(child);
            }
            else if (parent instanceof RandomSensor)
            {
                Expr child = node.getExpr();
                ((RandomSensor) parent).changeExpr(child);
            }
        }
    }

    @Override
    public void visit(NegativeExpr node)
    {
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            Expr child = node.getRight();
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(child);
            }
            else
            {
                ((BinaryOp) parent).changeRight(child);
            }
        }
        if (parent instanceof Relation)
        {
            Expr child = node.getRight();
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(child);
            }
            else
            {
                ((Relation) parent).changeRight(child);
            }
        }
        else if (parent instanceof Update)
        {
            Expr child = node.getRight();
            ((Update) parent).changeExpr(child);
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                Expr child = node.getRight();
                ((Action) parent).changeExpr(child);
            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
        }
        else if (parent instanceof Mem)
        {
            Expr child = node.getRight();
            ((Mem) parent).changeExpr(child);
        }
        else if (parent instanceof NegativeExpr)
        {
            Expr child = node.getRight();
            ((NegativeExpr) parent).changeRight(child);
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                Expr child = node.getRight();
                ((NearbySensor) parent).changeExpr(child);
            }
            else if (parent instanceof AheadSensor)
            {
                Expr child = node.getRight();
                ((AheadSensor) parent).changeExpr(child);
            }
            else if (parent instanceof RandomSensor)
            {
                Expr child = node.getRight();
                ((RandomSensor) parent).changeExpr(child);
            }
        }
    }

    @Override
    public void visit(NearbySensor node)
    {
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            Expr child = node.getExpr();
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(child);
            }
            else
            {
                ((BinaryOp) parent).changeRight(child);
            }
        }
        if (parent instanceof Relation)
        {
            Expr child = node.getExpr();
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(child);
            }
            else
            {
                ((Relation) parent).changeRight(child);
            }
        }
        else if (parent instanceof Update)
        {
            Expr child = node.getExpr();
            ((Update) parent).changeExpr(child);
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                Expr child = node.getExpr();
                ((Action) parent).changeExpr(child);
            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
        }
        else if (parent instanceof Mem)
        {
            Expr child = node.getExpr();
            ((Mem) parent).changeExpr(child);
        }
        else if (parent instanceof NegativeExpr)
        {
            Expr child = node.getExpr();
            ((NegativeExpr) parent).changeRight(child);
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                Expr child = node.getExpr();
                ((NearbySensor) parent).changeExpr(child);
            }
            else if (parent instanceof AheadSensor)
            {
                Expr child = node.getExpr();
                ((AheadSensor) parent).changeExpr(child);
            }
            else if (parent instanceof RandomSensor)
            {
                Expr child = node.getExpr();
                ((RandomSensor) parent).changeExpr(child);
            }
        }
    }

    @Override
    public void visit(AheadSensor node)
    {
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            Expr child = node.getExpr();
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(child);
            }
            else
            {
                ((BinaryOp) parent).changeRight(child);
            }
        }
        if (parent instanceof Relation)
        {
            Expr child = node.getExpr();
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(child);
            }
            else
            {
                ((Relation) parent).changeRight(child);
            }
        }
        else if (parent instanceof Update)
        {
            Expr child = node.getExpr();
            ((Update) parent).changeExpr(child);
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                Expr child = node.getExpr();
                ((Action) parent).changeExpr(child);
            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
        }
        else if (parent instanceof Mem)
        {
            Expr child = node.getExpr();
            ((Mem) parent).changeExpr(child);
        }
        else if (parent instanceof NegativeExpr)
        {
            Expr child = node.getExpr();
            ((NegativeExpr) parent).changeRight(child);
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                Expr child = node.getExpr();
                ((NearbySensor) parent).changeExpr(child);
            }
            else if (parent instanceof AheadSensor)
            {
                Expr child = node.getExpr();
                ((AheadSensor) parent).changeExpr(child);
            }
            else if (parent instanceof RandomSensor)
            {
                Expr child = node.getExpr();
                ((RandomSensor) parent).changeExpr(child);
            }
        }
    }

    @Override
    public void visit(RandomSensor node)
    {
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            Expr child = node.getExpr();
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(child);
            }
            else
            {
                ((BinaryOp) parent).changeRight(child);
            }
        }
        if (parent instanceof Relation)
        {
            Expr child = node.getExpr();
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(child);
            }
            else
            {
                ((Relation) parent).changeRight(child);
            }
        }
        else if (parent instanceof Update)
        {
            Expr child = node.getExpr();
            ((Update) parent).changeExpr(child);
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                Expr child = node.getExpr();
                ((Action) parent).changeExpr(child);
            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
        }
        else if (parent instanceof Mem)
        {
            Expr child = node.getExpr();
            ((Mem) parent).changeExpr(child);
        }
        else if (parent instanceof NegativeExpr)
        {
            Expr child = node.getExpr();
            ((NegativeExpr) parent).changeRight(child);
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                Expr child = node.getExpr();
                ((NearbySensor) parent).changeExpr(child);
            }
            else if (parent instanceof AheadSensor)
            {
                Expr child = node.getExpr();
                ((AheadSensor) parent).changeExpr(child);
            }
            else if (parent instanceof RandomSensor)
            {
                Expr child = node.getExpr();
                ((RandomSensor) parent).changeExpr(child);
            }
        }
    }

    @Override
    public void visit(SmellSensor node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Command node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Update node)
    {
        Node parent = node.getParent();
        if (((Command) parent).getChildren().size() > 1)
        {
            ((Command) parent).remove(node);
        }
    }

    @Override
    public void visit(Action node)
    {
        Node parent = node.getParent();
        if (((Command) parent).getChildren().size() > 1)
        {
            ((Command) parent).remove(node);
        }
    }
}
