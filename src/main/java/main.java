import org.gnu.glpk.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*

VM options path:
-Djava.library.path=/usr/lib/x86_64-linux-gnu/jni/

 */

public class main {

    static DecoupeOpti decoupeOpti = new DecoupeOpti();
    static List <Double> dual = new ArrayList<>();

    static glp_prob lp;
    static int  nbCol = 4;
    static int  nbRows = 4;

    public static void main(String[] args) {
        int ret;

        initializeMatrix();

        try{
            while(true) {
                initializeCoef();
                createProb();
                ret = solveProb();
                if (ret != 0)
                    System.out.println("The problem could not be solved");
                makeDual();

                SacADos sac = new SacADos(dual);
                glp_prob lpsac = sac.run();
                GLPK.glp_write_lp(lpsac,null,"sac");

                double res = GLPK.glp_mip_obj_val(lpsac);
                List<Integer> result= new ArrayList<>();
                for(int i=1;i<=dual.size();i++){
                    result.add((int)GLPK.glp_mip_col_val(lpsac,i));
                }

                if (res <= 1) {
                    break;
                } else {
                    nbCol++;
                    decoupeOpti.addXjtoXi(result);
                }
            }
            GLPK.glp_write_sol(lp, "p");
            GLPK.glp_delete_prob(lp);

            } catch (GlpkException ex){
                ex.printStackTrace();
            }
        }

    private static void makeDual() {
        dual = new ArrayList<>();
        for (int i = 1; i <= nbCol; i++) {
            double value=GLPK.glp_get_row_dual(lp, i);
            dual.add(value);
        }
    }

    private static int solveProb() {
        glp_smcp parm;
        parm = new glp_smcp();
        GLPK.glp_init_smcp(parm);
        return GLPK.glp_simplex(lp, parm);
    }

    /**
     *  init la matrice et la liste des coef
     *
     *             - init matrice de base x1 x2 x3 x4
     */
    private static void initializeMatrix() {
        decoupeOpti.initialize();

        ArrayList<Integer> x1 = new ArrayList<Integer>(Arrays.asList(2,0,0,0));
        decoupeOpti.addXjtoXi(x1);

        ArrayList<Integer> x2 = new ArrayList<Integer>(Arrays.asList(0,2,0,0));
        decoupeOpti.addXjtoXi(x2);

        ArrayList<Integer> x3 = new ArrayList<Integer>(Arrays.asList(0,0,3,0));
        decoupeOpti.addXjtoXi(x3);

        ArrayList<Integer> x4 = new ArrayList<Integer>(Arrays.asList(0,0,0,7));
        decoupeOpti.addXjtoXi(x4);

        initializeCoef();
    }

    /**
     * init la liste des coeff : case 0 = x1, case 1 = x2 .....
     */
    public static void initializeCoef(){
        for(int i=0; i<nbCol;i++){
            decoupeOpti.addCoefXjtoListXi(1);
        }
    }

    public static void createProb(){
            lp = GLPK.glp_create_prob();
            System.out.println("Problem created");
            GLPK.glp_set_prob_name(lp, "P");

            //Colonnes
            GLPK.glp_add_cols(lp, nbCol);

            for(int i=1; i <= nbCol; i++)
            {
                GLPK.glp_set_col_name(lp, i, "x" + Integer.toString(i));
                GLPK.glp_set_col_kind(lp, i, GLPKConstants.GLP_CV); //Type de la colonne
                GLPK.glp_set_col_bnds(lp, i, GLPKConstants.GLP_LO,0,0); // Bornes inf et sup
            }

            SWIGTYPE_p_int ind = GLPK.new_intArray(nbCol+1); //stocker les indices
            SWIGTYPE_p_double val = GLPK.new_doubleArray(nbCol + 1); //stocker les valeurs

            for(int k=1; k<=decoupeOpti.matriceXi.size();k++){
                GLPK.intArray_setitem(ind, k, k );
            }

            // Create constraints
            GLPK.glp_add_rows(lp, decoupeOpti.matriceXi.size());
            for(int i = 1; i <= nbRows; i++) {
                GLPK.glp_set_row_name(lp, i, "c"+i);
                double bounds = decoupeOpti.valueConstraint[i-1];
                GLPK.glp_set_row_bnds(lp, i, GLPKConstants.GLP_LO, bounds, 0);

                for (int j = 1; j <= nbCol; j++) {
                    double value = decoupeOpti.matriceXi.get(j-1).get(i-1);
                    GLPK.doubleArray_setitem(val, j,value);
                }
                GLPK.glp_set_mat_row(lp,i,nbCol,ind,val);
            }
            GLPK.glp_set_obj_name(lp, "z");
            GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MIN);

            for(int i = 1; i <= nbCol; i++){
                GLPK.glp_set_obj_coef(lp, i, 1);
            }
            GLPK.delete_intArray(ind);
            GLPK.delete_doubleArray(val);
        }
}
