package model;

import ast.*;
import parse.TokenType;

public class CritterAction
{
    private World world;
    private Critter critter;

    public CritterAction (World world, Critter critter)
    {
        this.world = world;
        this.critter = critter;
    }

    public boolean perform(TokenType action)
    {
        String name = action.toString();
        switch(name)
        {
            case ("wait"):
                return waitTurn();
            case ("forward"):
                return move(true);
            case ("back"):
                return move(false);
            case ("left"):
                return turn(true);
            case ("right"):
                return turn(false);
            case ("eat"):
                return eat();
            case ("attack"):
                return attack();
            case ("grow"):
                return grow();
            case ("bud"):
                return bud();
            case ("mate"):
                return mate();
            default:
                return false;
        }
    }

    public boolean waitTurn()
    {
        critter.setMem(4, critter.getMemValue(4 ) + critter.getMemValue(3) * Constants.SOLAR_FLUX);
        return true;
    }

    //direction here refers to forwards (true) or backwards (false)
    public boolean move(boolean direction)
    {
        Tile[][] tiles = world.getTiles();
        int r = critter.getRow();
        int c = critter.getColumn();

        int energySpent = critter.getMemValue(3) * Constants.MOVE_COST;


        if(critter.getMemValue(4) < energySpent)
        {
            world.deadCritter(critter);
            return false;
        }

        if(direction)
        {
            switch(critter.getDirection())
            {
                case 0:
                    c = c;
                    r = r - 2;
                    break;
                case 1:
                    c = c + 1;
                    r = r - 1;
                    break;
                case 2:
                    c = c + 1;
                    r = r + 1;
                    break;
                case 3:
                    c = c;
                    r = r + 2;
                    break;
                case 4:
                    c = c - 1;
                    r = r + 1;
                    break;
                case 5:
                    c = c - 1;
                    r = r - 1;
                    break;
            }
        }

        else
        {
            switch(critter.getDirection())
            {
                case 0:
                    c = c;
                    r = r + 2;
                    break;
                case 1:
                    c = c - 1;
                    r = r + 1;
                    break;
                case 2:
                    c = c - 1;
                    r = r - 1;
                    break;
                case 3:
                    c = c;
                    r = r - 2;
                    break;
                case 4:
                    c = c + 1;
                    r = r - 1;
                    break;
                case 5:
                    c = c + 1;
                    r = r + 1;
                    break;
            }
        }
        if(c < 0 || r < 0 || c >= tiles[0].length || r >= tiles.length)
        {
            critter.setMem(4, critter.getMemValue(4) - energySpent);
            if (critter.getMemValue(4) < energySpent) world.deadCritter(critter);
            return false;
        }

        if(world.getTerrainInfo(c, tiles.length - 1 - r) != 0)
        {
            critter.setMem(4, critter.getMemValue(4) - energySpent);
            if (critter.getMemValue(4) < energySpent) world.deadCritter(critter);
            return false;
        }

        world.setCritterPosition(critter, r, c);
        critter.setMem(4, critter.getMemValue(4) - energySpent);
        if(critter.getMemValue(4) < energySpent) world.deadCritter(critter);

        return true;
    }

    // true is left, false is right
    public boolean turn(boolean orientation)
    {
        int newDir = critter.getDirection();

        if(critter.getMemValue(4) < critter.getMemValue(3)){

            world.deadCritter(critter);
            return false;
        }

        else if(critter.getMemValue(4) == critter.getMemValue(3)){

            if(orientation){
                newDir += 5;
            }
            else{
                newDir += 1;
            }
            newDir %= 6;
            critter.setDirection(newDir);
            world.deadCritter(critter);
            return true;
        }

        else if(critter.getMemValue(4) > critter.getMemValue(3)){

            if(orientation){
                newDir += 5;
                newDir %= 6;
                critter.setDirection(newDir);
            }
            else{
                newDir += 1;
                newDir %= 6;
                critter.setDirection(newDir);
            }

            critter.setMem(4, critter.getMemValue(4) - critter.getMemValue(3));

            return true;
        }

        return false;
    }

