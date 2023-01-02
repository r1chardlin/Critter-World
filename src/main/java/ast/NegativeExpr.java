package ast;

import java.util.ArrayList;
import java.util.List;

public class NegativeExpr extends Expr
{
    private Expr factor;

    public NegativeExpr(Expr factor)
    {
        this.factor = factor;
        this.factor.setParent(this);
    }

    @Override
    public Node clone()
    {
        Expr clonedFactor =  (Expr) this.factor.clone();
        NegativeExpr cloned = new NegativeExpr(clonedFactor);
        clonedFactor.setParent(cloned);
        return cloned;
    }

    @Override
    public List<Node> getChildren()
    {
        ArrayList<Node> children = new ArrayList<>();
        children.add(factor);
        return children;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb)
    {
        sb.append("-");
//        sb.append(" ");
        factor.prettyPrint(sb);
        return sb;
    }

    public Expr getRight()
    {
        return factor;
    }

    public void changeRight(Expr newFactor)
    {
        factor.setParent(null);
        factor = newFactor;
        factor.setParent(this);
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }


    @Override
    public boolean classInv()
    {
        return factor.classInv();
    }
}
