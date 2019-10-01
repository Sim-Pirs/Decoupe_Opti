import org.gnu.glpk.glp_prob;

public class main {
        public static void main(String[] args) {
            double solutionOptimale = 0;
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

    }

}
