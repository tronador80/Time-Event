package pawel.sopremo.io.reutersnews;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.tu_berlin.dima.mia.reuters_model.jaxb2.Newsitem;

public class ReutersUnmarshaller
{

	private static JAXBContext context;
	private static  ReutersUnmarshaller instance;
	private final Unmarshaller unmarshaller;
	
	public static ReutersUnmarshaller getInstance() throws JAXBException{
		if(instance==null){
			instance=new ReutersUnmarshaller();
		}
		return instance;
	}
	
	public ReutersUnmarshaller() throws JAXBException{
		this.unmarshaller=createUnmarshaller();
	}
	
	public Newsitem unmarshall(InputStream stream) throws JAXBException{
		return (Newsitem) this.unmarshaller.unmarshal( stream );
	}
	
	public static Marshaller createMarshaller() throws JAXBException{
		JAXBContext context = getContext();
		Marshaller marshaller= context.createMarshaller();
		return marshaller;
	}
	
	public static Unmarshaller createUnmarshaller() throws JAXBException{
		JAXBContext context = getContext();
		Unmarshaller unmarshaller= context.createUnmarshaller();
		return unmarshaller;
	}
	
	public static JAXBContext getContext() throws JAXBException
	{
		if(context==null){
			context = createContext();	
		}
		return context;
	}
	
	public static JAXBContext createContext() throws JAXBException
	{
		return JAXBContext.newInstance( "de.tu_berlin.dima.mia.reuters_model.jaxb2" );
	}

}
