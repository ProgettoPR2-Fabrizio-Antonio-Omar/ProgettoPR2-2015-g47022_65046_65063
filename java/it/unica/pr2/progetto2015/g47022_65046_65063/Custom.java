package it.unica.pr2.progetto2015.g47022_65046_65063;

import org.wikipedia.*;
import net.htmlparser.jericho.*;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import com.memetix.mst.detect.Detect;

import java.io.*;
import java.util.*;
import javax.security.auth.login.FailedLoginException;
import java.net.UnknownHostException;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;

public class Custom implements it.unica.pr2.progetto2015.interfacce.SheetFunction 
{
	/* Attributi per il login su Wikipedia e per utilzzare il servizio di traduzione di Bing */
	private static final String WIKIPEDIA_ID = "ProgettoPR2";
	private static final String WIKIPEDIA_PASSWORD = "Passwordprogetto";
	private static final String BING_CLIENTID = "progetto2015Fabrizio_Antonio_Omar";
	private static final String BING_CLIENTSECRET = "mD+2KUq6O3fCbb90Ngz4Pj2o/zsI1giQhYZhSPFE9cM=";

	public Object execute(Object... args)
	{
		if(args.length == 2)
		{
			String titolo = (String) args[0];
			String lingua = (String) args[1];
			try
			{
				/* Viene cercata la pagina nella lingua richiesta dall'utente */
				if(Custom.exists(titolo, lingua))
				{
					return Custom.getPlainTextFromWikipedia(titolo, lingua);

				}
				/* Se la pagina non viene trovata nella lingua richiesta dall'utente viene cercata in Wikipedia
				 versione inglese */
				else if(Custom.exists(titolo, "en"))
				{
					return Custom.translate((Custom.getPlainTextFromWikipedia(titolo, "en")), lingua);
				}
				/* Se la pagina non viene trovata nemmeno in Wikipedia versione inglese, allora il titolo viene
				 tradotto in lingua inglese e si riesegue la ricerca del titolo tradotto in Wikipedia versione inglese */
				else
				{
					// Traduco il titolo in inglese
					String titoloTradotto = Custom.translate(titolo, "en");
					if(Custom.exists(titoloTradotto, "en"))
					{
						return Custom.translate((Custom.getPlainTextFromWikipedia(titoloTradotto, "en")), lingua);
					}
					/* A questo punto viene restituita una stringa di errore che specifica che la pagina
					 non è stata trovata */
					else
					{
						return "La pagina non è stata trovata.";
					}
				}
       			}
       			/* Questa eccezione viene sollevata nel momento in cui si utilizza un prefisso di lingua non esistente */
       			catch(UnknownHostException e)
       			{
       				return "Il prefisso di lingua \"" + lingua +"\" non esiste.";
       			}
       			/* Questa eccezione viene sollevata se i dati di login a Wikipedia sono errati */
       			catch(FailedLoginException e)
       			{
       				return "Login fallito: userid e/o password potrebbero essere errati o l'account potrebbe non esistere.";
       			}
       			/* Questa eccezione viene sollevata in caso di errori più generici */
       			catch(Exception e)
       			{
       				return "Si è verificato un errore.";
       			}
		}
		else if(args.length == 1)
		{
			String link = (String) args[0];
			try
			{
				return Custom.getPlainTextFromWikipedia(Custom.getTitle(link), Custom.getLanguagePrefix(link));
			}
			catch(Exception e)
			{
				return "URL non valido o pagina non esistente.";
			}
		}
		else
		{
			return "Il numero di parametri è errato.";
		}
	}


	public final String getCategory() 
	{
		return "Testo";
	}

	public final String getHelp() 
	{
		return "Restiuisce il contenuto di una pagina Wikipedia con eventuale traduzione dello stesso.\n\nSintassi:\nEVERYWHEREWIKI(\"Link della pagina Wikipedia\")\n\nEVERYWHEREWIKI(\"Titolo della pagina Wikipedia\", \"Prefisso della lingua\")\n\nEsempio:\n=EVERYWHEREWIKI(\"https://it.wikipedia.org/wiki/Italia\") restiuisce il contenuto della pagina Wikipedia \"Italia\".\n\n=EVERYWHEREWIKI(\"Alan Turing\",\"it\") restituisce il contenuto, in lingua italiana, della pagina Wikipedia \"Alan Turing\".";	
	} 
     
	public final String getName() 
	{
		return "EVERYWHEREWIKI";	
	}
	
	/* La funzione esegue la ricerca della pagina "titoloPagina" nella lingua "lingua" restituendo la conversione dell'html della
	 pagina in testo "normale" */
	private static String getPlainTextFromWikipedia(String titoloPagina, String lingua) throws IOException, FailedLoginException
	{
		String htmlText = new String();
		Wiki wiki = new Wiki(lingua + ".wikipedia.org");
		wiki.setThrottle(5000);
		wiki.login(WIKIPEDIA_ID, WIKIPEDIA_PASSWORD);
		htmlText = wiki.getRenderedText(titoloPagina);
		wiki.logout();
		Source htmlSource = new Source(htmlText);
    		Segment htmlSeg = new Segment(htmlSource, 0, htmlSource.length());
    		Renderer htmlRend = new Renderer(htmlSeg);
    		return(htmlRend.toString());
	}
	
	/* La funzione restituisce true se la pagina "titoloPagina" esiste in Wikipedia nella lingua contrassegnata da
	 "lingua", false se non esiste */
	private static boolean exists(String titoloPagina, String lingua) throws IOException, FailedLoginException
	{
		Map m = new HashMap();
		Wiki wiki = new Wiki(lingua + ".wikipedia.org");
		wiki.setThrottle(5000);
		wiki.login(WIKIPEDIA_ID, WIKIPEDIA_PASSWORD);
		m = wiki.getPageInfo(titoloPagina);
		wiki.logout();
		return (boolean) m.get("exists");
	}
	
	/* La funzione traduce la stringa "stringa" nella lingua "linguaDiDestinazione" */
	private static String translate(String stringa, String linguaDiDestinazione) throws Exception
	{
		Translate.setClientId(BING_CLIENTID);
		Translate.setClientSecret(BING_CLIENTSECRET);
		return Translate.execute(stringa, Language.fromString(linguaDiDestinazione));
	}
	
	/* La funzione restituisce il prefisso della lingua dall'URL di Wikipedia dato in input */
	private static String getLanguagePrefix(String url) throws MalformedURLException
	{
		URL u = new URL(url);
		return u.getHost().substring(0, u.getHost().indexOf("."));
	}
	
	/* La funzione restituisce il titolo della pagina dall'URL di Wikipedia dato in input */
	private static String getTitle(String url) throws MalformedURLException
	{
		URL u = new URL(url);
		return (u.getPath().substring(u.getPath().lastIndexOf("/") + 1, u.getPath().length())).replace('_', ' ');
	}

}
