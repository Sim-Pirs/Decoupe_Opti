import java.util.ArrayList;
import java.util.List;

public class DecoupeOpti {

    double valueConstraint[] = { 97, 610, 395, 211 };

    List<List<Integer>> matriceXi;
    List<Integer> coefXi;

    public DecoupeOpti(){
        matriceXi = new ArrayList<>(4);
        coefXi = new ArrayList<>();
    }

    public void addXjtoXi(List<Integer> xj){
        matriceXi.add(xj);
    }

    public void addCoefXjtoListXi(int coefXj){
        coefXi.add(coefXj);
    }

    public void initialize() {
        matriceXi = new ArrayList<>();
        coefXi = new ArrayList<>();
    }
}
