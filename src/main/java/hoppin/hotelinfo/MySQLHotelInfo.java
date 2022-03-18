package hoppin.hotelinfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import hoppin.util.sql.*;


public class MySQLHotelInfo extends MySQLCookie implements MySQLgetHotelNameById {
	
	public MySQLHotelInfo() {
		super();
	}
	
	public MySQLHotelInfo(int i) {
		super();
		id = i;
	}

	public HotelInfo getHotelInfo(int id) {
		HotelInfo hotel = null;
		
		String hotelName = this.getHotelNameById(conn, id);
		String query = "Select * from Hotel where Name = ?";
		
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, hotelName);
			ResultSet rs = ps.executeQuery();
			rs.next();
			HotelInfoBuilder hib = new HotelInfoBuilder(rs);
			hotel = hib.toHotelInfo();
			
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
		return hotel;
	}
	
	public Integer [] getMaxAndCountImageId(int id) {
		
		//return a Integer array of two elements, first is max and second is count
		
		Integer [] res = new Integer[2];
		res[0] = 1;
		res[1] = 0;
		
		String Hname = this.getHotelNameById(conn, id);
		
		String queryMax = "select max(imageId) as max, count(imageId) as count from HotelImages where Hotel = ? ;";
		try {
			PreparedStatement ps = conn.prepareStatement(queryMax);
			ps.setString(1, Hname);
			ResultSet rs = ps.executeQuery();
			
			if ( rs.next() ) {
				res[0] = Integer.valueOf( rs.getInt("max") ) + 1;
				res[1] = Integer.valueOf( rs.getInt("count") );
			}
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
		return res;
	}
	
	public void uploadFileName(int id, String fn) {
		Integer[] res = this.getMaxAndCountImageId(id);
		int max = res[0];
		int count = res[1];
		String Hname = this.getHotelNameById(conn, id);
		
		String query = "insert into HotelImages (Hotel, imageId, filename) VALUES ( ?, ?, ?); ";
		
		try {
			if ( count < 6) {
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, Hname);
				ps.setInt(2, max);
				ps.setString(3, fn);
				
				ps.execute();
			}
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
		
	}
	

	public  String getHotelNameById(int id) {
		
		String HotelName = "";
		try {
			String AccType = this.getAccTypeById(conn, id);
			if ( AccType.equals("Owner")) {
				PreparedStatement ps = conn.prepareStatement("select Name from Hotel where OwnerId = ?");
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				rs.next();
				HotelName = rs.getString("Name");
				
			}else if (AccType.equals("Employee")) {
				PreparedStatement ps = conn.prepareStatement("select Name from Hotel where OwnerId = (select sid from Employee where id = ?) ");
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				rs.next();
				HotelName = rs.getString("Name");
			}
			
		}catch (SQLException e) {
			System.out.println(e);
		}
		
		return HotelName;
	}
	
	public ArrayList<String> getHotelImagesId(int id){
		ArrayList<String> ids = new ArrayList<String>();
		String HotelName = this.getHotelNameById(id);
		String query = "select FileName from HotelImages where Hotel = ?;";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, HotelName);
			ResultSet rs = ps.executeQuery();
			
			while ( rs.next() ) {
				ids.add( rs.getString("FileName") );
			}
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
		
		return ids;
	}
	
	public void editHotelInfo(HotelInfo info) {
		
		/* Da togliere dopo i test
		StringBuilder sb = new StringBuilder();
		sb.append("Update Hotel SET ");
		String query; 
		
		String Hname = info.getName();
		
		int p = 1;
		if ( info.getVia() != null) {
			sb.append("Via = " +  '"' + info.getVia() + '"' );
			p++;
		}
		
		if ( info.getCity() != null ) {
			if ( p > 1) {
				sb.append(", ");
			}
			sb.append("City = " + '"' +  info.getCity() + '"' );
			p++;
		}
		
		if ( info.getPostcode() != null ) {
			if ( p > 1) {
				sb.append(", ");
			}
			sb.append("Postcode = " + '"' + info.getPostcode() + '"' );
			p++;
		}
		
		if ( info.getStars() != 0 ) {
			if ( p > 1) {
				sb.append(", ");
			}
			sb.append("Stars = " + '"' + info.getStars() + '"'  );
			p++;
		}
		
		if ( info.getDescription() != null) {
			if ( p > 1) {
				sb.append(", ");
			}
			sb.append("Description = " + '"' + info.getDescription() + '"' ) ;
			p++;
		}
		
		sb.append(" WHERE Name = " + '"' + Hname + '"' );
		query = sb.toString();
		*/
		
		HotelInfoQueryBuilder hiqb = new HotelInfoQueryBuilder(info, conn);

		try {
			PreparedStatement ps = hiqb.makeStatement();
			ps.execute();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public void deleteImg(int id, String imgId) {
		
		if ( imgId.equals("") || imgId == null) {
			return;
		}
		
		String query = "Delete from HotelImages where FileName = ? ";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, imgId);
			ps.execute();
		} catch (SQLException e) {
			System.out.println(e);
		}
		
	}
	
}
