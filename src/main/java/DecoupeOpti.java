import java.util.ArrayList;
import java.util.List;

public class DecoupeOpti {

    int nbCol = 4;
    int nbRows = 4;

    int valueConstraint[] = { 97, 610, 395, 211 };

     List<List<Integer>> matriceXi = new ArrayList<>();
    private List<Integer> coefXi;

    public DecoupeOpti (List<List<Integer>> matriceXi, List<Integer> coefXi){

        this.matriceXi = matriceXi;
        this.coefXi = coefXi;
    }

    public DecoupeOpti(){

    }

    public void addXjtoXi(List<Integer> xj){
        matriceXi.add(xj);
    }

    public void addCoefXjtocoefXi(int coefXj){
        coefXi.add(coefXj);
    }

}
