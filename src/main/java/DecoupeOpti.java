import java.util.ArrayList;
import java.util.List;

public class DecoupeOpti {

    int nbCol = 0; // au début
    int nbRows = 4;

    int valueConstraint[] = { 97, 610, 395, 211 };

    List<List<Integer>> matriceXi;
    List<Integer> coefXi;

    public DecoupeOpti (List<List<Integer>> matriceXi, List<Integer> coefXi){
        this.matriceXi = matriceXi;
        this.coefXi = coefXi;
    }

    public DecoupeOpti(){
        matriceXi = new ArrayList<List<Integer>>(4);
        coefXi = new ArrayList<>();
    }

    public void addXjtoXi(List<Integer> xj){
        matriceXi.add(xj);
        nbCol++;
    }

    public void addCoefXjtoListXi(int coefXj){
        coefXi.add(coefXj);
    }

}
