package hoppin.reservation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import hoppin.util.factory.CookieFactory;
import hoppin.util.sql.MySQLConnect;
import jakarta.servlet.http.HttpServletRequest;


/**
 * 
 * Nasconde la costruzione di alcuni oggetti a {@link ReservationManagement} e li restituisce. <br>
 * Implementa {@link hoppin.util.factory.CookieFactory} del quale utilizza
 * {@link #makeCookieGetter(HttpServletRequest)} per ottenere l'id dell'utente autenticato.
 *
 *	Implementa metodi per costruire {@link Reservation} utilizzando {@link ReservationBuilder}
 */
public class ReservationFactory implements CookieFactory {
	
	/**
	 * Crea una connessione al database
	 * @see hoppin.util.sql.MySQLConnect
	 */
	public MySQLConnect makeDatabaseConnect(HttpServletRequest request) {
		int i = this.makeCookieGetter(request).getIdbyCookies();
		MySQLConnect db = (MySQLConnect) new MySQLReservation(i);
		
		return db;
		
	}
	
	/**
	 * Crea un oggetto Reservation utilizzando una HttpServletRequest che ne contiene i parametri
	 * @param request
	 * @return un oggetto Reservation con i parametri inseriti
	 */
	public Reservation makeReservation( HttpServletRequest request ) {
		ReservationBuilder rb = new ReservationBuilder(request);
		
		return rb.toReservation();
	}
	
	/**
	 * Crea un oggetto Reservation utilizzando una HttpServletRequest che ne contiene i parametri e l'id della prenotazione
	 * 
	 * @param request richiesta HTTP che contiene i parametri da inserire nell'oggetto Reservation
	 * @param resId id della prenotazione
	 * @return un oggetto Reservation con i parametri inseriti
	 */
	public Reservation makeReservation(HttpServletRequest request, int resId ) {
		ReservationBuilder rb = new ReservationBuilder(request);
		rb.id(resId);
		return rb.toReservation();
	}
	
	/**
	 * Utilizza il risultato di una query, ResultSet, per costruire una lista di prenotazioni
	 * 
	 * @param rs ResultSet che contiene i dati di tutte le Reservation presi da una query sql
	 * @return un ArrayList di Reservation 
	 * @throws SQLException
	 */
	public ArrayList<Reservation> makeReservationList(ResultSet rs) throws SQLException {
		ArrayList<Reservation> al = new ArrayList<Reservation>();
		
		while (rs.next()) {
			ReservationBuilder rb = new ReservationBuilder(rs);
			al.add( rb.toReservation() );
		}
		
		rs.close();
		
		return al;
	}
}
