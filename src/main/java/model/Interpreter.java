package model;

import ast.*;
import ast.Number;
import parse.TokenType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Interpreter
{
    private World world;
    private Critter critter;
    private int numRulesRun;

    public Interpreter(World world, Critter critter)
    {
        this.world = world;
        this.critter = critter;
        this.numRulesRun = 0;
    }

    public boolean interpret()
    {
        while(numRulesRun < Constants.MAX_RULES_PER_TURN)
        {
            if (interpretProgram(critter.getProgram()) == 0){
                //System.out.println(critter.getLastRuleString());
                critter.setMem(5, critter.getMemValue(5) + 1);
                return true;
            }
            numRulesRun++;
        }

        CritterAction critterAction = new CritterAction(world, critter);
        critterAction.perform(TokenType.WAIT);
        return false;
    }
    public int interpretProgram(Program program)
    {
        Rule lastRule = null;
        for(Node rule : program.getChildren())
        {
            int i = interpretRule(rule);
            if(i == 0)
            {
                lastRule = (Rule) rule;
                critter.setLastRuleString(lastRule.toString());
                return 0;
            }
            else if(i == 1)
            {
                lastRule = (Rule) rule;
                critter.setLastRuleString(lastRule.toString());
            }
            numRulesRun ++;
        }

        if(lastRule == null)
        {
            return -1;
        }
        else return 1;
    }
    public int interpretRule(Node node)
    {
        Rule rule = (Rule) node;
        boolean updated = false;
        if(interpretCondition(rule.getCondition()))
        {
            critter.setLastRuleString(node.toString());
            for(Node child : rule.getCommand().getChildren())
            {
                if(child.getClass() == Update.class)
                {
                    interpretUpdate(child);
                    //System.out.println(critter.getSpecies() + ": " + child.toString());
                    updated = true;
                }
                else if(child.getClass() == Action.class)
                {
                    //System.out.println(critter.getSpecies() + ":" + child.toString());
                    if( !interpretAction(child) ) {
                        //System.out.println("failed action bozo");
                    }
                    return 0;
                }
            }
        }
        if(updated) return 1;
        else return -1;
    }
    public boolean interpretUpdate(Node update)
    {
        Update curr = (Update) update;
        int memType = interpretExpression(curr.getMemType().getExpr());
        int updateValue = interpretExpression(curr.getExpr());

        if(memType < 6 || memType >= critter.getMemValue(0))
        {
            System.out.println("not valid mem value bozo");
            return false;
        }
        critter.setMem(memType, updateValue);


        return true;
    }
    public boolean interpretAction(Node node)
    {
        Action action = (Action) node;
        TokenType tt = action.getName();
        CritterAction critterAction = new CritterAction(world, critter);

        if(tt == TokenType.SERVE){
            return critterAction.serve(interpretExpression(action.getExpr()));
        }
        else{
            return critterAction.perform(tt);
        }
    }
    public boolean interpretCondition(Node condition){

        if(condition.getClass() == Relation.class){
            return interpretRelation(condition);
        }

        BinaryCondition bc = (BinaryCondition) condition;
        boolean left = false;
        boolean right = false;
        BinaryCondition.Operator op = bc.getOp();
        if(bc.getLeft().getClass() == BinaryCondition.class){
            left = interpretCondition(bc.getLeft());
        }
        else if(bc.getLeft().getClass() == Relation.class){
            left = interpretRelation(bc.getLeft());
        }
        if(bc.getRight().getClass() == BinaryCondition.class){
            right = interpretCondition(bc.getRight());
        }
        else if(bc.getRight().getClass() == Relation.class){
            right = interpretRelation(bc.getRight());
        }

        if(op == BinaryCondition.Operator.AND){
            return left && right;
        }
        else return left || right;


    }

    public boolean interpretRelation(Node node){
        Relation relation = (Relation) node;
        int left = interpretExpression(relation.getLeft());
        int right = interpretExpression(relation.getRight());
        String rel = relation.getOperator().toString();

        switch(rel){
            case("<"):
                return (left < right);
            case("<="):
                return (left <= right);
            case("="):
                return (left == right);
            case(">="):
                return (left >= right);
            case(">"):
                return (left > right);
            case("!="):
                return (left != right);
            default:
                System.out.println(rel + " is not a valid relation operator");
                return false;
        }
    }

    public int interpretExpression(Node node)
    {
        Expr expr = (Expr) node;
        if(expr.getClass() == BinaryOp.class)
        {
            BinaryOp binOp = (BinaryOp) expr;
            int left = interpretExpression((binOp.getLeft()));
            int right = interpretExpression(binOp.getRight());
            String op = binOp.getOp().toString();

            switch(op){
                case("+"):
                    return left + right;
                case("-"):
                    return left - right;
                case("*"):
                    return left * right;
                case("/"):
                    return right != 0 ? (left / right) : 0;
                case("mod"):
                    return right != 0 ? (left % right) : 0;
                default:
                    System.out.println(op + " is not a valid binary operator stoopid");
                    return -1;
            }
        }

        else if(expr.getClass() == NegativeExpr.class)
        {
            NegativeExpr negged = (NegativeExpr) expr;
            return -1 * interpretExpression(negged.getRight());
        }

        else if(expr.getClass() == Number.class)
        {
            Number num = (Number) expr;
            return num.getNum();
        }

        else if(expr.getClass() == Mem.class)
        {
            Mem mem = (Mem) expr;
            return critter.getMemValue(interpretExpression(mem.getExpr()));
        }

        else if(expr.getClass() == NearbySensor.class)
        {
            NearbySensor ns = (NearbySensor) expr;
            return interpretNearbySensor(interpretExpression(ns.getExpr()));
        }

        else if(expr.getClass() == AheadSensor.class)
        {
            AheadSensor as = (AheadSensor) expr;
            return interpretAheadSensor(interpretExpression(as.getExpr()));
        }

        else if(expr.getClass() == SmellSensor.class)
        {
            return interpretSmellSensor();
        }

        else if(expr.getClass() == RandomSensor.class)
        {
            RandomSensor rs = (RandomSensor) expr;
            return interpretExpression(rs.getExpr());
        }

        return 0;
    }

    public int interpretNearbySensor(int dir)
    {
        Tile[][] tiles = world.getTiles();
//        System.out.print(dir + " ");
        dir = (dir + critter.getDirection()) % 6;
//        System.out.println(critter.getDirection() + " " + dir);
        int row = critter.getRow();
        int column = critter.getColumn();
        int info;
        if (dir == 0)
        {
            row -= 2;
            info = world.getTerrainInfo(column, tiles.length - 1 - row);
        }
        else if (dir == 1)
        {
            row--;
            column++;
            info = world.getTerrainInfo(column, tiles.length - 1 - row);
        }
        else if (dir == 2)
        {
            row++;
            column++;
            info = world.getTerrainInfo(column, tiles.length - 1 - row);
        }
        else if (dir == 3)
        {
            row += 2;
            info = world.getTerrainInfo(column, tiles.length - 1 - row);
        }
        else if (dir == 4)
        {
            row++;
            column--;
            info = world.getTerrainInfo(column, tiles.length - 1 - row);
        }
        else
        {
            row--;
            column--;
            info = world.getTerrainInfo(column, tiles.length - 1 - row);
        }

        if (info <= 0)
        {
            return info;
        }
        else
        {
            Critter target = tiles[row][column].getCritter();
            return target.getMemValue(3) * 1000 + target.getMemValue(6) * 10 + target.getDirection();
        }
    }

    public int interpretAheadSensor(int dist)
    {
        Tile[][] tiles = world.getTiles();
        dist = Math.max(dist, 0);
        int dir = critter.getDirection();
        int row = critter.getRow();
        int column = critter.getColumn();
        int info;
        if (dir == 0)
        {
            row -= dist * 2;
            info = world.getTerrainInfo(column, tiles.length - 1 - row);
        }
        else if (dir == 1)
        {
            row -= dist;
            column += dist;
            info = world.getTerrainInfo(column, tiles.length - 1 - row);
        }
        else if (dir == 2)
        {
            row += dist;
            column += dist;
            info = world.getTerrainInfo(column, tiles.length - 1 - row);
        }
        else if (dir == 3)
        {
            row += dist * 2;
            info = world.getTerrainInfo(column, tiles.length - 1 - row);
        }
        else if (dir == 4)
        {
            row += dist;
            column -= dist;
            info = world.getTerrainInfo(column, tiles.length - 1 - row);
        }
        else
        {
            row -= dist;
            column -= dist;
            info = world.getTerrainInfo(column, tiles.length - 1 - row);
        }

        if (info <= 0)
        {
            return info;
        }
        else
        {
            Critter target = tiles[row][column].getCritter();
            return target.getMemValue(3) * 1000 + target.getMemValue(6) * 10 + target.getDirection();
        }
    }

    public int interpretSmellSensor()
    {
        class Node implements Comparable<Node>
        {

            // row and column are based off the actual coordinates now array coordinates
            int row, column, distance, weight;
            ArrayList<Integer> previousDirections = new ArrayList<>();
            ArrayList<Node> previousTiles = new ArrayList<>();
            public Node(int row, int column)
            {
                this.row = row;
                this.column = column;
            }

            public int getDirection()
            {
                return previousDirections.get(previousDirections.size() - 1);
            }

            public int determineDirection(Node from, Node to)
            {
                int direction;
                if(from.row - to.row > 0){
                    if(from.column - to.column == 1) direction = 1;
                    else if(from.column - to.column == -1) direction = 5;
                    else direction = 0;
                }

                else {
                    if(from.column - to.column == 1) direction = 2;
                    else if(from.column - to.column == -1) direction = 4;
                    else direction = 3;
                }
                return direction;
            }

            @Override
            public int compareTo(Node node) {
                return (this.distance - node.distance);
            }
        }

        ArrayList<Node> nodes = new ArrayList<>();
        PriorityQueue<Node> frontier = new PriorityQueue(); // queue for dijkstras

        Queue<int[]> queue = new LinkedList<>(); //bfs
        queue.add(new int[]{critter.getRow(), critter.getColumn()});
        while (!queue.isEmpty()) {
            // get next tile in queue and its coordinates
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];
            if(world.getTerrainInfo(c, world.getTiles().length - 1 - r) == -1) continue;

            // if tile is out of bounds, continue to next tile in queue
            if( Math.max(Math.abs((c - critter.getColumn() - r + critter.getRow()) / 2),
                    Math.max(Math.abs(c - critter.getColumn()),
                            Math.abs((c - critter.getColumn() + r - critter.getRow()) / 2))) > 10
            ){
                continue;
            }

            // check if its already in the arraylist of nodes
            boolean exists = false;
            for(Node node : nodes){
                if(r == node.row && c == node.column){
                    exists = true;
                    break;
                }
            }
            if(exists) continue;
                // add it to arraylist of nodes if it doesn't exist
            else{
                Node node = new Node(r, c);
                node.distance = (Integer.MAX_VALUE);
                nodes.add(node);

            }

            //add the surrounding nodes
            queue.add(new int[]{r + 2, c});
            queue.add(new int[]{r + 1, c + 1});
            queue.add(new int[]{r - 1, c + 1});
            queue.add(new int[]{r - 2, c});
            queue.add(new int[]{r - 1, c - 1});
            queue.add(new int[]{r + 1, c - 1});

        }

        Node root = null;
        for(Node node : nodes){
            if(critter.getRow() == node.row && critter.getColumn() == node.column){
                root = node;
                break;
            }
        }
        // assert root != null;
        root.distance = 0;
        root.previousDirections.add(critter.getDirection());
        frontier.add(root);

        ArrayList<Node> finished = new ArrayList<>();
        int length = world.getTiles().length;

        while(frontier.size() != 0){
            Node g = frontier.poll();

            for(int i=0; i<6; i++){
                if(i == 0){
                    if(world.getTerrainInfo(g.column, length - 1 - (g.row - 2)) < -1 || world.getTerrainInfo(g.column, length - 1 - (g.row - 2)) == 0){
                        //find node of target tile
                        Node current = null;
                        for(Node node : nodes){
                            if(g.row - 2 == node.row && g.column == node.column){
                                current = node;
                                break;
                            }
                        }

                        if(current == null) break;
                        if(finished.contains(current)) continue;
                        // determine weight to get to target
                        current.weight = (i - g.getDirection()) % 6;
                        if(current.weight < 0) current.weight = 6 + current.weight;
                        if(current.weight > 3) current.weight = 6 - current.weight;
                        current.weight ++;
                        // if the distance from root through g to target is less than the distance stored within the target, update its info
                        if( current.weight + g.distance < current.distance){
                            current.distance = current.weight + g.distance;
                            current.previousTiles = (ArrayList<Node>) g.previousTiles.clone();
                            current.previousTiles.add(g);
                            current.previousDirections = (ArrayList<Integer>) g.previousDirections.clone();
                            current.previousDirections.add(0);

                            frontier.add(current);
                        }
                    }

                }

                else if(i == 1){
                    if(world.getTerrainInfo(g.column + 1, length - 1 -  (g.row - 1)) < -1 || world.getTerrainInfo(g.column + 1, length - 1 - (g.row - 1)) == 0){
                        Node current = null;
                        for(Node node : nodes){
                            if(g.row - 1 == node.row && g.column + 1 == node.column){
                                current = node;
                                break;
                            }
                        }
                        if(current == null) break;
                        if(finished.contains(current)) continue;
                        current.weight = (i - g.getDirection()) % 6;
                        if(current.weight < 0) current.weight = 6 + current.weight;
                        if(current.weight > 3) current.weight = 6 - current.weight;
                        current.weight++;
                        if( current.weight + g.distance < current.distance){
                            current.distance = current.weight + g.distance;
                            current.previousTiles = (ArrayList<Node>) g.previousTiles.clone();
                            current.previousTiles.add(g);
                            current.previousDirections = (ArrayList<Integer>) g.previousDirections.clone();
                            current.previousDirections.add(1);
                            frontier.add(current);
                        }
                    }
                }

                else if(i == 2){
                    if(world.getTerrainInfo(g.column + 1, length - 1 -  (g.row + 1)) < -1 || world.getTerrainInfo(g.column + 1, length - 1 - (g.row + 1)) == 0){
                        Node current = null;
                        for(Node node : nodes){
                            if(g.row + 1 == node.row && g.column + 1 == node.column){
                                current = node;
                                break;
                            }
                        }
                        if(current == null) break;
                        if(finished.contains(current)) continue;
                        current.weight = (i - g.getDirection()) % 6;
                        if(current.weight < 0) current.weight = 6 + current.weight;
                        if(current.weight > 3) current.weight = 6 - current.weight;
                        current.weight ++;
                        if( current.weight + g.distance < current.distance){
                            current.distance = current.weight + g.distance;
                            current.previousTiles = (ArrayList<Node>) g.previousTiles.clone();
                            current.previousTiles.add(g);
                            current.previousDirections = (ArrayList<Integer>) g.previousDirections.clone();
                            current.previousDirections.add(2);
                            frontier.add(current);
                        }
                    }
                }

                else if(i == 3) {
                    if (world.getTerrainInfo(g.column, length - 1 - (g.row + 2)) < -1 || world.getTerrainInfo(g.column, length - 1 - (g.row + 2)) == 0) {
                        Node current = null;
                        for (Node node : nodes) {
                            if (g.row + 2 == node.row && g.column == node.column) {
                                current = node;
                                break;
                            }
                        }
                        if(current == null) break;
                        if(finished.contains(current)) continue;
                        current.weight = (i - g.getDirection()) % 6;
                        if(current.weight < 0) current.weight = 6 + current.weight;
                        if(current.weight > 3) current.weight = 6 - current.weight;
                        current.weight ++;
                        if (current.weight + g.distance < current.distance) {
                            current.distance = current.weight + g.distance;
                            current.previousTiles = (ArrayList<Node>) g.previousTiles.clone();
                            current.previousTiles.add(g);
                            current.previousDirections = (ArrayList<Integer>) g.previousDirections.clone();
                            current.previousDirections.add(3);
                            frontier.add(current);
                        }
                    }
                }

                else if(i == 4){
                    if(world.getTerrainInfo(g.column - 1, length - 1 - (g.row + 1)) < -1 || world.getTerrainInfo(g.column - 1, length - 1 - (g.row + 1)) == 0){
                        Node current = null;
                        for(Node node : nodes){
                            if(g.row + 1 == node.row && g.column - 1 == node.column){
                                current = node;
                                break;
                            }
                        }
                        if(current == null) break;
                        if(finished.contains(current)) continue;
                        current.weight = (i - g.getDirection()) % 6;
                        if(current.weight < 0) current.weight = 6 + current.weight;
                        if(current.weight > 3) current.weight = 6 - current.weight;
                        current.weight ++;
                        if( current.weight + g.distance < current.distance){
                            current.distance = current.weight + g.distance;
                            current.previousTiles = (ArrayList<Node>) g.previousTiles.clone();
                            current.previousTiles.add(g);
                            current.previousDirections = (ArrayList<Integer>) g.previousDirections.clone();
                            current.previousDirections.add(4);
                            frontier.add(current);
                        }
                    }
                }

                else if(i == 5){
                    if(world.getTerrainInfo(g.column - 1, g.row - 1) < -1 || world.getTerrainInfo(g.column - 1, g.row - 1) == 0){
                        Node current = null;
                        for(Node node : nodes){
                            if(g.row - 1 == node.row && g.column - 1 == node.column){
                                current = node;
                                break;
                            }
                        }
                        if(current == null) break;
                        if(finished.contains(current)) continue;
                        current.weight = (i - g.getDirection()) % 6;
                        if(current.weight < 0) current.weight = 6 + current.weight;
                        if(current.weight > 3) current.weight = 6 - current.weight;
                        current.weight ++;
                        if( current.weight + g.distance < current.distance){
                            current.distance = current.weight + g.distance;
                            current.previousTiles = (ArrayList<Node>) g.previousTiles.clone();
                            current.previousTiles.add(g);
                            current.previousDirections = (ArrayList<Integer>) g.previousDirections.clone();
                            current.previousDirections.add(5);
                            frontier.add(current);
                        }
                    }
                }

            }

            finished.add(g);

        }

        Node food = new Node(-1, -1);
        food.distance = Integer.MAX_VALUE;
        for(Node node : finished){
            int r = node.row;
            int c = node.column;
            if(world.getTerrainInfo(c, world.getTiles().length - 1 - r) < -1 && node.distance < food.distance) {
                food = node;
            }
        }

        if(food.row == -1 || food.distance - 1 > 10) {
            return 1000000;
        }
        else {
            int relativeDirection = food.previousDirections.get(1) - food.previousDirections.get(0);
            if(relativeDirection < 0) relativeDirection += 6;
            relativeDirection %= 6;
            //subtract one because the first move takes one less timestep than normal
            return ((food.distance - 1) * 100 + relativeDirection);
        }
    }

    public int interpretRandomSensor(RandomSensor rs, int n)
    {
        return n < 2 ? 0 : (int) (Math.random() * n);
    }


    public void setMem(int memIndex, int changeNumber)
    {
        critter.setMem(memIndex, changeNumber);
    }
}
