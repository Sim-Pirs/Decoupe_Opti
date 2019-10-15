import org.gnu.glpk.*;

public class main {
        public static void main(String[] args) {
            System.out.println( GLPK.glp_version());
            glp_prob lp;
            glp_smcp parm;
            SWIGTYPE_p_int ind;
            int ret;

            int nbCol = 4;
            int nbRows = 4;
            int valueConstraint[] = { 97, 610, 395, 211 };

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

                // Create constraints
                GLPK.glp_add_rows(lp, nbRows);
                for(int i = 0; i < nbRows; i++){
                    GLPK.glp_set_row_name(lp, i, "c"+Integer.toString(i));
                    GLPK.glp_set_row_bnds(lp, 1, GLPKConstants.GLP_LO, valueConstraint[i], 0);
                    ind = GLPK.new_intArray(3);
                    GLPK.intArray_setitem(ind, 1, 1);
                    GLPK.intArray_setitem(ind, 2, 2);
                    val = GLPK.new_doubleArray(3);
                    GLPK.doubleArray_setitem(val, 1, 1.);
                    GLPK.doubleArray_setitem(val, 2, -1.);
                    GLPK.glp_set_mat_row(lp, 1, 2, ind, val);
                }


                // Define objective
                GLPK.glp_set_obj_name(lp, "z");
                GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MIN);
                GLPK.glp_set_obj_coef(lp, 0, 1.);
                GLPK.glp_set_obj_coef(lp, 1, -.5);
                GLPK.glp_set_obj_coef(lp, 2, .5);

            } catch (GlpkException ex){
                ex.printStackTrace();
            }
        }


         /*   double solutionOptimale = 0;
            Object colonne;

            double[] coef;
            double[] row;
            double[] column;

            glp_prob j = new glp_prob();

            selectionnerColonnes();

            while(true){

                resoudreProgramme();

                recupererSolutionDuale();

                resoudreSacADos();

                if(solutionOptimale <= 1){
                    break;
                }
                ajouterColonne(colonne);
            }

    }

    public static void ajouterColonne(Object colonne){

    }

    public static void selectionnerColonnes(){

    }

    public static void resoudreProgramme(){

    }

    public static void recupererSolutionDuale(){

    }

    public static void resoudreSacADos(){

    }*/

}
