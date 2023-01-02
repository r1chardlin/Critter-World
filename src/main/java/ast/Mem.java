package ast;

import parse.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Mem extends Expr
{
    Expr e;

    public Mem(Expr e)
    {
        this.e = e;
        e.setParent(this);
    }

    public Mem(TokenType sugar)
    {
        if (sugar == TokenType.ABV_MEMSIZE)
        {
            e = new Number(0);
        }
        else if (sugar == TokenType.ABV_DEFENSE)
        {
            e = new Number(1);
        }
        else if (sugar == TokenType.ABV_OFFENSE)
        {
            e = new Number(2);
        }
        else if (sugar == TokenType.ABV_SIZE)
        {
            e = new Number(3);
        }
        else if (sugar == TokenType.ABV_ENERGY)
        {
            e = new Number(4);
        }
        else if (sugar == TokenType.ABV_PASS)
        {
            e = new Number(5);
        }
        // (sugar == TokenType.ABV_POSTURE)
        else
        {
            e = new Number(6);
        }
        e.setParent(this);
    }

    public enum Sugar
    {
        MEMSIZE,
        DEFENSE,
        OFFENSE,
        SIZE,
        ENERGY,
        PASS,
        POSTURE;
    }

    @Override
    public Node clone()
    {
        Expr clonedExpression = (Expr)this.e.clone();
        Mem cloned = new Mem(clonedExpression);
        clonedExpression.setParent(cloned);
        return cloned;
    }

    @Override
    public List<Node> getChildren()
    {
        List<Node> children = new ArrayList<Node>();
        children.add(e);
        return children;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb)
    {
        sb.append("mem[");
        this.e.prettyPrint(sb);
        sb.append("]");
        return sb;
    }

    public Expr getExpr()
    {
        return e;
    }

    public void changeExpr(Expr e)
    {
        this.e = e;
        e.setParent(this);
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }


    @Override
    public boolean classInv()
    {
        return e.classInv();
    }
}
