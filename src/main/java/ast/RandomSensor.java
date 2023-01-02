package ast;

import java.util.ArrayList;
import java.util.List;

public class RandomSensor extends Sensor
{
    Expr e;
    public RandomSensor(Expr e)
    {
        this.e = e;
        e.setParent(this);
    }

    public Expr getExpr()
    {
        return e;
    }

    public void changeExpr(Expr e)
    {
        this.e.setParent(null);
        this.e = e;
        this.e.setParent(this);
    }

    @Override
    public Node clone(){
        Expr clonedExpr = (Expr) this.e.clone();
        RandomSensor cloned = new RandomSensor(clonedExpr);
        clonedExpr.setParent(cloned);
        return cloned;
    }

    @Override
    public List<Node> getChildren()
    {
        List<Node> ret = new ArrayList<Node>();
        ret.add(e);
        return ret;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        sb.append("random[");
        e.prettyPrint(sb);
        sb.append("]");
        return sb;
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }


    @Override
    public boolean classInv() {
        return e.classInv();
    }
}
