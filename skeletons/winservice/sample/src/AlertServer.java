

import java.net.*;
import java.util.*;
import java.io.*;
import javax.swing.JOptionPane;

public class AlertServer
{
    private ServerSocket m_socket;

    public class SListener implements Runnable
    {
	public Socket m_sock;

	public SListener(Socket s)
	{
	    m_sock = s;
	}
	
	public void run()
	{
	    try {
		InputStream is = m_sock.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		String line;
		while ((line=br.readLine()) != null)
		    {
			FileWriter fw = new FileWriter("c:/alertserver.log", true);
			System.out.println("Received: " + line);
			fw.write(line + "\n");
			fw.close();
			JOptionPane.showMessageDialog(null, line, "alert", JOptionPane.ERROR_MESSAGE); 
		    }
		is.close();
	    } catch (IOException iox)
		{
		    iox.printStackTrace();
		}
	}

    }

    public AlertServer()
    {
    }

    public void setup()
    {
	try {
	    m_socket = new ServerSocket(2073);
	} catch (Exception ex)
	    {
		ex.printStackTrace();
	    }
    }
    
    public void listen()
    {
	try {
	    
	    while (true)
		{
		    Socket sock = m_socket.accept();
		    SListener sl = new SListener(sock);
		    new Thread(sl).start();
		}

	} catch (Exception exc)
	    {
		exc.printStackTrace();
	    }
    }

    static public void main(String[] args)
    {
	AlertServer a = new AlertServer();
	a.setup();
	a.listen();
    }
}
