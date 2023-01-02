package ast;

import parse.TokenCategory;

import java.util.LinkedList;
import java.util.List;

public class Command extends AbstractNode
{

    private LinkedList<Node> children;

    public Command()
    {
        children = new LinkedList<>();
    }

    @Override
    public Node clone()
    {
        Command cloned = new Command();
        for(Node child : this.children)
        {
            if(child.getClass() == Update.class){
                Update clonedChild = (Update) child.clone();
                cloned.getChildren().add(clonedChild);
                clonedChild.setParent(cloned);
            }

            else if(child.getClass() == Action.class){
                Action clonedAction = (Action) child.clone();
                cloned.getChildren().add(clonedAction);
                clonedAction.setParent(cloned);

            }


        }
        return cloned;
    }

    @Override
    public List<Node> getChildren()
    {
        return children;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        for(Node child : this.children){
            child.prettyPrint(sb);
            sb.append(" ");
        }
        sb.deleteCharAt(sb.length() -1);
        return sb;
    }

    public void add(Node node)
    {
        children.add(node);
        ((AbstractNode) node).setParent(this);
    }

    public void remove(Node node)
    {
        children.remove(node);
        ((AbstractNode) node).setParent(null);
    }

    public void replace(Update update, Update newUpdate)
    {
        for (int i = 0; i < children.size(); i++)
        {
            if (children.get(i) == update)
            {
                children.set(i, newUpdate);
                update.setParent(null);
                newUpdate.setParent(this);
                break;
            }
        }
    }
    public void replace(Action action, Action newAction)
    {
        int size = children.size();
        if (children.get(size - 1) == action)
        {
            children.set(size - 1, newAction);
            action.setParent(null);
            newAction.setParent(this);
        }
    }

    @Override
    public NodeCategory getCategory()
    {
        return NodeCategory.COMMAND;
    }

    @Override
    public boolean classInv()
    {
        for(Node child : this.getChildren()){
            if(child == null || !child.classInv()) return false;
        }
        return true;
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }
}
