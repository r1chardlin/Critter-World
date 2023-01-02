package ast;

import cms.util.maybe.Maybe;
import parse.TokenType;

import java.util.Iterator;
import java.util.List;

public class Replace extends SearchMutation
{
    @Override
    public boolean equals(Mutation m)
    {
        return m instanceof Replace;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node)
    {
        if (canApply(node))
        {
            super.setRoot(program);
            node.accept(this);
            return Maybe.from(program);
        }
        return Maybe.none();
    }

    @Override
    public boolean canApply(Node n)
    {
        return !(n instanceof ProgramImpl);
    }

    @Override
    public void visit(ProgramImpl node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Rule node)
    {
        List<Node> subtrees = super.findSubtrees(node);
        int index = super.pickElement(subtrees);
        Node replacementNode = subtrees.get(index);
        Node parent = node.getParent();
        Rule clone = (Rule) (replacementNode.clone());
        ((ProgramImpl) parent).replace(node, clone);
    }

    @Override
    public void visit(BinaryCondition node)
    {
        List<Node> subtrees = super.findSubtrees(node);
        int index = super.pickElement(subtrees);
        Node replacementNode = subtrees.get(index);
        Condition clone = (Condition) (replacementNode.clone());
        Node parent = node.getParent();
        if (parent instanceof Rule)
        {
            ((Rule) parent).changeCondition(clone);
        }
        else if (parent instanceof BinaryCondition)
        {
            if (node == ((BinaryCondition) parent).getLeft())
            {
                ((BinaryCondition) parent).changeLeft(clone);
            }
            else
            {
                ((BinaryCondition) parent).changeLeft(clone);
            }
        }
    }

    @Override
    public void visit(Relation node)
    {
        List<Node> subtrees = super.findSubtrees(node);
        int index = super.pickElement(subtrees);
        Node replacementNode = subtrees.get(index);
        Condition clone = (Condition) (replacementNode.clone());
        Node parent = node.getParent();
        if (parent instanceof Rule)
        {
            ((Rule) parent).changeCondition(clone);
        }
        else if (parent instanceof BinaryCondition)
        {
            if (node == ((BinaryCondition) parent).getLeft())
            {
                ((BinaryCondition) parent).changeLeft(clone);
            }
            else
            {
                ((BinaryCondition) parent).changeRight(clone);
            }
        }
    }

