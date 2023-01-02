package ast;

import cms.util.maybe.Maybe;
import parse.TokenType;

import java.util.Random;

public class Transform extends AbstractMutation
{
    private Program root;
    @Override
    public boolean equals(Mutation m)
    {
        return m instanceof Transform;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node)
    {
        if (canApply(node))
        {
            root = program;
            node.accept(this);
            return Maybe.from(program);
        }
        return Maybe.none();
    }

    @Override
    public boolean canApply(Node n)
    {
        if (n instanceof Action)
        {
            return ((Action) n).getName() != TokenType.SERVE;
        }
        return n instanceof BinaryCondition || n instanceof Relation || n instanceof BinaryOp || n instanceof Number;
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
        int opPicker = (int) (Math.random() * 2);
        if (opPicker == 0)
        {
            node.changeOp(BinaryCondition.Operator.OR);
        }
        else
        {
            node.changeOp(BinaryCondition.Operator.AND);
        }
    }

    @Override
    public void visit(Relation node)
    {
        int relPicker = (int) (Math.random() * 6);
        if (relPicker == 0)
        {
            node.changeRel(TokenType.LT);
        }
        else if (relPicker == 1)
        {
            node.changeRel(TokenType.LE);
        }
        else if (relPicker == 2)
        {
            node.changeRel(TokenType.EQ);
        }
        else if (relPicker == 3)
        {
            node.changeRel(TokenType.GE);
        }
        else if (relPicker == 4)
        {
            node.changeRel(TokenType.GT);
        }
        else if (relPicker == 5)
        {
            node.changeRel(TokenType.NE);
        }
    }

    @Override
    public void visit(BinaryOp node)
    {
        int opPicker = (int) (Math.random() * 5);
        if (opPicker == 0)
        {
            node.changeOp(TokenType.PLUS);
        }
        else if (opPicker == 1)
        {
            node.changeOp(TokenType.MINUS);
        }
        else if (opPicker == 2)
        {
            node.changeOp(TokenType.MUL);
        }
        else if (opPicker == 3)
        {
            node.changeOp(TokenType.DIV);
        }
        else if (opPicker == 4)
        {
            node.changeOp(TokenType.MOD);
        }
    }

    @Override
    public void visit(Number node)
    {
        Random r = new Random();
        node.changeNum(Integer.MAX_VALUE / r.nextInt());
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
        if (node.getName() != TokenType.SERVE)
        {
            int actionPicker = (int) (Math.random() * 10);
            if (actionPicker == 0)
            {
                node.changeName(TokenType.WAIT);
            }
            else if (actionPicker == 1)
            {
                node.changeName(TokenType.FORWARD);
            }
            else if (actionPicker == 2)
            {
                node.changeName(TokenType.BACKWARD);
            }
            else if (actionPicker == 3)
            {
                node.changeName(TokenType.LEFT);
            }
            else if (actionPicker == 4)
            {
                node.changeName(TokenType.RIGHT);
            }
            else if (actionPicker == 5)
            {
                node.changeName(TokenType.EAT);
            }
            else if (actionPicker == 6)
            {
                node.changeName(TokenType.ATTACK);
            }
            else if (actionPicker == 7)
            {
                node.changeName(TokenType.GROW);
            }
            else if (actionPicker == 8)
            {
                node.changeName(TokenType.BUD);
            }
            else if (actionPicker == 9)
            {
                node.changeName(TokenType.MATE);
            }
        }
    }
}
