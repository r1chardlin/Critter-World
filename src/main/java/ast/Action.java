package ast;

import parse.TokenType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Action extends AbstractNode
{
    TokenType name;
    Expr value;

    public Action(TokenType name)
    {
        this.name = name;
    }

    public Action(TokenType name, Expr value)
    {
        this.name = name;
        this.value = value;
        value.setParent(this);
    }

    @Override
    public Node clone()
    {
        Action ret;
        if (value != null)
        {
            Expr clonedValue = (Expr) (value.clone());
            ret = new Action(name, clonedValue);
            clonedValue.setParent(ret);
        }
        else
        {
            ret = new Action(name);
        }
        return ret;
    }

    @Override
    public List<Node> getChildren()
    {
        if (value != null)
        {
            List<Node> list = new ArrayList<Node>();
            list.add(value);
            return list;
        }
        else return new ArrayList<>();
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb)
    {
        if(value == null)
        {
            sb.append(name.toString());
            return sb;
        }
        sb.append(name.toString());
        sb.append("[");
        value.prettyPrint(sb);
        sb.append("]");
        return sb;
    }

    public TokenType getName()
    {
        return name;
    }

    public Expr getExpr()
    {
        return value;
    }

    public void changeName(TokenType n)
    {
        name = n;
    }

    public void changeExpr(Expr e)
    {
        value = e;
        value.setParent(this);
    }

    @Override
    public NodeCategory getCategory()
    {
        return NodeCategory.ACTION;
    }

    @Override
    public boolean classInv() {
        if(name != TokenType.SMELL) return name != null;
        else return (name != null && value.classInv());
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }
}
