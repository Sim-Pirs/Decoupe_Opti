import org.gnu.glpk.*;

import java.util.ArrayList;
import java.util.List;

public class SacADos {

    List<Double> dual;
   // ArrayList<Double> yi;

    int coefConstraint[] = { 45, 36, 31, 14 };

    public SacADos(List <Double> dual){
        //yi = new ArrayList<>();
        this.dual = dual;
    }

    public double run (){
        double resultat = 0;
        SWIGTYPE_p_int ind;

        glp_prob s = GLPK.glp_create_prob();
        GLPK.glp_set_prob_name(s, "S");

        //Colonnes
        GLPK.glp_add_cols(s, dual.size());

        for(int i=1; i <= dual.size(); i++)
        {
            GLPK.glp_set_col_name(s, 1, "a" + Integer.toString(i));
            GLPK.glp_set_col_kind(s, 1, GLPKConstants.GLP_CV); //Type de la colonne
            GLPK.glp_set_col_bnds(s, 1, GLPKConstants.GLP_DB,0,0); // Bornes inf et sup
        }

        ind = GLPK.new_intArray(dual.size()+1); //stocker les indices
        for(int k=0; k<dual.size();k++){
            GLPK.intArray_setitem(ind, k, k );
        }
        SWIGTYPE_p_double val = GLPK.new_doubleArray(dual.size() + 1); //stocker les valeurs

        // Create constraints
        GLPK.glp_add_rows(s, 1);

        GLPK.glp_set_row_name(s, 1, "c"+1);
        GLPK.glp_set_row_bnds(s, 1, GLPKConstants.GLP_LO, 100, 0);

        for (int j = 1; j <= dual.size(); j++) {
                GLPK.doubleArray_setitem(val, j, dual.get(j-1));
        }

        GLPK.glp_set_mat_row(s,1,dual.size()-1,ind,val);
        GLPK.glp_set_obj_name(s, "z'");
        GLPK.glp_set_obj_dir(s, GLPKConstants.GLP_MAX);

        List <Double> result = new ArrayList<>();

        for(int i=1; i<=dual.size();i++){
            result.add(GLPK.glp_get_row_prim(s,i));
        }



        return resultat;
    }
}
