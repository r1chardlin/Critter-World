package ast;

import cms.util.maybe.Maybe;
import parse.TokenType;
import java.util.List;

public class Insert extends SearchMutation
{

    @Override
    public boolean equals(Mutation m)
    {
        return m instanceof Insert;
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
        return n instanceof Condition || n instanceof Expr;
    }

    @Override
    public void visit(ProgramImpl node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Rule node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(BinaryCondition node)
    {
        Node ogParent = node.getParent();
        if (ogParent instanceof BinaryCondition)
        {
            List<Node> subtrees = super.findSubtrees(node);
            int index = super.pickElement(subtrees);
            Condition child = (Condition) subtrees.get(index).clone();
            int opPicker = (int) (Math.random() * 2);
            Condition newParent = opPicker == 0 ? new BinaryCondition(node, BinaryCondition.Operator.OR, child) :
                    new BinaryCondition(node, BinaryCondition.Operator.AND, child);
            if (node == ((BinaryCondition) ogParent).getLeft())
            {
                ((BinaryCondition) ogParent).changeLeft(newParent);
            }
            else
            {
                ((BinaryCondition) ogParent).changeRight(newParent);
            }
            newParent.setParent(ogParent);
            node.setParent(newParent);
            child.setParent(newParent);
        }
        else if (ogParent instanceof Rule)
        {
            List<Node> subtrees = super.findSubtrees(node);
            int index = super.pickElement(subtrees);
            Condition child = (Condition) subtrees.get(index).clone();
            int opPicker = (int) (Math.random() * 2);
            Condition newParent = opPicker == 0 ? new BinaryCondition(node, BinaryCondition.Operator.OR, child) :
                    new BinaryCondition(node, BinaryCondition.Operator.AND, child);
            ((Rule) ogParent).changeCondition(newParent);
            newParent.setParent(ogParent);
            node.setParent(newParent);
            child.setParent(newParent);
        }
    }

    @Override
    public void visit(Relation node)
    {
        Node ogParent = node.getParent();
        if (ogParent instanceof BinaryCondition)
        {
            List<Node> subtrees = super.findSubtrees(node);
            int index = super.pickElement(subtrees);
            Condition child = (Condition) subtrees.get(index).clone();
            int opPicker = (int) (Math.random() * 2);
            Condition newParent = opPicker == 0 ? new BinaryCondition(node, BinaryCondition.Operator.OR, child) :
                    new BinaryCondition(node, BinaryCondition.Operator.AND, child);
            if (node == ((BinaryCondition) ogParent).getLeft())
            {
                ((BinaryCondition) ogParent).changeLeft(newParent);
            }
            else
            {
                ((BinaryCondition) ogParent).changeRight(newParent);
            }
            newParent.setParent(ogParent);
            node.setParent(newParent);
            child.setParent(newParent);
        }
        else if (ogParent instanceof Rule)
        {
            List<Node> subtrees = super.findSubtrees(node);
            int index = super.pickElement(subtrees);
            Condition child = (Condition) subtrees.get(index).clone();
            int opPicker = (int) (Math.random() * 2);
            Condition newParent = opPicker == 0 ? new BinaryCondition(node, BinaryCondition.Operator.OR, child) :
                    new BinaryCondition(node, BinaryCondition.Operator.AND, child);
            ((Rule) ogParent).changeCondition(newParent);
            newParent.setParent(ogParent);
            node.setParent(newParent);
            child.setParent(newParent);
        }
    }

    @Override
    public void visit(BinaryOp node)
    {
        visitExpr(node);
    }

    @Override
    public void visit(Number node)
    {
        visitExpr(node);
    }

    @Override
    public void visit(Mem node)
    {
        visitExpr(node);
    }

    @Override
    public void visit(NegativeExpr node)
    {
        visitExpr(node);
    }

    @Override
    public void visit(NearbySensor node)
    {
        visitExpr(node);
    }

    @Override
    public void visit(AheadSensor node)
    {
        visitExpr(node);
    }

    @Override
    public void visit(RandomSensor node)
    {
        visitExpr(node);
    }

    @Override
    public void visit(SmellSensor node)
    {
        visitExpr(node);
    }

    @Override
    public void visit(Command node)
    {
        throw new UnsupportedOperationException();
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

    public void visitExpr(Expr node)
    {
        Node ogParent = node.getParent();
        if (ogParent instanceof BinaryOp)
        {
            List<Node> subtrees = super.findSubtrees(node);
            int index = super.pickElement(subtrees);
            Expr child = (Expr) subtrees.get(index).clone();
            int opPicker = (int) (Math.random() * 5);
            BinaryOp newParent;
            if (opPicker == 0)
            {
                newParent = new BinaryOp(node, TokenType.PLUS, child);
            }
            else if (opPicker == 1)
            {
                newParent = new BinaryOp(node, TokenType.MINUS, child);
            }
            else if (opPicker == 2)
            {
                newParent = new BinaryOp(node, TokenType.MUL, child);
            }
            else if (opPicker == 3)
            {
                newParent = new BinaryOp(node, TokenType.DIV, child);
            }
            else
            {
                newParent = new BinaryOp(node, TokenType.MOD, child);
            }

            if (node == ((BinaryOp) ogParent).getLeft())
            {
                ((BinaryOp) ogParent).changeLeft(newParent);
            }
            else
            {
                ((BinaryOp) ogParent).changeRight(newParent);
            }
            newParent.setParent(ogParent);
            node.setParent(newParent);
            child.setParent(newParent);
        }
        else if (ogParent instanceof Relation)
        {
            List<Node> subtrees = super.findSubtrees(node);
            int index = super.pickElement(subtrees);
            Expr child = (Expr) subtrees.get(index).clone();
            int opPicker = (int) (Math.random() * 5);
            BinaryOp newParent;
            if (opPicker == 0)
            {
                newParent = new BinaryOp(node, TokenType.PLUS, child);
            }
            else if (opPicker == 1)
            {
                newParent = new BinaryOp(node, TokenType.MINUS, child);
            }
            else if (opPicker == 2)
            {
                newParent = new BinaryOp(node, TokenType.MUL, child);
            }
            else if (opPicker == 3)
            {
                newParent = new BinaryOp(node, TokenType.DIV, child);
            }
            else
            {
                newParent = new BinaryOp(node, TokenType.MOD, child);
            }

            if (node == ((Relation) ogParent).getLeft())
            {
                ((Relation) ogParent).changeLeft(newParent);
            }
            else
            {
                ((Relation) ogParent).changeRight(newParent);
            }
            newParent.setParent(ogParent);
            node.setParent(newParent);
            child.setParent(newParent);
        }
        else if (ogParent instanceof Update)
        {
            Mem newParent = new Mem(node);
            if (node == ((Update) ogParent).getMemType())
            {
                ((Update) ogParent).changeMemType(newParent);
            }
            else
            {
                ((Update) ogParent).changeExpr(newParent);
            }
            newParent.setParent(ogParent);
            node.setParent(newParent);
        }
        else if (ogParent instanceof Mem)
        {
            Mem newParent = new Mem(node);
            ((Mem) ogParent).changeExpr(newParent);
            newParent.setParent(ogParent);
            node.setParent(newParent);
        }
        else if (ogParent instanceof NearbySensor)
        {
            NearbySensor newParent = new NearbySensor(node);
            ((NearbySensor) ogParent).changeExpr(newParent);
            newParent.setParent(ogParent);
            node.setParent(newParent);
        }
        else if (ogParent instanceof AheadSensor)
        {
            AheadSensor newParent = new AheadSensor(node);
            ((AheadSensor) ogParent).changeExpr(newParent);
            newParent.setParent(ogParent);
            node.setParent(newParent);
        }
        else if (ogParent instanceof RandomSensor)
        {
            RandomSensor newParent = new RandomSensor(node);
            ((RandomSensor) ogParent).changeExpr(newParent);
            newParent.setParent(ogParent);
            node.setParent(newParent);
        }
        else if (ogParent instanceof Action)
        {
            List<Node> subtrees = super.findSubtrees(node);
            int index = super.pickElement(subtrees);
            Expr child = (Expr) subtrees.get(index).clone();
            int opPicker = (int) (Math.random() * 5);
            BinaryOp newParent;
            if (opPicker == 0)
            {
                newParent = new BinaryOp(node, TokenType.PLUS, child);
            }
            else if (opPicker == 1)
            {
                newParent = new BinaryOp(node, TokenType.MINUS, child);
            }
            else if (opPicker == 2)
            {
                newParent = new BinaryOp(node, TokenType.MUL, child);
            }
            else if (opPicker == 3)
            {
                newParent = new BinaryOp(node, TokenType.DIV, child);
            }
            else
            {
                newParent = new BinaryOp(node, TokenType.MOD, child);
            }
            ((Action) ogParent).changeExpr(newParent);
            newParent.setParent(ogParent);
            node.setParent(newParent);
            child.setParent(newParent);
        }
    }
}
