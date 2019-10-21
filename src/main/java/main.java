import org.gnu.glpk.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*

VM options path:
-Djava.library.path=/usr/lib/x86_64-linux-gnu/jni/

 */

public class main {
        public static void main(String[] args) {
            glp_prob lp;
            glp_smcp parm;
            int ret;
            SWIGTYPE_p_int ind;

            int nbCol = 4;
            int nbRows = 4;

            List <Double> dual = new ArrayList<>();
            List resultat;

            /* init la matrice et la liste des coef

            - init matrice de base x1 x2 x3 x4 */
            DecoupeOpti decoupeOpti = new DecoupeOpti();

            ArrayList<Integer> x1 = new ArrayList<Integer>(Arrays.asList(2,0,0,0));
            decoupeOpti.addXjtoXi(x1);

            ArrayList<Integer> x2 = new ArrayList<Integer>(Arrays.asList(0,2,0,0));
            decoupeOpti.addXjtoXi(x2);

            ArrayList<Integer> x3 = new ArrayList<Integer>(Arrays.asList(0,0,3,0));
            decoupeOpti.addXjtoXi(x3);

            ArrayList<Integer> x4 = new ArrayList<Integer>(Arrays.asList(0,0,0,7));
            decoupeOpti.addXjtoXi(x4);

            /*fin init matrice*/

            /*init la liste des coeff : case 0 = x1, case 1 = x2 .....*/
            for(int i=0; i<decoupeOpti.nbCol;i++){
                decoupeOpti.addCoefXjtoListXi(1);
            }

            /*fin init les coefs*/

     /*       for(int i = 0;i<nbCol; i++){
                System.out.println("");
                for(int j=0; j<nbRows; j++){
                    System.out.print(decoupeOpti.matriceXi.get(i).get(j));
                }
            }*/

            try{
                //On créé le probleme
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

                ind = GLPK.new_intArray(nbCol+1); //stocker les indices
                for(int k=0; k<decoupeOpti.matriceXi.size();k++){
                    GLPK.intArray_setitem(ind, k, k );
                }
                SWIGTYPE_p_double val = GLPK.new_doubleArray(nbCol + 1); //stocker les valeurs

               // Create constraints
               GLPK.glp_add_rows(lp, nbRows);
                for(int i = 1; i <= nbRows; i++) {
                    GLPK.glp_set_row_name(lp, i, "c"+i);
                    GLPK.glp_set_row_bnds(lp, i, GLPKConstants.GLP_FX, decoupeOpti.valueConstraint[i-1], decoupeOpti.valueConstraint[i-1]);

                    for (int j = 1; j <= decoupeOpti.matriceXi.size(); j++) {
                        GLPK.doubleArray_setitem(val, j, decoupeOpti.matriceXi.get(i-1).get(j-1));
                    }
                    GLPK.glp_set_mat_row(lp,i,decoupeOpti.matriceXi.size()-1,ind,val);
                }

                while(true) {

                    // Fin des contraintes

                    // fonction objectif z

                    GLPK.glp_set_obj_name(lp, "z");
                    GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MIN);

                    for(int i = 0; i < decoupeOpti.valueConstraint.length; i++){
                        GLPK.glp_set_obj_coef(lp, i, 1);
                    }

                    parm = new glp_smcp();
                    GLPK.glp_init_smcp(parm);
                    ret = GLPK.glp_simplex(lp, parm);
                    if (ret != 0) {
                        System.out.println("The problem could not be solved");
                    }

                    // fin

                    //   GLPK.glp_write_sol(lp, "p");
                    // GLPK.glp_delete_prob(lp);
                    // fin fonction objectif z

                    GLPK.delete_intArray(ind);
                    GLPK.delete_doubleArray(val);

                    // dual :

                    for (int i = 1; i <= decoupeOpti.matriceXi.size(); i++) {
                        dual.add(GLPK.glp_get_row_dual(lp, i));
                    }

                    SacADos sac = new SacADos(dual);

                    resultat = sac.run();

                    if (sac.calcul() <= 1) {
                        break;
                    } else {
                        GLPK.glp_add_cols(lp, 1);
                        nbCol++;
                        GLPK.glp_set_col_name(lp, nbCol, "x" + Integer.toString(nbCol));
                        GLPK.glp_set_col_kind(lp, nbCol, GLPKConstants.GLP_CV); //Type de la colonne
                        GLPK.glp_set_col_bnds(lp, nbCol, GLPKConstants.GLP_LO,0,0); // Bornes inf et sup
                        decoupeOpti.addXjtoXi(resultat);
                    }
                }

                //Continue si strictement supérieur

                GLPK.glp_write_sol(lp, "p");
                GLPK.glp_delete_prob(lp);

            } catch (GlpkException ex){
                ex.printStackTrace();
            }
        }
}
