import org.gnu.glpk.GLPK;
import org.gnu.glpk.glp_prob;

import java.util.ArrayList;
import java.util.List;

public class SacADos {

    List<Double> dual;
    ArrayList<Double> yi;

    int coefConstraint[] = { 45, 36, 31, 14 };

    public SacADos(List <Double> dual){
        yi = new ArrayList<>();
        this.dual = dual;
    }

    public double run (){
        double resultat = 0;

        glp_prob du = GLPK.glp_create_prob();



        return resultat;
    }
}
