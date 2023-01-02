package ast;

/** A critter program expression that has an integer value. */
public abstract class Expr extends AbstractNode
{
    @Override
    public NodeCategory getCategory()
    {
        return NodeCategory.EXPRESSION;
    }
}
