package it.unica.pr2.progetto2015.g47022_65046_65063;

public class Complessa implements it.unica.pr2.progetto2015.interfacce.SheetFunction {

	
    /** 
    Argomenti in input ed output possono essere solo: String, Integer, Long, Double, Character, Boolean e array di questi tipi.
    Ad esempio a runtime si puo' avere, come elementi di args, un Integer ed un Long[], e restituire un Double[];
    */
    public Object execute(Object... args) {
		Object[][] x = (Object[][]) args;

		Object tmp1, tmp2;
    		for (int i = 0; i < x.length; i++) 
    		{
        		for (int j = i; j < x[i].length; j++) 
        		{
            			tmp1 = x[i][j];
            			tmp2 = x[j][i];
            			x[i][j] = tmp2;
            			x[j][i] = tmp1;
        		}
    		}
		return x;
	}


    /** 
    Restituisce la categoria LibreOffice;
    Vedere: https://help.libreoffice.org/Calc/Functions_by_Category/it
    ad esempio, si puo' restituire "Data&Orario" oppure "Foglio elettronico"
    */
    public final String getCategory() {
		return "Matrice";
	}

    /** Informazioni di aiuto */
    public final String getHelp() {
		return "Traspone le righe e le colonne di una matrice.";
	} 

    /** 
    Nome della funzione.
    vedere: https://help.libreoffice.org/Calc/Functions_by_Category/it
    ad es. "VAL.DISPARI" 
    */         
    public final String getName() {
		return "MATR.TRASPOSTA";
	}

}
