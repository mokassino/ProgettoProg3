package hoppin.reservation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import hoppin.util.sql.*;

/**
 * 
 * Effettua operazioni CRUD relative al sottosistema ReservationManagement su database MySQL
 * Si collega al database estendendo la superclasse {@link hoppin.util.sql.MySQLConnect}
 * 
 */
public class MySQLReservation extends MySQLConnect implements MySQLgetHotelNameById {
	
	/**
	 * 
	 * @param i è l'id dell'utente autenticato.
	 */
	public MySQLReservation(int i) {
		super();
		id = i;
	}
	
	/**
	 * 
	 * @return restituisce un ArrayList di {@code Reservation} per l'Hotel dell'utente autenticato
	 * @see hoppin.reservation.Reservation
	 */
	public ArrayList<Reservation> getReservationList(){	
		
		try {			
			String HotelName = this.getHotelNameById(conn, id);
			
			
			PreparedStatement pss = conn.prepareStatement("select id,Name,Number, Check_In, Check_Out, Package from Reservation where Hotel = ?");
			pss.setString(1, HotelName);
			ResultSet rs = pss.executeQuery();
			
			ReservationFactory factory = new ReservationFactory();
			ArrayList<Reservation> al = factory.makeReservationList(rs);
			
			rs.close();
			pss.close();	
			
			return al;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}

	}
	
	/**
	 * Aggiunge una nuova prenotazione al database con i parametri presenti nella prenotazione passa in input
	 * @param res istanza di Reservation con tutti i dati della prenotazione da inserire nel database
	 * @return {@code true} se la prenotazione è inserita con successo, altrimenti {@code false}
	 */
	public boolean addReservation(Reservation res) {
		try {
			PreparedStatement ps = conn.prepareStatement("select max(id) as id from Reservation");
			ResultSet rs = ps.executeQuery();
			int ReservationId = -1;
			if ( rs.next() ) {
				ReservationId = rs.getInt("id") + 1;
			}else {
				return false;
			}
			rs.close();
			ps.close();
			
			//get Hotel Name
			ps = conn.prepareStatement("select Hotel from Room where Number= ?;");
			ps.setString(1, res.getRoomNum());
			rs = ps.executeQuery();
			rs.next();
			String HotelName = rs.getString("Hotel");
			
			ps = conn.prepareStatement("INSERT INTO Reservation (Name, id, Hotel, Number, Check_In, Check_Out, Package) "
					+ " VALUES (?, ?, ?, ?, STR_TO_DATE( ?,  '%d-%m-%Y'), STR_TO_DATE( ?, '%d-%m-%Y' ) , ? )");
			
			
			ps.setString(1, res.getCustomerName());
			ps.setInt(2, ReservationId);
			ps.setString(3, HotelName);
			ps.setString(4, res.getRoomNum());
			ps.setString(5, res.getCheckIn());
			ps.setString(6, res.getCheckOut());
			ps.setString(7, res.getPckg());
			ps.execute();
			
			ps.close();
			rs.close();
			return true;
			
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}
	
	/**
	 * Modifica una prenotazione già esistente con i parametri presenti nella prenotazione passata in input
	 * @param res istanza di Reservation con i dati da inserire al posto di quelli già presenti
	 * @return {@code true} se la prenotazione è modificata con successo, altrimenti {@code false}
	 */
	public boolean editReservation(Reservation res) {
		
		ReservationQueryBuilder rqb = new ReservationQueryBuilder(res, conn);
		
		try {	
			PreparedStatement ps = rqb.makeStatement();
			
			if ( ps != null) {
				ps.executeUpdate();
				ps.close();
			}
			
		} catch ( SQLException e) {
			System.out.println(e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Elimina una prenotazione a partire dall'id
	 * @param resId reservationId, id della prenotazione
	 * @return {@code true} se viene eliminato con successo, altrimenti {@code false}
	 */
	public boolean deleteReservation(int resId) {
		try {
			
			PreparedStatement ps = conn.prepareStatement("delete from Reservation where id = ? ");
			ps.setInt(1, resId);
			ps.execute();
		
			ps.close();
		
		} catch ( SQLException e) {
			System.out.println(e);
			return false;
		}
		
		return true;
	}
	
}