    public boolean eat()
    {
        int energyBefore = critter.getMemValue(4);
        int cost = critter.getMemValue(3);

        if(critter.getMemValue(4) <= cost){
            world.deadCritter(critter);
            return false;
        }

        else critter.setMem(4, energyBefore - cost);

        if(energyBefore >= critter.energyCapacity()) return false;


        Tile[][] tiles = world.getTiles();
        int r = critter.getRow();
        int c = critter.getColumn();
        switch(critter.getDirection()){
            case 0:
                c = c;
                r = r - 2;
                break;
            case 1:
                c = c + 1;
                r = r - 1;
                break;
            case 2:
                c = c + 1;
                r = r + 1;
                break;
            case 3:
                c = c;
                r = r + 2;
                break;
            case 4:
                c = c - 1;
                r = r + 1;
                break;
            case 5:
                c = c - 1;
                r = r - 1;
                break;
        }

        if(c < 0 || r < 0 || c >= tiles[0].length || r >= tiles.length
                && (tiles[tiles.length - 1 - r][c] != null && !tiles[tiles.length - 1 - r][c].getIsFood())) return false;


        int food;
        if(tiles[r][c] == null) food = 0;
        else food = tiles[r][c].getNumFood();

        if(energyBefore + food <= critter.energyCapacity()){
            critter.setMem(4, energyBefore + food);
            tiles[r][c] = null;
            return true;
        }

        else if(energyBefore + food > critter.energyCapacity()){

            critter.setMem(4, critter.energyCapacity());
            tiles[r][c] = new Tile(food - (critter.energyCapacity()) - energyBefore);
            return true;
        }

        return false;
    }
    public boolean attack (){

        int currEnergy = critter.getMemValue(4);
        int energyUsed = critter.getMemValue(3) * Constants.ATTACK_COST;

        critter.setMem(4, currEnergy - energyUsed);

        if(critter.getMemValue(4) < 0){
            world.deadCritter(critter);
        }

        Tile[][] tiles = world.getTiles();
        int r = tiles.length - 1 - critter.getRow();
        int c = critter.getColumn();

        int direction = critter.getDirection();
        switch(direction){
            case 0:
                c = c;
                r = r + 2;
                break;
            case 1:
                c = c + 1;
                r = r + 1;
                break;
            case 2:
                c = c + 1;
                r = r - 1;
                break;
            case 3:
                c = c;
                r = r - 2;
                break;
            case 4:
                c = c - 1;
                r = r - 1;
                break;
            case 5:
                c = c - 1;
                r = r + 1;
                break;
        }

        if(c < 0 || r < 0 || c >= tiles[0].length || r >= tiles.length || tiles[tiles.length - 1 - r][c] == null || !tiles[tiles.length - 1 - r][c].getIsCritter()){
            if(currEnergy == 0) world.deadCritter(critter);
            return false;
        }

        Critter target = tiles[tiles.length - 1 - r][c].getCritter();
        int size1 = critter.getMemValue(3);
        int size2 = target.getMemValue(3);

        int offensive = critter.getMemValue(2);
        int defensive = critter.getMemValue(1);

        double x = Constants.DAMAGE_INC * (size1 * offensive - size2 * defensive);
        double Px = 1 / (1 + Math.exp(x));
        int damageDone = (int) (Math.round( Constants.BASE_DAMAGE * size1 * Px));

        int targetNewEnergy = target.getMemValue(4) - damageDone;
        if(targetNewEnergy < 0) world.deadCritter(target);
        else target.setMem(4, targetNewEnergy);

        if(currEnergy == energyUsed){
            world.deadCritter(critter);
        }

        return true;

    }
    public boolean grow (){
        int cost = critter.getMemValue(3) * critter.complexity() * Constants.GROW_COST;
        int currEnergy = critter.getMemValue(4);
        if(currEnergy < cost){
            world.deadCritter(critter);
            return false;
        }

        critter.setMem(4, currEnergy - cost);
        critter.setMem(3, critter.getMemValue(3) + 1);
        return true;
    }
    public boolean bud()
    {
        int newEnergy = critter.getMemValue(4) - critter.complexity() * Constants.BUD_COST;
        if(newEnergy < 0){
            world.deadCritter(critter);
            return false;
        }

        critter.setMem(4, newEnergy);

        Tile[][] tiles = world.getTiles();
        int r = critter.getRow();
        int c = critter.getColumn();
        int direction = critter.getDirection();
        switch(direction){
            case 0:
                c = c;
                r = r + 2;
                break;
            case 1:
                c = c - 1;
                r = r + 1;
                break;
            case 2:
                c = c - 1;
                r = r - 1;
                break;
            case 3:
                c = c;
                r = r - 2;
                break;
            case 4:
                c = c + 1;
                r = r - 1;
                break;
            case 5:
                c = c + 1;
                r = r + 1;
                break;
        }

        if(world.getTerrainInfo(c, tiles.length - 1 - r) != 0)
        {
            if(newEnergy == 0) world.deadCritter(critter);
            return false;
        }

        Program clonedAST = (Program) critter.getProgram().clone();

        while(Math.random() < (0.25))
        {
            int selecter = (int) (Math.random() * clonedAST.size());
            Node mutatedNode = clonedAST.nodeAt(selecter);

            int mutation = (int) (Math.random() * 6);

            switch(mutation){
                case 0:
                    Mutation remove = MutationFactory.getRemove();
                    remove.apply(clonedAST, mutatedNode);
                case 1:
                    Mutation swap = MutationFactory.getSwap();
                    swap.apply(clonedAST, mutatedNode);
                case 2:
                    Mutation replace = MutationFactory.getReplace();
                    replace.apply(clonedAST, mutatedNode);
                case 3:
                    Mutation transform = MutationFactory.getTransform();
                    transform.apply(clonedAST, mutatedNode);
                case 4:
                    Mutation insert = MutationFactory.getInsert();
                    insert.apply(clonedAST, mutatedNode);
                case 5:
                    Mutation duplicate = MutationFactory.getDuplicate();
                    duplicate.apply(clonedAST, mutatedNode);
            }
        }

        int[] mem = new int[critter.getMemory().length];
        for(int i=0; i< mem.length; i++){
            if(i==3) mem[3] = 1;
            else if( i == 4) mem[4] = Constants.INITIAL_ENERGY;
            else if(i == 6) mem[6] = 0;
            else if(i >= 7) mem[i] = 0;
            else {
                mem[i] = critter.getMemValue(i);
            }
        }

        world.addCritter(critter.getSpecies(), mem, clonedAST,tiles.length - 1 - r, c, critter.getDirection() );

        if(newEnergy == 0) world.deadCritter(critter);
        return true;

    }
    public boolean mate()
    {
        int cost = critter.complexity() * Constants.MATE_COST;
        int failedCost = critter.getMemValue(3);
        int newEnergy = critter.getMemValue(4) - cost;

        Tile[][] tiles = world.getTiles();
        int r = tiles.length - 1 - critter.getRow();
        int c = critter.getColumn();
        int direction = critter.getDirection();
        switch(direction){
            case 0:
                c = c;
                r = r + 2;
                break;
            case 1:
                c = c + 1;
                r = r + 1;
                break;
            case 2:
                c = c + 1;
                r = r - 1;
                break;
            case 3:
                c = c;
                r = r - 2;
                break;
            case 4:
                c = c - 1;
                r = r - 1;
                break;
            case 5:
                c = c - 1;
                r = r + 1;
                break;
        }

        if(world.getTerrainInfo(c, r) < 1){
            critter.setMem(4, critter.getMemValue(4) - failedCost);
            if(critter.getMemValue(4) <= 0) {
                world.deadCritter(critter);
            }
            return false;
        }

        Critter mate = tiles[tiles.length - 1 - r][c].getCritter();

        boolean behindMate = true;

        int r2 = r;
        int c2 = c;

        direction = mate.getDirection();
        switch(direction)
        {
            case 0:
                c2 = c2;
                r2 = r2 - 2;
                break;
            case 1:
                c2 = c2 - 1;
                r2 = r2 - 1;
                break;
            case 2:
                c2 = c2 - 1;
                r2 = r2 + 1;
                break;
            case 3:
                c2 = c2;
                r2 = r2 + 2;
                break;
            case 4:
                c2 = c2 + 1;
                r2 = r2 + 1;
                break;
            case 5:
                c2 = c2 + 1;
                r2 = r2 - 1;
                break;
        }

        if(world.getTerrainInfo(c2, r2) != 0){
            behindMate = false;
        }

        boolean behindSelf = true;

        r = tiles.length - 1 - critter.getRow();
        c = critter.getColumn();
        switch( critter.getDirection()){
            case 0:
                c = c;
                r = r - 2;
                break;
            case 1:
                c = c - 1;
                r = r - 1;
                break;
            case 2:
                c = c - 1;
                r = r + 1;
                break;
            case 3:
                c = c;
                r = r + 2;
                break;
            case 4:
                c = c + 1;
                r = r + 1;
                break;
            case 5:
                c = c + 1;
                r = r - 1;
                break;
        }

        if(world.getTerrainInfo(c, r) != 0){
            behindSelf = false;
        }

        if(!behindMate && !behindSelf) {
            critter.setMem(4, critter.getMemValue(4) - failedCost);
            if(critter.getMemValue(4) <= 0) {
                world.deadCritter(critter);
            }
            return false;
        }

        if(!mate.isMating())
        {
            critter.setMating(true);
            return false;
        }

        else
        {
            int matedRow;
            int matedColumn;
            int matedDirection;
            if(behindMate && behindSelf){
                if(Math.random() > 0.5) behindMate = false;
                else behindSelf = false;
            }
            if(!behindMate && behindSelf){
                matedRow = r;
                matedColumn = c;
                matedDirection = critter.getDirection();
            }
            else{
                matedRow = r2;
                matedColumn = c2;
                matedDirection = mate.getDirection();
            }

            String newSpeciesName;
            if(Math.random() > 0.5) newSpeciesName = critter.getSpecies();
            else newSpeciesName = mate.getSpecies();

            int size;
            if(Math.random() < 0.5){
                size = critter.getMemValue(0);
            }
            else{
                size = mate.getMemValue(0);
            }
            int[] mem = new int[size];
            for(int i=0; i< mem.length; i++){
                if(i==3) mem[3] = 1;
                else if( i == 4) mem[4] = Constants.INITIAL_ENERGY;
                else if(i == 6) mem[6] = 0;
                else if(i >= 7) mem[i] = 0;
                else {
                    if(Math.random() > 0.5) mem[i] = critter.getMemValue(i);
                    else mem[i] = mate.getMemValue(i);
                }
            }

            world.addCritter(newSpeciesName, mem, matedCritter(critter, mate), matedRow, matedColumn, matedDirection);

            critter.setMating(false);
            mate.setMating(false);

            critter.setMem(4, newEnergy);
            mate.setMem(4, mate.getMemValue(4) - mate.complexity() * Constants.MATE_COST);
        }


        if(critter.getMemValue(4) <= 0) world.deadCritter(critter);
        if(mate.getMemValue(4) <= 0) world.deadCritter(mate);

        return true;

    }