    @Override
    public void visit(BinaryOp node)
    {
        List<Node> subtrees = super.findSubtrees(node);
        int index = super.pickElement(subtrees);
        Node replacementNode = subtrees.get(index);
        Node parent = node.getParent();
        Expr clone = (Expr) (replacementNode.clone());
        if (parent instanceof BinaryOp)
        {
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(clone);
            }
            else
            {
                ((BinaryOp) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Relation)
        {
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(clone);
            }
            else
            {
                ((Relation) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Update)
        {
            ((Update) parent).changeExpr(clone);
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                ((Action) parent).changeExpr(clone);
            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
        }
        else if (parent instanceof Mem)
        {
            ((Mem) parent).changeExpr(clone);
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                ((NearbySensor) parent).changeExpr(clone);
            }
            else if (parent instanceof AheadSensor)
            {
                ((AheadSensor) parent).changeExpr((clone));
            }
            else if (parent instanceof RandomSensor)
            {
                ((RandomSensor) parent).changeExpr(clone);
            }
        }
    }

    @Override
    public void visit(Number node)
    {
        List<Node> subtrees = super.findSubtrees(node);
        int index = super.pickElement(subtrees);
        Node replacementNode = subtrees.get(index);
        Expr clone = (Expr) (replacementNode.clone());
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(clone);
            }
            else
            {
                ((BinaryOp) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Relation)
        {
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(clone);
            }
            else
            {
                ((Relation) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Update)
        {
            ((Update) parent).changeExpr(clone);
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                ((Action) parent).changeExpr(clone);
            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
        }
        else if (parent instanceof Mem)
        {
            ((Mem) parent).changeExpr(clone);
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                ((NearbySensor) parent).changeExpr(clone);
            }
            else if (parent instanceof AheadSensor)
            {
                ((AheadSensor) parent).changeExpr((clone));
            }
            else if (parent instanceof RandomSensor)
            {
                ((RandomSensor) parent).changeExpr(clone);
            }
        }
    }

    @Override
    public void visit(Mem node)
    {
        List<Node> subtrees = super.findSubtrees(node);
        int index = super.pickElement(subtrees);
        Node replacementNode = subtrees.get(index);
        Expr clone = (Expr) (replacementNode.clone());
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(clone);
            }
            else
            {
                ((BinaryOp) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Relation)
        {
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(clone);
            }
            else
            {
                ((Relation) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Update)
        {
            if (node == ((Update) parent).getMemType())
            {
                Iterator<Node> iter = subtrees.iterator();
                while (iter.hasNext()) {
                    if (!(iter.next() instanceof Mem)) {
                        iter.remove();
                    }
                }
                index = super.pickElement(subtrees);
                Mem newClone = (Mem) (subtrees.get(index));
                ((Update) parent).changeMemType(newClone);
            }
            else
            {
                ((Update) parent).changeExpr(clone);
            }
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                ((Action) parent).changeExpr(clone);
            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
        }
        else if (parent instanceof Mem)
        {
            ((Mem) parent).changeExpr(clone);
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                ((NearbySensor) parent).changeExpr(clone);
            }
            else if (parent instanceof AheadSensor)
            {
                ((AheadSensor) parent).changeExpr((clone));
            }
            else if (parent instanceof RandomSensor)
            {
                ((RandomSensor) parent).changeExpr(clone);
            }
        }
    }

    @Override
    public void visit(NegativeExpr node)
    {
        List<Node> subtrees = super.findSubtrees(node);
        int index = super.pickElement(subtrees);
        Node replacementNode = subtrees.get(index);
        Expr clone = (Expr) (replacementNode.clone());
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(clone);
            }
            else
            {
                ((BinaryOp) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Relation)
        {
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(clone);
            }
            else
            {
                ((Relation) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Update)
        {
            ((Update) parent).changeExpr(clone);
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                ((Action) parent).changeExpr(clone);
            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
        }
        else if (parent instanceof Mem)
        {
            ((Mem) parent).changeExpr(clone);
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                ((NearbySensor) parent).changeExpr(clone);
            }
            else if (parent instanceof AheadSensor)
            {
                ((AheadSensor) parent).changeExpr((clone));
            }
            else if (parent instanceof RandomSensor)
            {
                ((RandomSensor) parent).changeExpr(clone);
            }
        }
    }

    @Override
    public void visit(NearbySensor node)
    {
        List<Node> subtrees = super.findSubtrees(node);
        int index = super.pickElement(subtrees);
        Node replacementNode = subtrees.get(index);
        Expr clone = (Expr) (replacementNode.clone());
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(clone);
            }
            else
            {
                ((BinaryOp) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Relation)
        {
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(clone);
            }
            else
            {
                ((Relation) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Update)
        {
            ((Update) parent).changeExpr(clone);
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                ((Action) parent).changeExpr(clone);
            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
        }
        else if (parent instanceof Mem)
        {
            ((Mem) parent).changeExpr(clone);
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                ((NearbySensor) parent).changeExpr(clone);
            }
            else if (parent instanceof AheadSensor)
            {
                ((AheadSensor) parent).changeExpr((clone));
            }
            else if (parent instanceof RandomSensor)
            {
                ((RandomSensor) parent).changeExpr(clone);
            }
        }
    }

    @Override
    public void visit(AheadSensor node)
    {
        List<Node> subtrees = super.findSubtrees(node);
        int index = super.pickElement(subtrees);
        Node replacementNode = subtrees.get(index);
        Expr clone = (Expr) (replacementNode.clone());
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(clone);
            }
            else
            {
                ((BinaryOp) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Relation)
        {
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(clone);
            }
            else
            {
                ((Relation) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Update)
        {
            ((Update) parent).changeExpr(clone);
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                ((Action) parent).changeExpr(clone);
            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
        }
        else if (parent instanceof Mem)
        {
            ((Mem) parent).changeExpr(clone);
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                ((NearbySensor) parent).changeExpr(clone);
            }
            else if (parent instanceof AheadSensor)
            {
                ((AheadSensor) parent).changeExpr((clone));
            }
            else if (parent instanceof RandomSensor)
            {
                ((RandomSensor) parent).changeExpr(clone);
            }
        }
    }

    @Override
    public void visit(RandomSensor node)
    {
        List<Node> subtrees = super.findSubtrees(node);
        int index = super.pickElement(subtrees);
        Node replacementNode = subtrees.get(index);
        Expr clone = (Expr) (replacementNode.clone());
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(clone);
            }
            else
            {
                ((BinaryOp) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Relation)
        {
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(clone);
            }
            else
            {
                ((Relation) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Update)
        {
            ((Update) parent).changeExpr(clone);
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                ((Action) parent).changeExpr(clone);
            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
        }
        else if (parent instanceof Mem)
        {
            ((Mem) parent).changeExpr(clone);
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                ((NearbySensor) parent).changeExpr(clone);
            }
            else if (parent instanceof AheadSensor)
            {
                ((AheadSensor) parent).changeExpr((clone));
            }
            else if (parent instanceof RandomSensor)
            {
                ((RandomSensor) parent).changeExpr(clone);
            }
        }
    }

    @Override
    public void visit(SmellSensor node)
    {
        List<Node> subtrees = super.findSubtrees(node);
        int index = super.pickElement(subtrees);
        Node replacementNode = subtrees.get(index);
        Expr clone = (Expr) (replacementNode.clone());
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(clone);
            }
            else
            {
                ((BinaryOp) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Relation)
        {
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(clone);
            }
            else
            {
                ((Relation) parent).changeRight(clone);
            }
        }
        else if (parent instanceof Update)
        {
            ((Update) parent).changeExpr(clone);
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                ((Action) parent).changeExpr(clone);
            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
        }
        else if (parent instanceof Mem)
        {
            ((Mem) parent).changeExpr(clone);
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                ((NearbySensor) parent).changeExpr(clone);
            }
            else if (parent instanceof AheadSensor)
            {
                ((AheadSensor) parent).changeExpr((clone));
            }
            else if (parent instanceof RandomSensor)
            {
                ((RandomSensor) parent).changeExpr(clone);
            }
        }
    }

    @Override
    public void visit(Command node)
    {
        List<Node> subtrees = super.findSubtrees(node);
        int index = super.pickElement(subtrees);
        Node replacementNode = subtrees.get(index);
        Command clone = (Command) (replacementNode.clone());
        Node parent = node.getParent();
        if (parent == null)
        {
            System.out.println("parent is null");
        }
        ((Rule) parent).changeCommand(clone);
    }

    @Override
    public void visit(Update node)
    {
        List<Node> subtrees = super.findSubtrees(node);
        int index = super.pickElement(subtrees);
        Node replacementNode = subtrees.get(index);
        Update clone = (Update) (replacementNode.clone());
        Node parent = node.getParent();
        ((Command) parent).replace(node, clone);
    }

    @Override
    public void visit(Action node)
    {
        List<Node> subtrees = super.findSubtrees(node);
        int index = super.pickElement(subtrees);
        Node replacementNode = subtrees.get(index);
        Action clone = (Action) (replacementNode.clone());
        Node parent = node.getParent();
        ((Command) parent).replace(node, clone);
    }
}
