package ast;

import parse.TokenCategory;
import parse.TokenType;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Binary Operation
 * @author Richard Lin
 * @author Benjamin Li
 * @author Allison Zheng
 * @version
 * Class invariant: The global variable op must be of TokenCategory ADDOP or MULOP
 */
public class BinaryOp extends Expr
{
    private Expr left;
    private TokenType op;
    private Expr right;

    public BinaryOp(Expr left, TokenType op, Expr right)
    {
        this.left = left;
        this.op = op;
        this.right = right;
        this.left.setParent(this);
        this.right.setParent(this);
    }

    @Override
    public Node clone()
    {
        Expr clonedLeft = (Expr) left.clone();
        Expr clonedRight = (Expr) right.clone();
        BinaryOp cloned =  new BinaryOp(clonedLeft, op, clonedRight);
        clonedLeft.setParent(cloned);
        clonedRight.setParent(cloned);
        return cloned;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb)
    {
        if (this.getParent() instanceof BinaryOp)
        {
            BinaryOp parent = (BinaryOp) this.getParent();
            if ((parent.getOp().category() == TokenCategory.MULOP && this.op.category() == TokenCategory.ADDOP)
                    || (this == parent.getRight() && (parent.getOp().category() == TokenCategory.MULOP || this.op.category() == TokenCategory.ADDOP)))
            {
                sb.append("(");
                getLeft().prettyPrint(sb);
                sb.append(" ");
                sb.append(op.toString());
                sb.append(" ");
                getRight().prettyPrint(sb);
                sb.append(")");
                return sb;
            }
        }
        getLeft().prettyPrint(sb);
        sb.append(" ");
        sb.append(op.toString());
        sb.append(" ");
        getRight().prettyPrint(sb);
        return sb;
    }

    public Expr getLeft()
    {
        return left;
    }

    public TokenType getOp()
    {
        return op;
    }

    public Expr getRight()
    {
        return right;
    }

    public void changeLeft(Expr l)
    {
        left = l;
        left.setParent(this);
    }

    public void changeOp(TokenType newOp)
    {
        op = newOp;
    }

    public void changeRight(Expr r)
    {
        right.setParent(null);
        right = r;
        right.setParent(this);
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }

    public List<Node> getChildren()
    {
        List<Node> list = new ArrayList<Node>();
        list.add(left);
        list.add(right);
        return list;
    }

    @Override
    public boolean classInv()
    {
        return (left.classInv() && op != null && right.classInv());
    }
}
