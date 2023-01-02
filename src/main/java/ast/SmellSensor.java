package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmellSensor extends Sensor
{
    @Override
    public StringBuilder prettyPrint(StringBuilder sb)
    {
        sb.append("smell");
        return sb;
    }

    @Override
    public Node clone(){
        return new SmellSensor();
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }

    @Override
    public boolean classInv() {
        return true;
    }
}
