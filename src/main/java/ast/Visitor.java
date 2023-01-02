package ast;

public interface Visitor
{
    void visit(ProgramImpl node);

    void visit(Rule node);

    void visit(BinaryCondition node);

    void visit (Relation node);

    void visit(BinaryOp node);

    void visit(Number node);

    void visit(Mem node);

    void visit(NegativeExpr node);

    void visit(NearbySensor node);

    void visit(AheadSensor node);

    void visit(RandomSensor node);

    void visit(SmellSensor node);

    void visit(Command node);

    void visit(Update node);

    void visit(Action node);
}
