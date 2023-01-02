package ast;

import java.util.ArrayList;
import java.util.List;

/** A representation of a binary Boolean condition: 'and' or 'or' */
public class BinaryCondition extends Condition
{
    private Condition l;
    private Operator op;
    private Condition r;

    /**
     * Create an AST representation of l op r.
     *
     * @param l
     * @param op
     * @param r
     */
    public BinaryCondition(Condition l, Operator op, Condition r)
    {
        this.l = l;
        this.op = op;
        this.r = r;
        this.l.setParent(this);
        this.r.setParent(this);
    }

    /** An enumeration of all possible binary condition operators. */
    public enum Operator
    {
        OR,
        AND;
    }

    public Condition getLeft()
    {
        return l;
    }

    public Operator getOp()
    {
        return op;
    }

    public Condition getRight()
    {
        return r;
    }

    public void changeLeft(Condition left)
    {
        l = left;
        l.setParent(this);
    }

    public void changeOp(Operator newOp)
    {
        op = newOp;
    }

    public void changeRight(Condition right)
    {
        r = right;
        r.setParent(this);
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }

    public List<Node> getChildren(){
        List<Node> list = new ArrayList<Node>();
        list.add(l);
        list.add(r);
        return list;
    }

    @Override
    public Node clone()
    {
        Condition clonedLeft = (Condition) this.l.clone();
        Condition clonedRight = (Condition)this.r.clone();
        BinaryCondition cloned = new BinaryCondition(clonedLeft, this.op, clonedRight);
        clonedLeft.setParent(cloned);
        clonedRight.setParent(cloned);
        return cloned;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb)
    {
        if(this.getParent() instanceof BinaryCondition)
        {
            BinaryCondition parent = (BinaryCondition) this.getParent();
            if(parent.getOp() == Operator.AND && this.op == Operator.OR)
            {
                sb.append("{");
                getLeft().prettyPrint(sb);
                sb.append(" ");
                if(op == Operator.OR)
                {
                    sb.append("or");
                }
                else
                {
                    sb.append("and");
                }
                sb.append(" ");
                getRight().prettyPrint(sb);
                sb.append("}");
                return sb;
            }
        }
        getLeft().prettyPrint(sb);
        sb.append(" ");
        if(op == Operator.OR) sb.append("or");
        else sb.append("and");
        sb.append(" ");
        getRight().prettyPrint(sb);
        return sb;
    }

    public boolean classInv()
    {
        return (l.classInv() && op != null && r.classInv());
    }
}
