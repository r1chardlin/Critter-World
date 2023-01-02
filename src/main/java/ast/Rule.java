package ast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A representation of a critter rule. */
public class Rule extends AbstractNode {

    private Condition condition;
    private Command command;

    public Rule(Condition condition, Command command)
    {
        this.condition = condition;
        this.command = command;
        this.condition.setParent(this);
        this.command.setParent(this);
    }
    @Override
    public Node clone(){
        Condition clonedCondition = (Condition)this.condition.clone();
        Command clonedCommand = (Command) this.command.clone();
        Rule cloned = new Rule(clonedCondition, clonedCommand);
        clonedCondition.setParent(cloned);
        clonedCommand.setParent(cloned);
        return cloned;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        condition.prettyPrint(sb);
        sb.append((" --> "));
        command.prettyPrint(sb);
        sb.append(";");

        return sb;
    }

    public Condition getCondition()
    {
        return condition;
    }

    public Command getCommand()
    {
        return command;
    }

    public List<Node> getChildren()
    {
        List<Node> list = new ArrayList<Node>();
        list.add(condition);
        list.add(command);
        return list;
    }

    public void changeCondition(Condition c)
    {
        condition.setParent(null);
        condition = c;
        condition.setParent(this);
    }

    public void changeCommand(Command c)
    {
        command.setParent(null);
        command = c;
        command.setParent(this);
    }

    @Override
    public NodeCategory getCategory()
    {
        return NodeCategory.RULE;
    }


    public boolean classInv()
    {
       return condition.classInv() && command.classInv();
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }
}
