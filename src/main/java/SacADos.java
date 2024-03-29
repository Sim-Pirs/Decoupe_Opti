import org.gnu.glpk.*;

import java.util.ArrayList;
import java.util.List;

public class SacADos {

    List<Double> dual;
    List<Double> result;
   // ArrayList<Double> yi;

    double coefConstraint[] = { 45, 36, 31, 14 };

    public SacADos(List <Double> dual){
        this.dual = dual;
        result = new ArrayList<>();
    }

    public glp_prob run (){
        SWIGTYPE_p_int ind;
        glp_iocp parm;
        int ret;

        glp_prob s = GLPK.glp_create_prob();
        GLPK.glp_set_prob_name(s, "S");

        //Colonnes
        GLPK.glp_add_cols(s, dual.size());

        for(int i=1; i <= dual.size(); i++)
        {
            GLPK.glp_set_col_name(s, i, "a" + Integer.toString(i));
            GLPK.glp_set_col_kind(s, i, GLPKConstants.GLP_IV); //Type de la colonne
            GLPK.glp_set_col_bnds(s, i, GLPKConstants.GLP_LO,0, 0); // Bornes inf et sup
        }

        ind = GLPK.new_intArray(dual.size()+1); //stocker les indices
        SWIGTYPE_p_double val = GLPK.new_doubleArray(dual.size() + 1); //stocker les valeurs


        for(int k=1; k<=dual.size();k++){
            GLPK.intArray_setitem(ind, k, k );
        }

        // Create constraints
        GLPK.glp_add_rows(s, 1);

        GLPK.glp_set_row_name(s, 1, "c"+1);
        GLPK.glp_set_row_bnds(s, 1, GLPKConstants.GLP_DB, 0, 100);

        for (int j = 1; j <= coefConstraint.length; j++) {
            double value= coefConstraint[j-1];
            GLPK.doubleArray_setitem(val, j,value);
            GLPK.glp_set_mat_row(s,1,dual.size(),ind,val);
        }

        GLPK.glp_set_obj_name(s, "z'");
        GLPK.glp_set_obj_dir(s, GLPKConstants.GLP_MAX);

        for(int i = 0; i < dual.size(); i++){
            GLPK.glp_set_obj_coef(s, i+1, dual.get(i));
        }

        parm = new glp_iocp();
        GLPK.glp_init_iocp(parm);
        parm.setPresolve(GLPKConstants.GLP_ON);
        ret = GLPK.glp_intopt(s, parm);
        if (ret != 0) {
            System.out.println("The problem could not be solved");
        }

        for(int i = 0; i < result.size(); i++)
            System.out.println(result.get(i));

        if(ret != 0){
            System.out.println("The problem could not be solved yeaaah");
        }

        GLPK.delete_intArray(ind);
        GLPK.delete_doubleArray(val);

        return s;
    }

    public void afficheDual(){
        for (Double aDual : dual) {
            System.out.println(aDual);
        }
    }
}
