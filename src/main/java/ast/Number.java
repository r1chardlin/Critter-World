package ast;

import java.util.ArrayList;
import java.util.List;

public class Number extends Expr
{
    private int num;

    public Number(int num)
    {
        this.num = num;
    }

    public Node clone()
    {
        return new Number(this.getNum());
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        sb.append(num);
        return sb;
    }

    public int getNum()
    {
        return num;
    }

    public void changeNum(int newNum)
    {
        num = newNum;
    }

    @Override
    public List<Node> getChildren()
    {
        return new ArrayList<Node>();
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }


    @Override
    public boolean classInv()
    {
        return true;
    }
}
