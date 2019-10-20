import java.util.ArrayList;
import java.util.List;

public class DecoupeOpti {

    int nbCol = 0; // au début
    int nbRows = 4;

    int valueConstraint[] = { 97, 610, 395, 211 };

    private List<List<Integer>> matriceXi = new ArrayList<>();
     List<Integer> coefXi = new ArrayList<>();

    public DecoupeOpti (List<List<Integer>> matriceXi, List<Integer> coefXi){

        this.matriceXi = matriceXi;
        this.coefXi = coefXi;
    }

    public DecoupeOpti(){

    }

    public void addXjtoXi(List<Integer> xj){
        matriceXi.add(xj);
        nbCol++;
    }

    public void addCoefXjtoListXi(int coefXj){
        coefXi.add(coefXj);
    }

}