    public Program matedCritter(Critter mate1, Critter mate2){

        double pickGenomeSize = Math.random();

        Program mate1AST = mate1.getProgram();
        Program mate2AST = mate2.getProgram();

        Program newProgram = new ProgramImpl();
        int size;
        if(pickGenomeSize < 0.5){
            size = mate1AST.getChildren().size();
        }
        else{
            size = mate2AST.getChildren().size();
        }

        for(int i=0; i<size; i++){
            if(i > mate1AST.size()) newProgram.getChildren().add(mate2AST.getChildren().get(i).clone());
            else if(i > mate2AST.size()) newProgram.getChildren().add(mate1AST.getChildren().get(i).clone());
            else {
                if(Math.random() > 0.5) newProgram.getChildren().add(mate1AST.getChildren().get(i).clone());
                else newProgram.getChildren().add(mate2AST.getChildren().get(i).clone());
            }
        }

        return newProgram;
    }


    public boolean serve (int value)
    {
        int energySpent = critter.getMemValue(3) + value;

        if(energySpent > critter.getMemValue(4)) {
            world.deadCritter(critter);
            return false;
        }

        critter.setMem(4, critter.getMemValue(4) - energySpent);

        Tile[][] tiles = world.getTiles();
        int r = critter.getRow();
        int c = critter.getColumn();
        switch(critter.getDirection()){
            case 0:
                c = c;
                r = r - 2;
                break;
            case 1:
                c = c + 1;
                r = r - 1;
                break;
            case 2:
                c = c + 1;
                r = r + 1;
                break;
            case 3:
                c = c;
                r = r + 2;
                break;
            case 4:
                c = c - 1;
                r = r + 1;
                break;
            case 5:
                c = c - 1;
                r = r - 1;
                break;
        }

        if(c < 0 || r < 0 || c >= tiles[0].length || r >= tiles.length && (tiles[tiles.length - 1 - r][c] != null
                && tiles[tiles.length - 1 - r][c].getIsRock() || tiles[tiles.length - 1 - r][c].getIsCritter())){
            if(critter.getMemValue(4) == 0) world.deadCritter(critter);
            return false;
        }

        if(tiles[r][c] == null) world.addFood(tiles.length - 1 - r, c, value);
        else tiles[r][c] = new Tile(tiles[r][c].getNumFood() + value);

        if(critter.getMemValue(4) == 0){
            world.deadCritter(critter);
        }


        return true;
    }


}
