import org.gnu.glpk.*;

public class main {
        public static void main(String[] args) {
            System.out.println( GLPK.glp_version());
            glp_prob lp;
            glp_smcp parm;
            SWIGTYPE_p_int ind;
            int ret;

            try{
                //On créé le probleme
                lp = GLPK.glp_create_prob();
                System.out.println("Problem created");
                GLPK.glp_set_prob_name(lp, "P");

                //Colonnes
                GLPK.glp_add_cols(lp, 4);

                GLPK.glp_set_col_name(lp, 1, "x1");
                GLPK.glp_set_col_kind(lp, 1, GLPKConstants.GLP_CV); //Type de la colonne
                GLPK.glp_set_col_bnds(lp, 1, GLPKConstants.GLP_DB, 0, .5); // Bornes inf et sup



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
