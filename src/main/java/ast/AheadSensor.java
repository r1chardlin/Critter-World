package ast;

import java.util.ArrayList;
import java.util.List;

public class AheadSensor extends Sensor
{


    Expr e;
    public AheadSensor(Expr e)
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
        this.e = e;
        this.e.setParent(this);
    }

    @Override
    public Node clone(){
        Expr clonedExpr = (Expr) this.e.clone();
        AheadSensor cloned = new AheadSensor(clonedExpr);
        clonedExpr.setParent(cloned);
        return cloned;
    }

    @Override
    public List<Node> getChildren()
    {
        List<Node> children = new ArrayList<>();
        children.add(e);
        return children;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        sb.append("ahead[");
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
