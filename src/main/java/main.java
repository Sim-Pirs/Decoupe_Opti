import org.gnu.glpk.*;

import java.util.ArrayList;
import java.util.List;

/*

VM options path:
-Djava.library.path=/usr/lib/x86_64-linux-gnu/jni/

 */

public class main {
        public static void main(String[] args) {
            System.out.println( GLPK.glp_version());
            glp_prob lp;
            //glp_smcp parm;
            SWIGTYPE_p_int ind;

            int nbCol = 4;
            int nbRows = 4;

            /* init la matrice et la liste des coef

            - init matrice de base x1 x2 x3 x4 */
            DecoupeOpti decoupeOpti = new DecoupeOpti();

            List<Integer> x1 = new ArrayList<>();
            x1.add(2);
            x1.add(0);
            x1.add(0);
            x1.add(0);
            decoupeOpti.addXjtoXi(x1);

            List<Integer> x2 = new ArrayList<>();
            x1.add(0);
            x1.add(2);
            x1.add(0);
            x1.add(0);
            decoupeOpti.addXjtoXi(x2);

            List<Integer> x3 = new ArrayList<>();
            x1.add(0);
            x1.add(0);
            x1.add(3);
            x1.add(0);
            decoupeOpti.addXjtoXi(x3);

            List<Integer> x4 = new ArrayList<>();
            x1.add(0);
            x1.add(0);
            x1.add(0);
            x1.add(7);
            decoupeOpti.addXjtoXi(x4);

            /*fin init matrice*/

            /*init la liste des coeff : case 0 = x1, case 1 = x2 .....*/
            for(int i=0; i<decoupeOpti.nbCol;i++){
                decoupeOpti.addCoefXjtoListXi(1);
            }

            for(int i=0; i<decoupeOpti.nbCol; i++)
                System.out.println(decoupeOpti.coefXi.get(i));
            /*fin init les coefs*/

            try{
                //On créé le probleme
                lp = GLPK.glp_create_prob();
                System.out.println("Problem created");
                GLPK.glp_set_prob_name(lp, "P");

                //Colonnes
                GLPK.glp_add_cols(lp, nbCol);

                for(int i=0; i < nbCol; i++)
                {
                    GLPK.glp_set_col_name(lp, 1, "x" + Integer.toString(i));
                    GLPK.glp_set_col_kind(lp, 1, GLPKConstants.GLP_CV); //Type de la colonne
                    GLPK.glp_set_col_bnds(lp, 1, GLPKConstants.GLP_DB,0,0); // Bornes inf et sup
                }

                ind = GLPK.new_intArray(nbCol+1); //stocker les indices
                for(int k=0; k<decoupeOpti.matriceXi.size();k++){
                    GLPK.intArray_setitem(ind, k, k );
                }
                SWIGTYPE_p_double val = GLPK.new_doubleArray(nbCol + 1); //stocker les valeurs


                // Create constraints
               GLPK.glp_add_rows(lp, nbRows);
                for(int i = 0; i < nbRows; i++) {
                    GLPK.glp_set_row_name(lp, i, "c" + Integer.toString(i));
                    GLPK.glp_set_row_bnds(lp, 1, GLPKConstants.GLP_LO, decoupeOpti.valueConstraint[i], 0);

                    for (int j = 0; j < decoupeOpti.matriceXi.size(); j++) {
                        GLPK.doubleArray_setitem(val, j, decoupeOpti.valueConstraint[j]);
                    }
                    GLPK.glp_set_mat_row(lp,i,decoupeOpti.matriceXi.size(),ind,val);

                }

                // Fin des contraintes

                // fonction objective z

                GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MIN);

            } catch (GlpkException ex){
                ex.printStackTrace();
            }
        }

}
